package org.blacksmith.finlib.calendar.policy;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.blacksmith.commons.arg.Validate;

public class WeekDayPolicySet extends AbstractHolidayPolicySet<DayOfWeek> {

  public static final WeekDayPolicySet SAT_SUN = new WeekDayPolicySet(new DayOfWeek[]{
    DayOfWeek.SATURDAY,DayOfWeek.SUNDAY});

  public static final WeekDayPolicySet FRI_SAT = new WeekDayPolicySet(new DayOfWeek[]{
      DayOfWeek.FRIDAY,DayOfWeek.SATURDAY});

  public static final WeekDayPolicySet THU_FRI = new WeekDayPolicySet(new DayOfWeek[]{
      DayOfWeek.THURSDAY,DayOfWeek.FRIDAY});

  public WeekDayPolicySet(DayOfWeek[] weekendDays) {
    Validate.checkNotNull(weekendDays, "Null week days not allowed");
    this.addAll(Arrays.stream(weekendDays).collect(Collectors.toSet()));
  }

  public WeekDayPolicySet(int[] weekendDays) {
    Validate.checkNotNull(weekendDays, "Null week days not allowed");
    this.addAll(Arrays.stream(weekendDays).boxed().map(d->DayOfWeek.of(d)).collect(Collectors.toSet()));
  }

  public DayOfWeek convertDate(LocalDate date) {
    return date.getDayOfWeek();
  }
}
