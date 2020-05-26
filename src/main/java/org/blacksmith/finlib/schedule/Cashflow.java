package org.blacksmith.finlib.schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.SuperBuilder;
import lombok.Builder;

@SuperBuilder
public class Cashflow extends BaseCashflow {

  private LocalDate paymentDate;
  @Builder.Default
  private List<SubCashflow> subCashflows = new ArrayList<>();
}
