package org.blacksmith.finlib.schedule.events;

import java.time.LocalDate;

import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.schedule.events.Event;

import lombok.Value;

@Value(staticConstructor = "of")
public class PrincipalEvent implements Event {
  LocalDate date;
  Amount principal;

  @Override
  public LocalDate getEventDate() {
    return this.date;
  }
}
