package org.blacksmith.finlib.calendar.policy;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import org.blacksmith.finlib.calendar.HolidayPolicy;
import org.blacksmith.finlib.calendar.helper.DateToPartConverter;
import org.blacksmith.finlib.calendar.helper.StandardDateToPartConverters;
import org.blacksmith.finlib.calendar.policy.lookup.HolidayLookupProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Holiday policy containing set of holidays - it's a template for week day, day of month, day of year policy
 * */
public class HolidayLookupPolicy<U> implements HolidayPolicy {

  private static final Logger LOGGER = LoggerFactory.getLogger(HolidayLookupPolicy.class);
  private final DateToPartConverter<U> converter;
  private final HolidayLookupProvider<U> provider;

  public HolidayLookupPolicy(DateToPartConverter<U> converter, HolidayLookupProvider<U> provider) {
    this.converter = converter;
    this.provider = provider;
  }

  public static <U> HolidayLookupPolicy<U> of (DateToPartConverter<U> converter, HolidayLookupProvider<U> provider) {
    return new HolidayLookupPolicy<>(converter,provider);
  }

  public static HolidayLookupPolicy<LocalDate> ofDay(HolidayLookupProvider<LocalDate> provider) {
    return new HolidayLookupPolicy<>(StandardDateToPartConverters.DAY, provider);
  }

  public static HolidayLookupPolicy<MonthDay> ofMonthDay(HolidayLookupProvider<MonthDay> provider) {
    return new HolidayLookupPolicy<>(StandardDateToPartConverters.MONTH_DAY, provider);
  }

  public static HolidayLookupPolicy<DayOfWeek> ofWeekDay(HolidayLookupProvider<DayOfWeek> provider) {
    return new HolidayLookupPolicy<>(StandardDateToPartConverters.WEEK_DAY, provider);
  }

  @Override
  public boolean isHoliday(LocalDate date) {
    boolean result = provider.contains(converter.convert(date));
    LOGGER.debug("Check isHoliday date={}, result={}, class={}",date,result,this);
    return result;
  }
}
