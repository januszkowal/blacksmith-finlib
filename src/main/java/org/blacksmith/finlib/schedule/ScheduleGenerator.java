package org.blacksmith.finlib.schedule;

import java.time.LocalDate;
import org.blacksmith.finlib.interestbasis.ScheduleInfo;
import org.blacksmith.finlib.interestbasis.ScheduleParameters;

public interface ScheduleGenerator {
  Schedule generate();
  
  default ScheduleInfo createScheduleInfo(ScheduleParameters scheduleParameters, LocalDate couponStartDate, LocalDate couponEndDate) {
    return ScheduleInfo.builder()
        .isEndOfMonthConvention(scheduleParameters.isEndOfMonthConvention())
        .couponFrequency(scheduleParameters.getCouponFrequency())
        .startDate(scheduleParameters.getStartDate())
        .maturityDate(scheduleParameters.getMaturityDate())
        .couponStartDate(couponStartDate)
        .couponEndDate(couponEndDate)
        .notional(scheduleParameters.getNotional())
        .build();
  }
}
