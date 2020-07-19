package org.blacksmith.finlib.schedule.events;

import java.time.LocalDate;
import java.util.List;

import org.blacksmith.commons.datetime.DateRange;
import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.basic.numbers.Rate;

public interface InterestEventSrc extends Event {
  LocalDate getStartDate();
  LocalDate getEndDate();
  Amount getPrincipal();
  Rate getInterestRate();

  static <E extends InterestEventSrc> List<LocalDate> getStartDates(List<E> interestEvents) {
    return Event.getDates(interestEvents, InterestEventSrc::getStartDate);
  }

  static <E extends InterestEventSrc> List<LocalDate> getEndDates(List<E> interestEvents) {
    return Event.getDates(interestEvents, InterestEventSrc::getEndDate);
  }

  static <E extends InterestEventSrc> E getEventInRange(List<E> interestEvents, LocalDate date) {
    return interestEvents.stream()
        .filter(ie-> DateRange.closedOpen(ie.getStartDate(),ie.getEndDate()).contains(date))
        .findFirst().orElse(null);
  }
}
