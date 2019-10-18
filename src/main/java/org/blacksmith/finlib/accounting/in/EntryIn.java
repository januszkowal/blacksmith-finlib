package org.blacksmith.finlib.accounting.in;

import java.math.BigDecimal;

import org.blacksmith.finlib.accounting.Register;
import org.blacksmith.finlib.accounting.Operation;


import lombok.Data;

@Data
public class EntryIn {
  private final BigDecimal cr;
  private final BigDecimal dt;
  private final Operation operation;
  private final Register register;
}
