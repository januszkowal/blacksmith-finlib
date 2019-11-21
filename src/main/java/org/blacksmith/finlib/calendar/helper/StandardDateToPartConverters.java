package org.blacksmith.finlib.calendar.helper;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;

public class StandardDateToPartConverters {
  private StandardDateToPartConverters() {}
  public static final DateToPartConverter<DayOfWeek>  WEEK_DAY  = d -> d.getDayOfWeek();
  public static final DateToPartConverter<MonthDay>   MONTH_DAY = d -> MonthDay.of(d.getMonthValue(),d.getDayOfMonth());
  public static final DateToPartConverter<LocalDate>  YEAR_DAY  = d -> d;
}
