package org.blacksmith.finlib.schedule;

import java.time.LocalDate;
import blacksmith.finlib.commons.Amount;
import lombok.Builder;
import lombok.Data;

@Data(staticConstructor="of")
@Builder
public class Cashflow {
  private LocalDate startDate;  
  private LocalDate endDate;
  private LocalDate paymentDate;
  private Amount notional;
  private Amount amount;
}
