package org.blacksmith.finlib.schedule.events.interest;

import java.time.LocalDate;

import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.schedule.events.Event;
import org.blacksmith.finlib.schedule.events.InterestEventSrc;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RateResetEvent implements InterestEventSrc {
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
  boolean isRateReset=true;

  public boolean setInterestRate(Rate interestRate) {
    boolean result = this.interestRate.equals(interestRate);
    if (!result) {
      this.interestRate = interestRate;
    }
    return result;
  }

  public boolean setPrincipal(Amount principal) {
    boolean result = this.principal.equals(principal);
    if (!result) {
      this.principal = principal;
    }
    return result;
  }

  @Override
  public LocalDate getEventDate() {
    return this.startDate;
  }
}
