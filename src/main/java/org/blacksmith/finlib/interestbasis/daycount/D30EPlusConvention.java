package org.blacksmith.finlib.interestbasis.daycount;

import java.time.LocalDate;
import org.blacksmith.finlib.dayconvention.utils.DayCountUtils;
import org.blacksmith.finlib.dayconvention.utils.YMD;
import org.blacksmith.finlib.interestbasis.ScheduleInfo;

public class D30EPlusConvention extends AbstractSimpleConvention {

  public D30EPlusConvention(double denominator) {
    super(denominator);
  }

  @Override
  public int calculateDays(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    YMD date1 = YMD.of(startDate);
    YMD date2 = YMD.of(endDate);
    if (date1.getDay()==31) {
      date1.setDay(30);
    }
    if (date2.getDay()==31) {
      date2.setFirstDayOfNextMonth();
    }
    return DayCountUtils.days360(date1, date2);
  }
}
