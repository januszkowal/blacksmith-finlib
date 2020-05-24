package org.blacksmith.finlib.calendar.helper;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;

public class StandardDateToPartConverters {
  private StandardDateToPartConverters() {}
  public static final DateToPartConverter<DayOfWeek>  WEEK_DAY  = LocalDate::getDayOfWeek;
  public static final DateToPartConverter<MonthDay>   MONTH_DAY = MonthDay::from;
  public static final DateToPartConverter<LocalDate>  DAY  = d -> d;
}
