package org.blacksmith.finlib.calendar.policy;

import java.time.MonthDay;

import org.blacksmith.finlib.calendar.policy.helper.DatePartProvider;
import org.blacksmith.finlib.calendar.policy.helper.MonthDayExtractor;

public class MonthAndDayHolidayPolicy extends DatePartHolidayPolicy<MonthDay> {
  public MonthAndDayHolidayPolicy(DatePartProvider<MonthDay> monthDaysProvider) {
    super(MonthDayExtractor.getInstance(), monthDaysProvider);
  }

  public static MonthAndDayHolidayPolicy of(DatePartProvider<MonthDay> monthDaysProvider) {
    return new MonthAndDayHolidayPolicy(monthDaysProvider);
  }
}
