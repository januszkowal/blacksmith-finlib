package org.blacksmith.finlib.interest.basis;

import java.time.LocalDate;

import org.blacksmith.finlib.basic.datetime.Frequency;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ScheduleInfo {
  public static ScheduleInfo SIMPLE_SCHEDULE_INFO = builder()
      .isEndOfMonthConvention(true)
      .build();

  LocalDate startDate;
  LocalDate endDate;
  LocalDate couponStartDate;
  LocalDate couponEndDate;
  boolean isEndOfMonthConvention;
  Frequency couponFrequency;
}
