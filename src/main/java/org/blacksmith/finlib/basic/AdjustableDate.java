package org.blacksmith.finlib.basic;

import java.time.LocalDate;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AdjustableDate {
  private final LocalDate unadjusted;
  private final BusinessDayConvention convention;

  public static AdjustableDate of(LocalDate unadjusted, BusinessDayConvention convention) {
    return new AdjustableDate(unadjusted,convention);
  }
}
