package org.blacksmith.finlib.calendar.policy;

import java.time.LocalDate;
import org.blacksmith.finlib.calendar.HolidayPolicy;

public abstract class AbstractHolidayPolicy {
  protected HolidayPolicy next;

  public AbstractHolidayPolicy() {
    this.next=null;
  }

  public AbstractHolidayPolicy(HolidayPolicy next) {
    this.next = next;
  }

  public boolean isHoliday(LocalDate date) {
    return (next==null) ?
        currentIsHoliday(date) :
        currentIsHoliday(date) && next.isHoliday(date);
  }

  public abstract boolean currentIsHoliday(LocalDate date);
}
