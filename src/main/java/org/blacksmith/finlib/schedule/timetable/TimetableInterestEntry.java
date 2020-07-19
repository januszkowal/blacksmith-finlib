package org.blacksmith.finlib.schedule.timetable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.blacksmith.finlib.schedule.events.Event;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class TimetableInterestEntry implements Event {
  @NonNull
  LocalDate startDate;
  @NonNull
  LocalDate endDate;
  @NonNull
  LocalDate paymentDate;
  @NonNull
  LocalDate paymentDateUnadjusted;
  @Builder.Default
  List<TimetableRateResetEntry> subEvents = new ArrayList<>();

  @Override
  public LocalDate getEventDate() {
    return this.paymentDate;
  }

  @Value
  @Builder
  public static class TimetableRateResetEntry implements Event {
    @NonNull
    LocalDate startDate;
    @NonNull
    LocalDate endDate;

    @Override
    public LocalDate getEventDate() {
      return this.startDate;
    }
  }
}

