package org.blacksmith.finlib.interestbasis;

import java.math.BigDecimal;
import java.time.LocalDate;
import blacksmith.finlib.commons.Amount;
import blacksmith.finlib.commons.Frequency;
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
  //private Amount notional;
  
  public BigDecimal getRate(LocalDate date) {
    return BigDecimal.ZERO;
  }

  public BigDecimal getNotional(LocalDate date) {
    return BigDecimal.ZERO;
  }

  public static ScheduleInfo SIMPLE_SCHEDULE_INFO = builder()
      .isEndOfMonthConvention(true)      
      .build();
}
