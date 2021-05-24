package org.blacksmith.finlib.interest.schedule.principal;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.interest.schedule.events.PrincipalEvent;

import static java.util.stream.Collectors.collectingAndThen;

public class PrincipalsHolder {
  private final List<PrincipalEvent> events;
  private final Amount startPrincipal;

  public PrincipalsHolder(Amount startPrincipal, List<PrincipalEvent> events) {
    ArgChecker.notNull(startPrincipal);
    ArgChecker.isTrue(startPrincipal.isPositive(), "Start principal must be greater than 0");
    ArgChecker.notNull(events, "Events must be not null");
    this.startPrincipal = startPrincipal;
    this.events = events.stream()
        .sorted()
        .collect(collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
  }

  public PrincipalsHolder(Amount startPrincipal) {
    this(startPrincipal, Collections.emptyList());
  }

  public Amount getPrincipal(LocalDate date) {
    return events.isEmpty() ? startPrincipal : events.stream()
        .filter(e -> e.getDate().compareTo(date) <= 0)
        .sorted(Comparator.reverseOrder())
        .map(PrincipalEvent::getPrincipal)
        .findFirst()
        .orElse(startPrincipal);
  }

  public boolean contains(LocalDate date) {
    return events.stream()
        .anyMatch(e -> e.getDate().equals(date));
  }

  public List<PrincipalEvent> getEvents() {
    return this.events;
  }

  public boolean isEmpty() {
    return this.events.isEmpty();
  }
}
