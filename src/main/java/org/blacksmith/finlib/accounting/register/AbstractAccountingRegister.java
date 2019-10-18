package org.blacksmith.finlib.accounting.register;

import org.blacksmith.finlib.accounting.AccountingRegister;
import org.blacksmith.finlib.basic.Currency;

public abstract class AbstractAccountingRegister implements AccountingRegister {

  private Long id;
  private Long ownerId;
  private Currency currency;

  public AbstractAccountingRegister(Long ownerId, Currency currency) {
    this.ownerId = ownerId;
    this.currency = currency;
  }

  public AbstractAccountingRegister(Long id, Long ownerId, Currency currency) {
    this.id = id;
    this.ownerId = ownerId;
    this.currency = currency;
  }

  public Long getId() {
    return this.id;
  }


  public Currency getCurrency() {
    return this.currency;
  }

  public Long getOwnerId() {
    return this.ownerId;
  }
}
