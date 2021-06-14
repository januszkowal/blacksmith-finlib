package org.blacksmith.finlib.datetime.daycount.impl;

import java.time.LocalDate;

import org.blacksmith.finlib.datetime.dayconvention.utils.DayCountUtils;
import org.blacksmith.finlib.datetime.dayconvention.utils.YmdDate;
import org.blacksmith.finlib.datetime.daycount.ScheduleInfo;
import org.blacksmith.finlib.datetime.daycount.impl.AbstractConstantDenominatorConvention;

public class D30IsdaConvention extends AbstractConstantDenominatorConvention {

  public D30IsdaConvention(double denominator) {
    super(denominator);
  }

  @Override
  public long calculateDays(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo) {
    YmdDate date1 = YmdDate.of(firstDate);
    YmdDate date2 = YmdDate.of(secondDate);
    if (date1.getDay() == 31)
      date1.setDay(30);
    if (date2.getDay() == 31 && date1.getDay() == 30) {
      date2.setDay(30);
    }
    return DayCountUtils.days360(date1, date2);
  }
}
