package org.blacksmith.finlib.schedule.events.schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.basic.numbers.Amount;

public class PrincipalsGenerator {
  private final boolean updateFromFirst;
  private final boolean addInitial;

  public PrincipalsGenerator(boolean addInitial, boolean updateFromFirst) {
    this.addInitial = addInitial;
    this.updateFromFirst = updateFromFirst;
  }
  public List<SchedulePrincipalEvent> generate(Amount startPrincipal,
      List<LocalDate> dates,PrincipalUpdatePolicy principalUpdatePolicy) {
    ArgChecker.notNull(startPrincipal,"Start principal must be not null");
    ArgChecker.notEmpty(dates, "Dates list must be not empty");
    List<LocalDate> eventDates = dates.stream().sorted().collect(Collectors.toList());
    List<SchedulePrincipalEvent> events = new ArrayList<>();
    int startIndex;
    if (updateFromFirst) {
      startIndex = 0;
    } else {
      startIndex = 1;
      if (addInitial) {
        events.add(SchedulePrincipalEvent.of(events.get(0).getDate(), startPrincipal));
      }
    }
    Amount priorPrincipal = startPrincipal;
    for (int i = startIndex; i < eventDates.size(); i++) {
      Amount principal = principalUpdatePolicy.apply(priorPrincipal);
      events.add(SchedulePrincipalEvent.of(eventDates.get(i), principal));
      priorPrincipal = principal;
    }
    return events;
  }
}
