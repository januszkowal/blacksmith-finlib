package org.blacksmith.finlib.datetime.calendar.policy;

import java.time.LocalDate;

import org.blacksmith.finlib.datetime.calendar.HolidayPolicy;
import org.blacksmith.finlib.datetime.calendar.extractor.DatePartExtractor;
import org.blacksmith.finlib.datetime.calendar.provider.DatePartProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Holiday policy containing set of holidays - it's a template for week day, day of month, day of year policy
 */
public class DatePartHolidayPolicy<U> implements HolidayPolicy {

  private static final Logger log = LoggerFactory.getLogger(DatePartHolidayPolicy.class);
  private final DatePartExtractor<U> extractor;
  private final DatePartProvider<U> provider;

  public DatePartHolidayPolicy(DatePartExtractor<U> extractor, DatePartProvider<U> provider) {
    this.extractor = extractor;
    this.provider = provider;
  }

  public static <U> DatePartHolidayPolicy<U> of(DatePartExtractor<U> converter, DatePartProvider<U> provider) {
    return new DatePartHolidayPolicy<>(converter, provider);
  }

  @Override
  public boolean isHoliday(LocalDate date) {
    boolean result = provider.contains(extractor.extract(date));
    log.debug("Check isHoliday date={}, result={}, class={}", date, result, this);
    return result;
  }
}
