package org.blacksmith.finlib.calendar.policy;

import java.time.MonthDay;
import java.util.Collection;

import org.blacksmith.finlib.calendar.extractor.MonthDayExtractor;
import org.blacksmith.finlib.calendar.provider.DatePartInMemoryProvider;
import org.blacksmith.finlib.calendar.provider.DatePartProvider;

public class MonthDayHolidayPolicy extends DatePartHolidayPolicy<MonthDay> {
  public MonthDayHolidayPolicy(DatePartProvider<MonthDay> monthDaysProvider) {
    super(MonthDayExtractor.getInstance(), monthDaysProvider);
  }

  public static MonthDayHolidayPolicy of(DatePartProvider<MonthDay> monthDaysProvider) {
    return new MonthDayHolidayPolicy(monthDaysProvider);
  }

  public static MonthDayHolidayPolicy ofMonthDays(Collection<MonthDay> monthDays) {
    return new MonthDayHolidayPolicy(new DatePartInMemoryProvider<>(monthDays));
  }
}
