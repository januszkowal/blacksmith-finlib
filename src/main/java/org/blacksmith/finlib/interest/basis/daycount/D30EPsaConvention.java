package org.blacksmith.finlib.interest.basis.daycount;

import java.time.LocalDate;

import org.blacksmith.finlib.dayconvention.utils.DayCountUtils;
import org.blacksmith.finlib.dayconvention.utils.YmdDate;
import org.blacksmith.finlib.interest.basis.ScheduleInfo;

import static org.blacksmith.commons.datetime.DateUtils.isLastDayOfFebruary;

public class D30EPsaConvention extends AbstractConstantDenominatorConvention {

  public D30EPsaConvention(double denominator) {
    super(denominator);
  }

  @Override
  public long calculateDays(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    YmdDate date1 = YmdDate.of(startDate);
    YmdDate date2 = YmdDate.of(endDate);
    if (date1.getDay() == 31 || isLastDayOfFebruary(startDate))
      date1.setDay(30);
    if (date2.getDay() == 31 && date1.getDay() == 30) {
      date2.setDay(30);
    }
    return DayCountUtils.days360(date1, date2);
  }
}