package org.blacksmith.finlib.schedule;

import java.time.LocalDate;
import lombok.experimental.SuperBuilder;
import org.blacksmith.finlib.basic.Amount;
import org.blacksmith.finlib.basic.Rate;

@SuperBuilder
public class BaseCashflow {
  private LocalDate startDate;  
  private LocalDate endDate;
  private Rate rate;
  private Amount notional;
  private Amount amount;
}
