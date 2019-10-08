package org.blacksmith.finlib.interestbasis;

import java.time.LocalDate;
import org.blacksmith.finlib.basic.Frequency;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScheduleInfo {
  private LocalDate startDate;
  private LocalDate endDate;
  private boolean isEndOfMonthConvention;
  private Frequency couponFrequency;
  
  public LocalDate getCouponStartDate(LocalDate date) {
    return this.startDate;
  }
  
  public LocalDate getCouponEndDate(LocalDate date) {
    return this.startDate;
  }

  public static ScheduleInfo SIMPLE_SCHEDULE_INFO = builder()
      .isEndOfMonthConvention(true)      
      .build();
}
