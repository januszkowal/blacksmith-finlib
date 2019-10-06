package org.blacksmith.finlib.calendar.policy;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.blacksmith.finlib.calendar.policy.helper.StandardDateToPartConverters;

public class WeekDaySetPolicy extends HolidaySetProvider<DayOfWeek> {

  public static final WeekDaySetPolicy SAT_SUN = new WeekDaySetPolicy(DayOfWeek.SATURDAY,DayOfWeek.SUNDAY);

  public static final WeekDaySetPolicy FRI_SAT = new WeekDaySetPolicy(DayOfWeek.FRIDAY,DayOfWeek.SATURDAY);

  public static final WeekDaySetPolicy THU_FRI = new WeekDaySetPolicy(DayOfWeek.THURSDAY,DayOfWeek.FRIDAY);

  public WeekDaySetPolicy(DayOfWeek...weekendDays) {
    super(StandardDateToPartConverters.WEEK_DAY,weekendDays);
  }

  public WeekDaySetPolicy(int...weekendDays) {
    super(StandardDateToPartConverters.WEEK_DAY,Arrays.stream(weekendDays).boxed().map(DayOfWeek::of).collect(Collectors.toSet()));
  }

}
