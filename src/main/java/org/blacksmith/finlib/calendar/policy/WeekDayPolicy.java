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

  public static org.blacksmith.finlib.basic.calendar.policy.WeekDayPolicy of(DayOfWeek... weekendDays) {
    return new org.blacksmith.finlib.basic.calendar.policy.WeekDayPolicy(weekendDays);
  }

  public static org.blacksmith.finlib.basic.calendar.policy.WeekDayPolicy of(int... weekendDays) {
    return new org.blacksmith.finlib.basic.calendar.policy.WeekDayPolicy(weekendDays);
  }
}
