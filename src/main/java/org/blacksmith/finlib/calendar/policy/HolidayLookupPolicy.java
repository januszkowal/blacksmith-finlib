package org.blacksmith.finlib.calendar.policy;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.blacksmith.finlib.calendar.HolidayPolicy;
import org.blacksmith.finlib.calendar.helper.DateToPartConverter;
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

  @Override
  public boolean isHoliday(LocalDate date) {
    boolean result = provider.contains(converter.convert(date));
    LOGGER.debug("Check isHoliday date={}, result={}, class={}",date,result,this);
    return result;
  }
}
