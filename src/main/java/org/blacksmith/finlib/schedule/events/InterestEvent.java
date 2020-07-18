package org.blacksmith.finlib.schedule.events;

import java.time.LocalDate;

import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.basic.numbers.Rate;

public interface InterestEvent extends InterestEventSrc {

  LocalDate getPaymentDate();
//  Amount getPrincipal();
//  Rate getInterestRate();

  default LocalDate getEventDate() {
    return getPaymentDate();
  }
}
