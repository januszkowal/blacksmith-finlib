package org.blacksmith.finlib.schedule.policy;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.blacksmith.commons.counter.BooleanStateCounter;
import org.blacksmith.finlib.schedule.ScheduleParameters;
import org.blacksmith.finlib.rates.interestrates.InterestRateService;
import org.blacksmith.finlib.schedule.ScheduleComposePolicy;
import org.blacksmith.finlib.schedule.events.interest.CashflowInterestEvent;
import org.blacksmith.finlib.schedule.events.interest.RateResetEvent;
import org.blacksmith.finlib.schedule.events.schedule.PrincipalsHolder;
import org.blacksmith.finlib.schedule.helper.PrincipalUpdater;
import org.blacksmith.finlib.schedule.timetable.TimetableInterestEntry;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NormalPolicy extends AbstractScheduleAlgorithmPolicy implements ScheduleComposePolicy {

  private final PrincipalsHolder principalsHolder;
  private final InterestRateService interestRateService;
  private final InterestCalculator interestCalculator;

  public NormalPolicy(ScheduleParameters scheduleParameters,
      PrincipalsHolder principalsHolder, InterestRateService interestRateService) {
    super(scheduleParameters);
    this.principalsHolder = principalsHolder;
    this.interestRateService = interestRateService;
    this.interestCalculator = new InterestCalculator(scheduleParameters);
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
    cashflows = this.interestCalculator.apply(cashflows);
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
      updated = this.interestCalculator.apply(updated);
    }
    return updated;
  }
}
