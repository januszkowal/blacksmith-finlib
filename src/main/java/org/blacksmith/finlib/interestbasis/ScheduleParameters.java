package org.blacksmith.finlib.interestbasis;

import java.time.LocalDate;
import org.blacksmith.finlib.basic.calendar.BusinessDayCalendar;
import org.blacksmith.finlib.basic.datetime.Frequency;
import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.dayconvention.BusinessDayConvention;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScheduleParameters {
  private Amount principal;
  private Amount endPrincipal;
  private Rate startInterestRate;
  private boolean isEndOfMonthConvention;
  private Frequency couponFrequency;
  private Frequency rateResetFrequency;
  private DayCountConvention basis;
  private BusinessDayConvention businessDayConvention;
  private BusinessDayCalendar businessDayCalendar;
  @Builder.Default
  private InterestAlghoritm algorithm=InterestAlghoritm.NORMAL;
  @Builder.Default
  private boolean linkCouponLengthWitPayment=true;
  private LocalDate startDate;
  private LocalDate maturityDate;
  private LocalDate firstCouponDate;
}
