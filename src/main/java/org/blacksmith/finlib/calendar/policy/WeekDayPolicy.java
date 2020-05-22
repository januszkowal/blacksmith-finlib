package org.blacksmith.finlib.calendar.policy;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.blacksmith.finlib.calendar.helper.StandardDateToPartConverters;

public class WeekDayPolicy extends HolidayLookupPolicy<DayOfWeek> {

  public static final WeekDayPolicy SAT_SUN = WeekDayPolicy.of(DayOfWeek.SATURDAY,DayOfWeek.SUNDAY);

  public static final WeekDayPolicy FRI_SAT = WeekDayPolicy.of(DayOfWeek.FRIDAY,DayOfWeek.SATURDAY);

  public static final WeekDayPolicy THU_FRI = WeekDayPolicy.of(DayOfWeek.THURSDAY,DayOfWeek.FRIDAY);

  public WeekDayPolicy(DayOfWeek...weekendDays) {
    super(StandardDateToPartConverters.WEEK_DAY, HolidayLookupContainer.of(weekendDays));
  }

  public WeekDayPolicy(int...weekendDays) {
    super(StandardDateToPartConverters.WEEK_DAY,
        HolidayLookupContainer.of(Arrays.stream(weekendDays).boxed().map(DayOfWeek::of).collect(Collectors.toSet())));
  }

  public static WeekDayPolicy of(DayOfWeek...weekendDays) {
    return new WeekDayPolicy(weekendDays);
  }

  public static WeekDayPolicy of (int...weekendDays) {
    return new WeekDayPolicy(weekendDays);
  }
}
