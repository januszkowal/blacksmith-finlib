package org.blacksmith.finlib.calendar.policy;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.blacksmith.finlib.basic.calendar.policy.DatePartHolidayPolicy;
import org.blacksmith.finlib.basic.calendar.policy.helper.DatePartInMemoryProvider;
import org.blacksmith.finlib.basic.calendar.policy.helper.StandardDatePartExtractors;

public class WeekDayPolicy extends DatePartHolidayPolicy<DayOfWeek> {

  public WeekDayPolicy(DayOfWeek... weekendDays) {
    super(StandardDatePartExtractors.WEEK_DAY, DatePartInMemoryProvider.of(weekendDays));
  }

  public WeekDayPolicy(int... weekendDays) {
    super(StandardDatePartExtractors.WEEK_DAY,
        DatePartInMemoryProvider.of(Arrays.stream(weekendDays).boxed().map(DayOfWeek::of).collect(Collectors.toSet())));
  }

  public static WeekDayPolicy of(DayOfWeek... weekendDays) {
    return new WeekDayPolicy(weekendDays);
  }

  public static WeekDayPolicy of(int... weekendDays) {
    return new WeekDayPolicy(weekendDays);
  }
}
