package org.blacksmith.finlib.schedule.policy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.blacksmith.commons.counter.BooleanStateCounter;
import org.blacksmith.commons.property.PropertyUpdater;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.schedule.ScheduleParameters;
import org.blacksmith.finlib.rates.interestrates.InterestRateId;
import org.blacksmith.finlib.rates.interestrates.InterestRateService;
import org.blacksmith.finlib.schedule.InterestRateType;
import org.blacksmith.finlib.schedule.ScheduleUpdater;
import org.blacksmith.finlib.schedule.events.interest.CashflowInterestEvent;
import org.blacksmith.finlib.schedule.events.interest.RateResetEvent;

public class RateUpdater implements ScheduleUpdater {
  private final ScheduleParameters scheduleParameters;
  private final InterestRateService interestRateService;

  public RateUpdater(ScheduleParameters scheduleParameters, InterestRateService interestRateService) {
    this.scheduleParameters = scheduleParameters;
    this.interestRateService = interestRateService;
  }

  @Override
  public List<CashflowInterestEvent> apply(List<CashflowInterestEvent> cashflows) {
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
    //return change.hasTrue();
    return cashflows;
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
}
