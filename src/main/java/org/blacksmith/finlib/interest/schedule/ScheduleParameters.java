package org.blacksmith.finlib.interest.schedule;

import java.time.LocalDate;

import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.basic.datetime.Frequency;
import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.datetime.calendar.BusinessDayCalendar;
import org.blacksmith.finlib.datetime.dayconvention.BusinessDayConvention;
import org.blacksmith.finlib.interest.InterestAlgorithm;
import org.blacksmith.finlib.datetime.daycount.DayCount;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScheduleParameters {
  private Currency currency;
  @Builder.Default
  private Amount principal = Amount.ZERO;
  @Builder.Default
  private Amount endPrincipal = Amount.ZERO;
  @Builder.Default
  private InterestRateIndexation indexation = InterestRateIndexation.FIXED;
  private Rate fixedRate;
  @Builder.Default
  private Rate interestRateAddMargin = Rate.ZERO;
  @Builder.Default
  private Rate interestRateMulMargin = Rate.ONE;
  private String interestTable;
  private boolean isEndOfMonthConvention;
  private Frequency couponFrequency;
  private Frequency rateResetFrequency;
  private DayCount basis;
  private BusinessDayConvention businessDayConvention;
  private BusinessDayCalendar businessDayCalendar;
  @Builder.Default
  private InterestAlgorithm algorithm = InterestAlgorithm.SIMPLE;
  @Builder.Default
  private boolean linkCouponLengthWitPayment = true;
  private LocalDate startDate;
  private LocalDate maturityDate;
  private LocalDate firstCouponDate;
}
