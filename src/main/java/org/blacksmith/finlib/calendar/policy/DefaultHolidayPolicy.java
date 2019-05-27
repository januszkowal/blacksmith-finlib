package org.blacksmith.finlib.calendar.policy;

import java.time.LocalDate;
import org.blacksmith.commons.arg.Validate;
import org.blacksmith.finlib.calendar.HolidayPolicy;
import org.blacksmith.finlib.calendar.HolidayProvider;


public class DefaultHolidayPolicy implements HolidayPolicy {
  private final HolidayProvider<?> holidaysProvider;

  public DefaultHolidayPolicy(HolidayProvider<?> holidaysProvider) {
    Validate.checkNotNull(holidaysProvider, "Null holiday provider not allowed");
    this.holidaysProvider = holidaysProvider;
  }

  @Override
  public boolean isHoliday(LocalDate date) {
    return holidaysProvider.contains(date);
  }
}
