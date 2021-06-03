package org.blacksmith.finlib.calendar.policy;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.blacksmith.finlib.calendar.policy.helper.DatePartInMemoryProvider;
import org.blacksmith.finlib.calendar.policy.helper.WeekDayExtractor;

public class WeekDayPolicy extends DatePartHolidayPolicy<DayOfWeek> {

  public WeekDayPolicy(DayOfWeek... weekendDays) {
    super(WeekDayExtractor.getInstance(), DatePartInMemoryProvider.of(weekendDays));
  }

  public WeekDayPolicy(int... weekendDays) {
    super(WeekDayExtractor.getInstance(),
        DatePartInMemoryProvider.of(Arrays.stream(weekendDays).mapToObj(DayOfWeek::of).collect(Collectors.toSet())));
  }

  public static WeekDayPolicy of(DayOfWeek... weekendDays) {
    return new WeekDayPolicy(weekendDays);
  }

  public static WeekDayPolicy of(int... weekendDays) {
    return new WeekDayPolicy(weekendDays);
  }
}
