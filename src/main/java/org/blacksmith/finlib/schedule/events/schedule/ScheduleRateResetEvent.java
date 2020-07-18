package org.blacksmith.finlib.schedule.events.schedule;

import java.time.LocalDate;

import org.blacksmith.finlib.schedule.events.Event;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ScheduleRateResetEvent implements Event {
  @NonNull
  LocalDate startDate;
  @NonNull
  LocalDate endDate;
  boolean isRateReset;

  @Override
  public LocalDate getEventDate() {
    return this.startDate;
  }
}
