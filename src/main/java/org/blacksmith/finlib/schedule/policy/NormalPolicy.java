package org.blacksmith.finlib.schedule.policy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.blacksmith.commons.counter.BooleanStateCounter;
import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.schedule.ScheduleParameters;
import org.blacksmith.finlib.rates.interestrates.InterestRateId;
import org.blacksmith.finlib.rates.interestrates.InterestRateService;
import org.blacksmith.finlib.schedule.InterestRateType;
import org.blacksmith.finlib.schedule.ScheduleComposePolicy;
import org.blacksmith.finlib.schedule.events.interest.CashflowInterestEvent;
import org.blacksmith.finlib.schedule.events.interest.RateResetEvent;
import org.blacksmith.finlib.schedule.events.schedule.PrincipalsHolder;
import org.blacksmith.finlib.schedule.timetable.TimetableInterestEntry;

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

  public Function<List<CashflowInterestEvent>, List<CashflowInterestEvent>> getUpdater() {
    return new CascadeFunction<>(List.of(
        new RateResetSplitUpdater(principalsHolder),
        new PrincipalUpdater(principalsHolder),
        new RateUpdater(scheduleParameters, interestRateService)
    ));
  }

  public Function<List<CashflowInterestEvent>, List<CashflowInterestEvent>> getUpdater1() {
    return new RateResetSplitUpdater(principalsHolder)
        .andThen(new PrincipalUpdater(principalsHolder))
        .andThen(new RateUpdater(scheduleParameters, interestRateService));
  }

  @Override
  public List<CashflowInterestEvent> create(List<TimetableInterestEntry> events) {
    var cashflows = events.stream()
        .map(se -> CashflowInterestEvent.builder()
            .startDate(se.getStartDate())
            .endDate(se.getEndDate())
            .paymentDate(se.getPaymentDate())
            .subEvents(se.getSubEvents().stream()
                .map(sr -> RateResetEvent.builder()
                    .startDate(sr.getStartDate())
                    .endDate(sr.getEndDate())
                    //                    .principal(principalsHolder.getPrincipal(sr.getStartDate()))
                    //                    .interestRate(getInterestRate(sr.getStartDate(), sr.getEndDate()))
                    .isRateReset(true)
                    .build()).collect(Collectors.toList()))
            .build())
        .collect(Collectors.toList());
    cashflows = getUpdater().apply(cashflows);
    calculateInterest(cashflows);
    return cashflows;
  }

  @Override
  public List<CashflowInterestEvent> update(List<CashflowInterestEvent> cashflows) {
    BooleanStateCounter change = new BooleanStateCounter();
    //keep original to detect if changed
    var original = CashflowInterestEvent.copy(cashflows);
    var updated = getUpdater().apply(cashflows);
    if (!CollectionUtils.isEqualCollection(original, updated)) {
      log.info("Recalculate interest");
      calculateInterest(updated);
    }
    return updated;
  }

  private void calculateInterest(List<CashflowInterestEvent> cashflows) {
    for (CashflowInterestEvent cashflow : cashflows) {
      if (cashflow.getSubEvents().isEmpty()) {
        Amount interest = calculateInterest(cashflow);
        cashflow.setInterest(interest);
        cashflow.setAmount(interest);
      } else {
        cashflow.getSubEvents().forEach(rr -> {
          Amount interest = calculateInterest(rr);
          rr.setInterest(interest);
        });
        Amount interestSum = cashflow.getSubEvents().stream()
            .map(RateResetEvent::getInterest)
            .reduce(Amount.ZERO, Amount::add);
        cashflow.setInterest(interestSum);
        cashflow.setAmount(interestSum);
      }
    }
  }

  private Rate getInterestRate(LocalDate startDate, LocalDate endDate) {
    if (scheduleParameters.getInterestRateType() == InterestRateType.CONST) {
      return scheduleParameters.getStartInterestRate();
    } else {
      InterestRateId rateKey = InterestRateId.of(scheduleParameters.getInterestTable(),
          scheduleParameters.getCouponFrequency().toString(),
          scheduleParameters.getCurrency());
      //fixingDate
      return Optional.ofNullable(interestRateService.getRateValue(rateKey, startDate))
          .map(r -> r.multiply(scheduleParameters.getInterestRateMulMargin()))
          .orElse(Rate.ZERO)
          .add(scheduleParameters.getInterestRateAddMargin());
      //      xrate := Yield_Pkg.get_fra_DD(IntParam.yieldCurveId,
      //          IntParam.ccyId, Accpkg.procdate, resetDate, endDate + 1);

    }
  }
}
