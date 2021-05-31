package org.blacksmith.finlib.interest.schedule.policy.helper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.blacksmith.commons.counter.BooleanStateCounter;
import org.blacksmith.commons.property.PropertyUpdater;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rate.intrate.InterestRateId;
import org.blacksmith.finlib.rate.intrate.InterestRateService;
import org.blacksmith.finlib.interest.schedule.InterestRateIndexation;
import org.blacksmith.finlib.interest.schedule.ScheduleParameters;
import org.blacksmith.finlib.interest.schedule.events.InterestEvent;
import org.blacksmith.finlib.interest.schedule.events.RateResetEvent;
import org.blacksmith.finlib.interest.schedule.policy.ScheduleUpdater;

public class RateUpdater implements ScheduleUpdater {
  private final ScheduleParameters scheduleParameters;
  private final InterestRateService interestRateService;

  public RateUpdater(ScheduleParameters scheduleParameters, InterestRateService interestRateService) {
    this.scheduleParameters = scheduleParameters;
    this.interestRateService = interestRateService;
  }

  @Override
  public List<InterestEvent> apply(List<InterestEvent> cashflows) {
    BooleanStateCounter change = new BooleanStateCounter();
    Rate newRate = Rate.ZERO;
    PropertyUpdater<InterestEvent, Rate> cashflowRateUpdater =
        new PropertyUpdater<>(InterestEvent::getInterestRate, InterestEvent::setInterestRate);
    PropertyUpdater<RateResetEvent, Rate> rateResetRateUpdater =
        new PropertyUpdater<>(RateResetEvent::getInterestRate, RateResetEvent::setInterestRate);
    for (InterestEvent cashflow : cashflows) {
      if (cashflow.getSubEvents().isEmpty()) {
        newRate = getInterestRate(cashflow.getStartDate(), cashflow.getEndDate());
        change.update(cashflowRateUpdater.set(cashflow, newRate));
      } else {
        for (int i = 0; i < cashflow.getSubEvents().size(); i++) {
          RateResetEvent rr = cashflow.getSubEvents().get(i);
          if (i == 0 || rr.isRateReset()) {
            newRate = getInterestRate(rr.getStartDate(), rr.getEndDate());
          }
          change.update(rateResetRateUpdater.set(rr, newRate));
        }
        change.update(cashflowRateUpdater.set(cashflow, cashflow.firstRateReset().getInterestRate()));
      }
    }
    //return change.hasTrue();
    return cashflows;
  }

  private Rate getInterestRate(RateResetEvent rateResetEvent) {
    return getInterestRate(rateResetEvent.getStartDate(), rateResetEvent.getEndDate());
  }

  private Rate getInterestRate(LocalDate startDate, LocalDate endDate) {
    if (scheduleParameters.getIndexation() == InterestRateIndexation.FIXED) {
      return scheduleParameters.getFixedRate();
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
