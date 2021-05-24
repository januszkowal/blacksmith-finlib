package org.blacksmith.finlib.cucumber;

import java.time.LocalDate;

import org.blacksmith.finlib.interest.basis.ScheduleInfo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConventionInput {
  LocalDate startDate;
  LocalDate endDate;
  int days;
  double fraction;
  ScheduleInfo scheduleInfo;
}
