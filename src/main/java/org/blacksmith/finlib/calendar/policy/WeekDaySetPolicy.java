package org.blacksmith.finlib.calendar.policy;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.blacksmith.commons.arg.Validate;

public class WeekDaySetPolicy extends AbstractHolidaySetPolicy<DayOfWeek> {

  public static final WeekDaySetPolicy SAT_SUN = new WeekDaySetPolicy(new DayOfWeek[]{
    DayOfWeek.SATURDAY,DayOfWeek.SUNDAY});

  public static final WeekDaySetPolicy FRI_SAT = new WeekDaySetPolicy(new DayOfWeek[]{
      DayOfWeek.FRIDAY,DayOfWeek.SATURDAY});

  public static final WeekDaySetPolicy THU_FRI = new WeekDaySetPolicy(new DayOfWeek[]{
      DayOfWeek.THURSDAY,DayOfWeek.FRIDAY});

  public WeekDaySetPolicy(DayOfWeek[] weekendDays) {
    Validate.checkNotNull(weekendDays, "Null week days not allowed");
    this.addAll(Arrays.stream(weekendDays).collect(Collectors.toSet()));
  }

  public WeekDaySetPolicy(int[] weekendDays) {
    Validate.checkNotNull(weekendDays, "Null week days not allowed");
    this.addAll(Arrays.stream(weekendDays).boxed().map(d->DayOfWeek.of(d)).collect(Collectors.toSet()));
  }

  public DayOfWeek convertDate(LocalDate date) {
    return date.getDayOfWeek();
  }
}
