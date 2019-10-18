package org.blacksmith.finlib.accounting;

import org.blacksmith.finlib.basic.Currency;

public interface AccountingRegister {
  Long getId();
  String getName();
  Currency getCurrency();
  RegisterOwnerType getOwnerType();
  Long getOwnerId();
}
