package org.blacksmith.finlib.interest.schedule.events;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface Event extends Comparable<Event> {
  static <E extends Event> List<LocalDate> getDates(List<E> events, Function<E, LocalDate> dateExtractor) {
    return events.stream()
        .map(dateExtractor::apply)
        .collect(Collectors.toList());
  }

  static <E extends Event> List<LocalDate> getEventDates(List<E> events) {
    return getDates(events, Event::getEventDate);
  }

  static String eventsToString(List<? extends Event> cashflows) {
    return cashflows.stream()
        .map(Event::toString)
        .collect(Collectors.joining(System.lineSeparator(), System.lineSeparator(), ""));
  }

  /* Execution date & sorting*/
  LocalDate getEventDate();

  default int compareTo(Event e) {
    return this.getEventDate().compareTo(e.getEventDate());
  }
}
