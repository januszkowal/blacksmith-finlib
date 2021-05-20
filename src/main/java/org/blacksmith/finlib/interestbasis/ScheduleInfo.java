package org.blacksmith.finlib.interestbasis;

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

  private LocalDate startDate;
  private LocalDate endDate;
  private LocalDate couponStartDate;
  private LocalDate couponEndDate;
  private boolean isEndOfMonthConvention;
  private Frequency couponFrequency;
}
