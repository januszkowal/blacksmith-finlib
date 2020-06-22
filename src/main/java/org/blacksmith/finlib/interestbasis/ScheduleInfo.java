package org.blacksmith.finlib.interestbasis;

import java.time.LocalDate;
import lombok.Value;
import org.blacksmith.finlib.basic.Amount;
import org.blacksmith.finlib.basic.Rate;
import org.blacksmith.finlib.datetime.Frequency;
import lombok.Builder;

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

  public Amount getNotional(LocalDate date) {
    return Amount.ZERO;
  }

  public Rate getRate(LocalDate date) {
    return Rate.ZERO;
  }
}
