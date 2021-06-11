package org.blacksmith.finlib.interest.basis.daycount;

import java.time.LocalDate;

import org.blacksmith.finlib.dayconvention.utils.DayCountUtils;
import org.blacksmith.finlib.dayconvention.utils.YmdDate;
import org.blacksmith.finlib.interest.basis.ScheduleInfo;

import static org.blacksmith.commons.datetime.DateUtils.isLastDayOfFebruary;

public class D30EIsdaConvention extends AbstractConstantDenominatorConvention {

  public D30EIsdaConvention(double denominator) {
    super(true, denominator);
  }

  @Override
  public long calculateDays(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo) {
    YmdDate date1 = YmdDate.of(firstDate);
    YmdDate date2 = YmdDate.of(secondDate);
    if (date1.getDay() == 31 || isLastDayOfFebruary(firstDate))
      date1.setDay(30);
    if (date2.getDay() == 31 || (isLastDayOfFebruary(secondDate) && !secondDate.equals(scheduleInfo.getEndDate()))) {
      date2.setDay(30);
    }
    return DayCountUtils.days360(date1, date2);
  }
}
