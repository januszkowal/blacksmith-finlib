package org.blacksmith.finlib.calendar.policy;

import java.time.LocalDate;
import java.util.Optional;
import org.blacksmith.finlib.calendar.HolidayPolicy;

public abstract class AbstractHolidayPolicy {
  protected HolidayPolicy next;

  public AbstractHolidayPolicy() {
  }

  public AbstractHolidayPolicy(HolidayPolicy next) {
    this.next = next;
  }

  public void setNext(HolidayPolicy next) {
    this.next = next;
  }

  public boolean isHoliday(LocalDate date) {
    if (currentIsHoliday(date))
      return true;
    else
      return nextIsHoliday(date);
  }

  public boolean nextIsHoliday(LocalDate date) {
    return Optional.ofNullable(next)
        .map(n->n.isHoliday(date))
        .orElse(false);
  }

  public abstract boolean currentIsHoliday(LocalDate date);
}
