package org.blacksmith.finlib.interestbasis;

import java.time.LocalDate;
import org.blacksmith.finlib.basic.Rate;
import org.blacksmith.finlib.calendar.BusinessDayCalendar;
import org.blacksmith.finlib.datetime.Frequency;
import org.blacksmith.finlib.dayconvention.BusinessDayConvention;
import org.blacksmith.finlib.basic.Amount;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScheduleParameters {
  private Amount notional;
  private Rate startInterestRate;
  private boolean isEndOfMonthConvention;
  private Frequency couponFrequency;
  private Frequency rateResetFrequency;
  private DayCountConvention basis;
  private BusinessDayConvention businessDayConvention;
  private BusinessDayCalendar businessDayCalendar;
  @Builder.Default
  private boolean linkCouponLengthWitPayment=true;
  private LocalDate startDate;
  private LocalDate maturityDate;
  private LocalDate firstCouponDate;
}
