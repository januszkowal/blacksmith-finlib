package org.blacksmith.finlib.schedule.policy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.interestbasis.ScheduleParameters;
import org.blacksmith.finlib.rates.interestrates.InterestRateId;
import org.blacksmith.finlib.rates.interestrates.InterestRateService;
import org.blacksmith.finlib.schedule.InterestRateType;
import org.blacksmith.finlib.schedule.ScheduleComposePolicy;
import org.blacksmith.finlib.schedule.events.InterestEvent;
import org.blacksmith.finlib.schedule.events.InterestEventSrc;
import org.blacksmith.finlib.schedule.events.interest.CashflowInterestEvent;
import org.blacksmith.finlib.schedule.events.interest.RateResetEvent;
import org.blacksmith.finlib.schedule.events.schedule.PrincipalsHolder;
import org.blacksmith.finlib.schedule.events.schedule.ScheduleInterestEvent;

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
    splitResets(cashflows);
    boolean principalsUpdated = updatePrincipals(cashflows);
    boolean ratesUpdated = updateRates(cashflows);
    if (principalsUpdated || ratesUpdated) {
      calculateInterest(cashflows);
    }
    return cashflows;
  }

  private boolean updateRates(List<CashflowInterestEvent> cashflows) {
    boolean changed = false;
    Rate newRate = Rate.ZERO;
    for (CashflowInterestEvent cashflow: cashflows) {
      if (cashflow.getSubEvents().isEmpty()) {
        newRate = getInterestRate(cashflow.getStartDate(),cashflow.getEndDate());
        if (cashflow.setInterestRate(newRate)) {
          changed = true;
        }
      }
      else {
        for (int i=0; i<cashflow.getSubEvents().size(); i++) {
          RateResetEvent rr = cashflow.getSubEvents().get(i);
          if (i==0 || rr.isRateReset()) {
            newRate = getInterestRate(rr.getStartDate(),rr.getEndDate());
          }
          if (rr.setInterestRate(newRate)) {
            changed = true;
          }
        }
        if (cashflow.setInterestRate(cashflow.firstRateReset().getInterestRate())) {
          changed = true;
        }
      }
    }
    return changed;
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

  private void splitResets(List<CashflowInterestEvent> cashflows) {
    if (principalsHolder.isEmpty()) return;
    principalsHolder.getEvents().forEach(pe->{
      var ie = InterestEventSrc.getEventInRange(cashflows,pe.getDate());
      if (ie!=null) {
        ie.splitRateReset(pe.getDate());
      }
    });
  }

  private boolean updatePrincipals(List<CashflowInterestEvent> cashflows) {
    boolean changed=false;
    for (CashflowInterestEvent cashflow: cashflows) {
      if (cashflow.getSubEvents().isEmpty()) {
        Amount newPrincipal = principalsHolder.getPrincipal(cashflow.getStartDate());
        if (cashflow.setPrincipal(newPrincipal)) {
          changed = true;
        }
      }
      else {
        for (RateResetEvent rr: cashflow.getSubEvents()) {
          if (rr.setPrincipal(principalsHolder.getPrincipal(rr.getStartDate()))) {
            changed = true;
          }
        }
        if (cashflow.setPrincipal(cashflow.firstRateReset().getPrincipal())) {
          changed = true;
        }
      }
    }
    return changed;
  }
}
