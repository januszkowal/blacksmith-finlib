package org.blacksmith.finlib.schedule.events;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ScheduleEvent {
  LocalDate startDate;
  LocalDate endDate;
  LocalDate paymentDate;
  LocalDate paymentDateUnadjusted;
  @Builder.Default
  List<SubEvent> subEvents = new ArrayList<>();

  @Value(staticConstructor = "of")
  public static class SubEvent {
    LocalDate startDate;
    LocalDate endDate;
  }
}

