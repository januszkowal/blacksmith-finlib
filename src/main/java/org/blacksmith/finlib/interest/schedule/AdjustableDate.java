package org.blacksmith.finlib.interest.schedule;

import java.time.LocalDate;

import org.blacksmith.finlib.dayconvention.BusinessDayConvention;

public class AdjustableDate {

  private final LocalDate unadjusted;
  private final BusinessDayConvention convention;

  public AdjustableDate(LocalDate unadjusted, BusinessDayConvention convention) {
    this.unadjusted = unadjusted;
    this.convention = convention;
  }

  public static AdjustableDate of(LocalDate unadjusted, BusinessDayConvention convention) {
    return new AdjustableDate(unadjusted, convention);
  }

  public LocalDate getUnadjusted() {
    return this.unadjusted;
  }

  public BusinessDayConvention getConvention() {
    return this.convention;
  }
}
