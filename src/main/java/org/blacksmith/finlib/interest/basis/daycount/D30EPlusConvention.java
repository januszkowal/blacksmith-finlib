package org.blacksmith.finlib.interest.basis.daycount;

import java.time.LocalDate;

import org.blacksmith.finlib.dayconvention.utils.DayCountUtils;
import org.blacksmith.finlib.dayconvention.utils.YmdDate;
import org.blacksmith.finlib.interest.basis.ScheduleInfo;

public class D30EPlusConvention extends AbstractConstantDenominatorConvention {

  public D30EPlusConvention(double denominator) {
    super(denominator);
  }

  @Override
  public long calculateDays(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    YmdDate date1 = YmdDate.of(startDate);
    YmdDate date2 = YmdDate.of(endDate);
    if (date1.getDay() == 31) {
      date1.setDay(30);
    }
    if (date2.getDay() == 31) {
      date2.setFirstDayOfNextMonth();
    }
    return DayCountUtils.days360(date1, date2);
  }
}
