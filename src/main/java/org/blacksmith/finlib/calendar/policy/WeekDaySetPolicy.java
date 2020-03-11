package org.blacksmith.finlib.calendar.policy;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.blacksmith.finlib.calendar.helper.StandardDateToPartConverters;

public class WeekDaySetPolicy extends HolidaySetPolicy<DayOfWeek> {

  public static final WeekDaySetPolicy SAT_SUN = WeekDaySetPolicy.of(DayOfWeek.SATURDAY,DayOfWeek.SUNDAY);

  public static final WeekDaySetPolicy FRI_SAT = WeekDaySetPolicy.of(DayOfWeek.FRIDAY,DayOfWeek.SATURDAY);

  public static final WeekDaySetPolicy THU_FRI = WeekDaySetPolicy.of(DayOfWeek.THURSDAY,DayOfWeek.FRIDAY);

  public WeekDaySetPolicy(DayOfWeek...weekendDays) {
    super(StandardDateToPartConverters.WEEK_DAY,weekendDays);
  }

  public WeekDaySetPolicy(int...weekendDays) {
    super(StandardDateToPartConverters.WEEK_DAY,Arrays.stream(weekendDays).boxed().map(DayOfWeek::of).collect(Collectors.toSet()));
  }

  public static WeekDaySetPolicy of(DayOfWeek...weekendDays) {
    return new WeekDaySetPolicy(weekendDays);
  }

  public static WeekDaySetPolicy of (int...weekendDays) {
    return new WeekDaySetPolicy(weekendDays);
  }
}
