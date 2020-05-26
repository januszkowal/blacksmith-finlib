package org.blacksmith.finlib.schedule;

import java.time.LocalDate;
import org.blacksmith.finlib.interestbasis.ScheduleInfo;
import org.blacksmith.finlib.interestbasis.ScheduleParameters;
import org.blacksmith.finlib.basic.Amount;

public class TermScheduleGenerator implements ScheduleGenerator {

  private final ScheduleParameters scheduleParameters;

  public TermScheduleGenerator(ScheduleParameters scheduleParameters) {
    this.scheduleParameters = scheduleParameters;
  }

  private ScheduleInfo createScheduleInfo(ScheduleParameters scheduleParameters, LocalDate couponStartDate, LocalDate couponEndDate) {
    return ScheduleInfo.builder()
        .isEndOfMonthConvention(scheduleParameters.isEndOfMonthConvention())
        .couponFrequency(scheduleParameters.getCouponFrequency())
        .startDate(scheduleParameters.getStartDate())
        .maturityDate(scheduleParameters.getMaturityDate())
        .couponStartDate(couponStartDate)
        .couponEndDate(couponEndDate)
        .startInterestRate(scheduleParameters.getStartInterestRate())
        .build();
  }
  @Override
  public Schedule generate() {
    var schedule = new Schedule();
    ScheduleInfo scheduleInfo = createScheduleInfo(scheduleParameters,scheduleParameters.getStartDate(),scheduleParameters.getMaturityDate());
    double rate = scheduleParameters.getStartInterestRate().doubleValue() / 100.0;
    double interest =
        scheduleParameters.getBasis().yearFraction(scheduleInfo.getCouponStartDate(), scheduleInfo.getCouponEndDate(), scheduleInfo)*
        rate*
        scheduleInfo.getNotional(scheduleInfo.getCouponStartDate()).doubleValue();
    
    Cashflow cashflow = Cashflow.builder()
        .startDate(scheduleParameters.getStartDate())
        .endDate(scheduleParameters.getMaturityDate())
        .paymentDate(scheduleParameters.getMaturityDate())
        .notional(scheduleParameters.getNotional())
        .amount(Amount.of(interest))
        .build();
    schedule.getCashflow().add(cashflow);
    return schedule;
  }
}
