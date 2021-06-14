package org.blacksmith.finlib.cucumber.dto;

import java.time.LocalDate;

import org.blacksmith.finlib.datetime.daycount.ScheduleInfo;

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
