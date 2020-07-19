package org.blacksmith.finlib.schedule.policy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.blacksmith.commons.counter.BooleanStateCounter;
import org.blacksmith.commons.property.PropertyUpdater;
import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.interestbasis.ScheduleParameters;
import org.blacksmith.finlib.rates.interestrates.InterestRateId;
import org.blacksmith.finlib.rates.interestrates.InterestRateService;
import org.blacksmith.finlib.schedule.InterestRateType;
import org.blacksmith.finlib.schedule.ScheduleComposePolicy;
import org.blacksmith.finlib.schedule.events.InterestEventSrc;
import org.blacksmith.finlib.schedule.events.interest.CashflowInterestEvent;
import org.blacksmith.finlib.schedule.events.interest.RateResetEvent;
import org.blacksmith.finlib.schedule.events.schedule.PrincipalsHolder;
import org.blacksmith.finlib.schedule.events.schedule.ScheduleInterestEvent;
import org.blacksmith.finlib.schedule.events.schedule.SchedulePrincipalEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NormalPolicy extends AbstractScheduleAlgorithmPolicy implements ScheduleComposePolicy {

  private final PrincipalsHolder principalsHolder;
  private final InterestRateService interestRateService;

  public NormalPolicy(ScheduleParameters scheduleParameters,
      PrincipalsHolder principalsHolder, InterestRateService interestRateService) {
    super(scheduleParameters);
    this.principalsHolder = principalsHolder;
    this.interestRateService = interestRateService;
  }

  @Override
  public List<CashflowInterestEvent> create(List<ScheduleInterestEvent> events) {
    var cashflows = events.stream()
        .map(se->CashflowInterestEvent.builder()
            .startDate(se.getStartDate())
            .endDate(se.getEndDate())
            .paymentDate(se.getPaymentDate())
            .subEvents(se.getSubEvents().stream()
                .map(sr-> RateResetEvent.builder()
                    .startDate(sr.getStartDate())
                    .endDate(sr.getEndDate())
                    .principal(principalsHolder.getPrincipal(sr.getStartDate()))
                    .interestRate(getInterestRate(sr.getStartDate(),sr.getEndDate()))
                    .isRateReset(true)
                    .build()).collect(Collectors.toList()))
            .build())
        .collect(Collectors.toList());
    //split resets
    splitResets(cashflows);
    //updatePrincipals(cashflows);
    calculateInterest(cashflows);
    return cashflows;
  }

  @Override
  public List<CashflowInterestEvent> update(List<CashflowInterestEvent> cashflows) {
    BooleanStateCounter change = new BooleanStateCounter();
    change.update(splitResets(cashflows));
    change.update(updatePrincipals(cashflows));
    change.update(updateRates(cashflows));
    if (change.hasTrue()) {
      calculateInterest(cashflows);
    }
    return cashflows;
  }

  private boolean updateRates(List<CashflowInterestEvent> cashflows) {
    BooleanStateCounter change = new BooleanStateCounter();
    Rate newRate = Rate.ZERO;
    PropertyUpdater<CashflowInterestEvent,Rate> cashflowRateUpdater =
        new PropertyUpdater<>(CashflowInterestEvent::getInterestRate,CashflowInterestEvent::setInterestRate);
    PropertyUpdater<RateResetEvent,Rate> rateResetRateUpdater =
        new PropertyUpdater<>(RateResetEvent::getInterestRate,RateResetEvent::setInterestRate);
    for (CashflowInterestEvent cashflow: cashflows) {
      if (cashflow.getSubEvents().isEmpty()) {
        newRate = getInterestRate(cashflow.getStartDate(),cashflow.getEndDate());
        change.update(cashflowRateUpdater.set(cashflow,newRate));
      }
      else {
        for (int i=0; i<cashflow.getSubEvents().size(); i++) {
          RateResetEvent rr = cashflow.getSubEvents().get(i);
          if (i==0 || rr.isRateReset()) {
            newRate = getInterestRate(rr.getStartDate(),rr.getEndDate());
          }
          change.update(rateResetRateUpdater.set(rr,newRate));
        }
        change.update(cashflowRateUpdater.set(cashflow,cashflow.firstRateReset().getInterestRate()));
      }
    }
    return change.hasTrue();
  }

  private void calculateInterest(List<CashflowInterestEvent> cashflows) {
    for (CashflowInterestEvent cashflow: cashflows) {
      if (cashflow.getSubEvents().isEmpty()) {
        Amount interest = calculateInterest(cashflow);
        cashflow.setInterest(interest);
        cashflow.setAmount(interest);
      }
      else {
        cashflow.getSubEvents().forEach(rr->{
          Amount interest = calculateInterest(rr);
          rr.setInterest(interest);
        });
        Amount interestSum = cashflow.getSubEvents().stream()
            .map(rr->rr.getInterest())
            .reduce(Amount.ZERO,Amount::add);
        cashflow.setInterest(interestSum);
        cashflow.setAmount(interestSum);
      }
    }
  }

  private Rate getInterestRate(RateResetEvent rateResetEvent) {
    return getInterestRate(rateResetEvent.getStartDate(),rateResetEvent.getEndDate());
  }

  private Rate getInterestRate(LocalDate startDate, LocalDate endDate) {
    if (scheduleParameters.getInterestRateType()== InterestRateType.CONST) {
      return scheduleParameters.getStartInterestRate();
    }
    else {
      InterestRateId rateKey = InterestRateId.of(scheduleParameters.getInterestTable(),
          scheduleParameters.getCouponFrequency().toString(),
          scheduleParameters.getCurrency());
      //fixingDate
      return Optional.ofNullable(interestRateService.getRateValue(rateKey,startDate))
          .map(r->r.multiply(scheduleParameters.getInterestRateMulMargin()))
          .orElse(Rate.ZERO)
          .add(scheduleParameters.getInterestRateAddMargin());
      //      xrate := Yield_Pkg.get_fra_DD(IntParam.yieldCurveId,
      //          IntParam.ccyId, Accpkg.procdate, resetDate, endDate + 1);
    }
  }

  private boolean splitResets(List<CashflowInterestEvent> cashflows) {
    if (principalsHolder.isEmpty()) return false;
    BooleanStateCounter stateCounter = new BooleanStateCounter();
    for (SchedulePrincipalEvent pe: principalsHolder.getEvents()) {
      var ie = InterestEventSrc.getEventInRange(cashflows,pe.getDate());
      if (ie!=null) {
        stateCounter.update(ie.splitSubEvent(pe.getDate()));
      }
    }
    return stateCounter.hasTrue();
  }

  private boolean updatePrincipals(List<CashflowInterestEvent> cashflows) {
    BooleanStateCounter stateCounter = new BooleanStateCounter();
    PropertyUpdater<CashflowInterestEvent,Amount> cashflowPrincipalUpdater =
        new PropertyUpdater<>(CashflowInterestEvent::getPrincipal,CashflowInterestEvent::setPrincipal);
    PropertyUpdater<RateResetEvent,Amount> rateResetPrincipalUpdater =
        new PropertyUpdater<>(RateResetEvent::getPrincipal,RateResetEvent::setPrincipal);
    for (CashflowInterestEvent cashflow: cashflows) {
      if (cashflow.getSubEvents().isEmpty()) {
        Amount newPrincipal = principalsHolder.getPrincipal(cashflow.getStartDate());
        stateCounter.update(cashflowPrincipalUpdater.set(cashflow,newPrincipal));
      }
      else {
        for (RateResetEvent rr: cashflow.getSubEvents()) {
          Amount newPrincipal = principalsHolder.getPrincipal(rr.getStartDate());
          stateCounter.update(rateResetPrincipalUpdater.set(rr,newPrincipal));
        }
        stateCounter.update(cashflowPrincipalUpdater.set(cashflow,cashflow.firstRateReset().getPrincipal()));
      }
    }
    return stateCounter.hasTrue();
  }
}
