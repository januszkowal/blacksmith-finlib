package org.blacksmith.finlib.calendar.policy;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.blacksmith.commons.arg.Validate;
import org.blacksmith.finlib.calendar.policy.helper.DateToPartConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HolidaySetProvider<U> implements HolidayProvider {

  private static final Logger LOGGER = LoggerFactory.getLogger(HolidaySetProvider.class);
  private final Set<U> holidays = new HashSet<>();
  private final DateToPartConverter<U> converter;  
  
  public HolidaySetProvider(DateToPartConverter<U> converter) {
    this.converter = converter;
  }

  public HolidaySetProvider(DateToPartConverter<U> converter,Collection<U> holidays) {
    this.addAll(holidays);
    this.converter = converter;
  }

  @SafeVarargs
  public HolidaySetProvider(DateToPartConverter<U> converter,U...holidays) {
    this.addAll(holidays);
    this.converter = converter;
  }

  public static <U> HolidaySetProvider<U> of (DateToPartConverter<U> converter,Collection<U> holidays) {
    return new HolidaySetProvider<>(converter,holidays);
  }

  @SafeVarargs
  public static <U> HolidaySetProvider<U> of (DateToPartConverter<U> converter,U...holidays) {
    return new HolidaySetProvider<>(converter,holidays);
  }

  public void add(U d) {
    this.holidays.add(d);
  }

  public void addAll(Collection<U> holidays) {
    Validate.checkNotNull(holidays, "Null holidays list not allowed");
    this.holidays.addAll(holidays); 
  }
  
  @SafeVarargs
  public final void addAll(U...holidays) {
    Validate.checkNotNull(holidays, "Null holidays list not allowed");
    this.holidays.addAll(Arrays.stream(holidays).collect(Collectors.toSet()));
  }

  public void clear() {
    holidays.clear();
  }

  @Override
  public boolean isHoliday(LocalDate date) {
    boolean result = holidays.contains(converter.convert(date));
    LOGGER.info("Check date={}, result={}, class={}",date,result,this);
    return result;
  }
}
