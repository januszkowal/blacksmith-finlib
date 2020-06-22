package org.blacksmith.finlib.cucumber;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import org.blacksmith.finlib.interestbasis.ScheduleInfo;

@Getter
@Builder
public class ConventionInput {
  LocalDate startDate;
  LocalDate endDate;
  int days;
  double fraction;
  ScheduleInfo scheduleInfo;
}
