package org.blacksmith.finlib.calendar.policy;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.blacksmith.commons.arg.Validate;
import org.blacksmith.finlib.calendar.HolidayProvider;

public class WeekDayHolidayProvider implements HolidayProvider<DayOfWeek> {

  public static final WeekDayHolidayProvider SAT_SUN = new WeekDayHolidayProvider(new DayOfWeek[]{
    DayOfWeek.SATURDAY,DayOfWeek.SUNDAY});

  public static final WeekDayHolidayProvider FRI_SAT = new WeekDayHolidayProvider(new DayOfWeek[]{
      DayOfWeek.FRIDAY,DayOfWeek.SATURDAY});

  public static final WeekDayHolidayProvider THU_FRI = new WeekDayHolidayProvider(new DayOfWeek[]{
      DayOfWeek.THURSDAY,DayOfWeek.FRIDAY});

  private final Set<DayOfWeek> weekendDays = new HashSet<>();

  public WeekDayHolidayProvider(DayOfWeek[] days) {
    Validate.checkNotNull(weekendDays, "Null week days not allowed");
    this.weekendDays.addAll(Arrays.stream(days).collect(Collectors.toSet()));
  }

  public WeekDayHolidayProvider(int[] days) {
    Validate.checkNotNull(weekendDays, "Null week days not allowed");
    this.weekendDays.addAll(Arrays.stream(days).boxed().map(d->DayOfWeek.of(d)).collect(Collectors.toSet()));
  }

  @Override
  public boolean contains(LocalDate date) {
    return weekendDays.contains(convertDate(date));
  }

  private DayOfWeek convertDate(LocalDate date) {
    return date.getDayOfWeek();
  }
}
