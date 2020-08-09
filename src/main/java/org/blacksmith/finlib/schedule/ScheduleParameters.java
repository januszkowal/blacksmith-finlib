package org.blacksmith.finlib.schedule;

import java.time.LocalDate;
import org.blacksmith.finlib.basic.calendar.BusinessDayCalendar;
import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.basic.datetime.Frequency;
import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.dayconvention.BusinessDayConvention;
import org.blacksmith.finlib.interestbasis.InterestBasis;
import org.blacksmith.finlib.interestbasis.InterestAlgoritm;

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
  private Rate startInterestRate;
  @Builder.Default
  private Rate interestRateAddMargin = Rate.ZERO;
  @Builder.Default
  private Rate interestRateMulMargin = Rate.ONE;
  private String interestTable;
  private boolean isEndOfMonthConvention;
  private Frequency couponFrequency;
  private Frequency rateResetFrequency;
  private InterestBasis basis;
  private BusinessDayConvention businessDayConvention;
  private BusinessDayCalendar businessDayCalendar;
  @Builder.Default
  private InterestAlgoritm algorithm= InterestAlgoritm.SIMPLE;
  @Builder.Default
  private boolean linkCouponLengthWitPayment=true;
  private LocalDate startDate;
  private LocalDate maturityDate;
  private LocalDate firstCouponDate;
}
