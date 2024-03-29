package org.blacksmith.finlib.interest.schedule.events;

import java.time.LocalDate;

import org.blacksmith.finlib.basic.numbers.Amount;

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
