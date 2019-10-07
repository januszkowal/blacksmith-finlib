package org.blacksmith.finlib.schedule;

import java.time.LocalDate;


import lombok.Data;

@Data
public class InterestPayment {
  private LocalDate paymentDate;
  private LocalDate startDate;
  private LocalDate endDate;
}
