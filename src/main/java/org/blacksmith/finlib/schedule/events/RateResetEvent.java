package org.blacksmith.finlib.schedule.events;

import java.time.LocalDate;

import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.basic.numbers.Rate;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RateResetEvent implements Event {
  @NonNull
  LocalDate startDate;
  @NonNull
  LocalDate endDate;
  @Builder.Default
  Amount principal = Amount.ZERO;
  @Builder.Default
  Rate interestRate = Rate.ZERO;
  @Builder.Default
  Amount interest = Amount.ZERO;

  @Builder.Default
  boolean isRateReset = true;

  @Override
  public LocalDate getEventDate() {
    return this.startDate;
  }

  public RateResetEvent copy() {
    return builder()
        .startDate(startDate)
        .endDate(endDate)
        .principal(principal)
        .interestRate(interestRate)
        .interest(interest)
        .build();
  }
}
