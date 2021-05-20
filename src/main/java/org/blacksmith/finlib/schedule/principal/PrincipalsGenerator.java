package org.blacksmith.finlib.schedule.principal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.schedule.events.PrincipalEvent;

public class PrincipalsGenerator {
  private final boolean updateFromFirst;
  private final boolean addInitial;

  public PrincipalsGenerator(boolean addInitial, boolean updateFromFirst) {
    this.addInitial = addInitial;
    this.updateFromFirst = updateFromFirst;
  }

  public List<PrincipalEvent> generate(Amount startPrincipal,
      List<LocalDate> dates, PrincipalUpdatePolicy principalUpdatePolicy) {
    ArgChecker.notNull(startPrincipal, "Start principal must be not null");
    ArgChecker.notEmpty(dates, "Dates list must be not empty");
    List<LocalDate> eventDates = dates.stream().sorted().collect(Collectors.toList());
    List<PrincipalEvent> events = new ArrayList<>();
    int startIndex;
    if (updateFromFirst) {
      startIndex = 0;
    } else {
      startIndex = 1;
      if (addInitial) {
        events.add(PrincipalEvent.of(eventDates.get(0), startPrincipal));
      }
    }
    Amount priorPrincipal = startPrincipal;
    for (int i = startIndex; i < eventDates.size(); i++) {
      Amount principal = principalUpdatePolicy.apply(priorPrincipal);
      events.add(PrincipalEvent.of(eventDates.get(i), principal));
      priorPrincipal = principal;
    }
    return events;
  }
}
