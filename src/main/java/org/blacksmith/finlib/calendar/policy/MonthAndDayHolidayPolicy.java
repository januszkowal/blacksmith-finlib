package org.blacksmith.finlib.calendar.policy;

import java.time.MonthDay;

import org.blacksmith.finlib.basic.calendar.policy.DatePartHolidayPolicy;
import org.blacksmith.finlib.basic.calendar.policy.helper.DatePartProvider;
import org.blacksmith.finlib.basic.calendar.policy.helper.StandardDatePartExtractors;

public class MonthAndDayHolidayPolicy extends DatePartHolidayPolicy<MonthDay> {
  public MonthAndDayHolidayPolicy(DatePartProvider<MonthDay> monthDaysProvider) {
    super(StandardDatePartExtractors.MONTH_DAY, monthDaysProvider);
  }

  public static MonthAndDayHolidayPolicy of(DatePartProvider<MonthDay> monthDaysProvider) {
    return new MonthAndDayHolidayPolicy(monthDaysProvider);
  }
}
