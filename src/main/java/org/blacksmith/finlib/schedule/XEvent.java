package org.blacksmith.finlib.schedule;

import java.time.LocalDate;
import lombok.Data;
import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.basic.numbers.Rate;

@Data
public class XEvent {
  private LocalDate startDate;
  private LocalDate endDate;
  private LocalDate paymentDate;
  private Amount amount;
  //
  private Amount principal;
  private Rate rate;
  private Amount principalPayment;
  private Amount interestPayment;
}
