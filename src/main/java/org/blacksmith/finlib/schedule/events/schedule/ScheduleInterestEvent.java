package org.blacksmith.finlib.schedule.events.schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.blacksmith.commons.datetime.DateRange;
import org.blacksmith.finlib.schedule.events.Event;
import org.blacksmith.finlib.schedule.events.InterestEvent;
import org.blacksmith.finlib.schedule.events.InterestEventSrc;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class ScheduleInterestEvent implements Event {
  @NonNull
  LocalDate startDate;
  @NonNull
  LocalDate endDate;
  @NonNull
  LocalDate paymentDate;
  @NonNull
  LocalDate paymentDateUnadjusted;
  @Builder.Default
  List<ScheduleRateResetEvent> subEvents = new ArrayList<>();

  @Override
  public LocalDate getEventDate() {
    return this.startDate;
  }
}

