package org.blacksmith.finlib.valuation.dto;

import java.util.List;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder
public class Cashflows {
  @Singular
  public List<Cashflow> cashflows;
}
