package org.blacksmith.finlib.interestbasis.daycount;

import static org.blacksmith.commons.datetime.DateUtils.isLastDayOfFebruary;

import java.time.LocalDate;
import org.blacksmith.finlib.dayconvention.utils.DayCountUtils;
import org.blacksmith.finlib.dayconvention.utils.YmdDate;
import org.blacksmith.finlib.interestbasis.ScheduleInfo;

public class D30USEomConvention extends AbstractSimpleConvention {

  public D30USEomConvention(double denominator) {
    super(denominator);
  }

  @Override
  public int calculateDays(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    YmdDate date1 = YmdDate.of(startDate);
    YmdDate date2 = YmdDate.of(endDate);
    if (isLastDayOfFebruary(startDate)) {
      if (isLastDayOfFebruary(endDate)) {
        date2.setDay(30);
      }
      date1.setDay(30);
    }
    if (date1.getDay() == 31) {
      date1.setDay(30);
    }
    if (date2.getDay() == 31 && date1.getDay() == 30) {
      date2.setDay(30);
    }
    return DayCountUtils.days360(date1, date2);
  }
}
