package org.blacksmith.finlib.calendar.policy;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.blacksmith.commons.arg.Validate;
import org.blacksmith.finlib.calendar.HolidayPolicy;
import org.blacksmith.finlib.calendar.helper.DateToPartConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Holiday policy containing set of holidays - it's a template for week day, day of month, day of year policy
 * */
public class HolidaySetPolicy<U> implements HolidayPolicy {

  private static final Logger LOGGER = LoggerFactory.getLogger(HolidaySetPolicy.class);
  private final Set<U> holidays = new HashSet<>();
  private final DateToPartConverter<U> converter;  
  
  public HolidaySetPolicy(DateToPartConverter<U> converter) {
    this.converter = converter;
  }

  public HolidaySetPolicy(DateToPartConverter<U> converter,Collection<U> holidays) {
    this.addAll(holidays);
    this.converter = converter;
  }

  @SafeVarargs
  public HolidaySetPolicy(DateToPartConverter<U> converter,U...holidays) {
    this.addAll(holidays);
    this.converter = converter;
  }

  public static <U> HolidaySetPolicy<U> of (DateToPartConverter<U> converter,Collection<U> holidays) {
    return new HolidaySetPolicy<>(converter,holidays);
  }

  @SafeVarargs
  public static <U> HolidaySetPolicy<U> of (DateToPartConverter<U> converter,U...holidays) {
    return new HolidaySetPolicy<>(converter,holidays);
  }

  public void add(U holiday) {
    Validate.notNull(holiday, "Null holiday not allowed");
    this.holidays.add(holiday);
  }

  public void addAll(Collection<U> holidays) {
    Validate.notNull(holidays, "Null holidays list not allowed");
    this.holidays.addAll(holidays); 
  }
  
  @SafeVarargs
  public final void addAll(U...holidays) {
    Validate.notNull(holidays, "Null holidays list not allowed");
    this.holidays.addAll(Arrays.stream(holidays).collect(Collectors.toSet()));
  }

  public void clear() {
    holidays.clear();
  }

  @Override
  public boolean isHoliday(LocalDate date) {
    boolean result = holidays.contains(converter.convert(date));
    LOGGER.debug("Check isHoliday date={}, result={}, class={}",date,result,this);
    return result;
  }
}
