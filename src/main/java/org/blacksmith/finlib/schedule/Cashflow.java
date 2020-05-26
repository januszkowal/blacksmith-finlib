package org.blacksmith.finlib.schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder.Default;
import lombok.experimental.SuperBuilder;
import org.blacksmith.finlib.basic.Amount;
import lombok.Builder;
import lombok.Data;
import org.blacksmith.finlib.basic.Rate;

@SuperBuilder
public class Cashflow extends BaseCashflow {

  private LocalDate paymentDate;
  @Builder.Default
  private List<SubCashflow> subCashflows = new ArrayList<>();
}
