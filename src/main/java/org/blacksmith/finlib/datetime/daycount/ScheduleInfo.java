package org.blacksmith.finlib.datetime.daycount;

import java.time.LocalDate;

import org.blacksmith.finlib.basic.datetime.Frequency;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ScheduleInfo {
  LocalDate startDate;
  LocalDate endDate;
  LocalDate periodStartDate;
  LocalDate periodEndDate;
  boolean isEndOfMonthConvention;
  Frequency couponFrequency;

  public static ScheduleInfo fromDateRange(LocalDate firstDate, LocalDate secondDate) {
    return ScheduleInfo.builder()
        .startDate(firstDate)
        .endDate(secondDate)
        .periodStartDate(firstDate)
        .periodEndDate(secondDate)
        .isEndOfMonthConvention(true)
        .build();
  }
}
