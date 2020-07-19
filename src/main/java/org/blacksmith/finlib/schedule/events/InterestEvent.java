package org.blacksmith.finlib.schedule.events;

import java.time.LocalDate;

public interface InterestEvent extends InterestEventSrc {

  LocalDate getPaymentDate();

  default LocalDate getEventDate() {
    return getPaymentDate();
  }
}
