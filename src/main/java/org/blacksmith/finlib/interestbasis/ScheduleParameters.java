package org.blacksmith.finlib.interestbasis;

import java.time.LocalDate;
import org.blacksmith.finlib.basic.Frequency;
import org.blacksmith.finlib.calendar.BusinessDayCalendar;
import org.blacksmith.finlib.dayconvention.BusinessDayConvention;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScheduleParameters {
  private boolean isEndOfMonthConvention;
  private Frequency couponFrequency;
  private InterestBasis basis;
  private BusinessDayConvention businessDayConvention;
  private BusinessDayCalendar businessDayCalendar;
  private LocalDate startDate;
  private LocalDate endDate;
  private LocalDate referenceDate;


  public static ScheduleParameters SIMPLE_SCHEDULE_PARAMETERS = builder()
      .isEndOfMonthConvention(true)
      .build();
}
