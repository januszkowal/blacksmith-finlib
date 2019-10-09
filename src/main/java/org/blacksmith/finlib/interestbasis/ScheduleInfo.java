package org.blacksmith.finlib.interestbasis;

import java.time.LocalDate;
import org.blacksmith.finlib.basic.Amount;
import org.blacksmith.finlib.basic.Frequency;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScheduleInfo {
  private LocalDate startDate;
  private LocalDate maturityDate;
  private LocalDate couponStartDate;
  private LocalDate couponEndDate;
  private boolean isEndOfMonthConvention;
  private Frequency couponFrequency;
  private Amount notional;


  public static ScheduleInfo SIMPLE_SCHEDULE_INFO = builder()
      .isEndOfMonthConvention(true)      
      .build();
}
