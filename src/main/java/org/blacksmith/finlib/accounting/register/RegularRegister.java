package org.blacksmith.finlib.accounting.register;

import org.blacksmith.finlib.accounting.AccountingRegister;
import org.blacksmith.finlib.basic.Currency;
import org.blacksmith.finlib.accounting.RegisterOwnerType;

public class RegularRegister extends AbstractAccountingRegister implements AccountingRegister {

  private final String name;
  private final RegisterOwnerType ownerType;

  public RegularRegister(Long id, String name, RegisterOwnerType ownerType, Long sourceId, Currency currency) {
    super(id, sourceId, currency);
    this.name = name;
    this.ownerType = ownerType;
  }

  public RegularRegister(String name, RegisterOwnerType ownerType, Long sourceId, Currency currency) {
    super(sourceId, currency);
    this.name = name;
    this.ownerType = ownerType;
  }

  public static RegularRegister ofNew (String name, RegisterOwnerType ownerType, Long sourceId, Currency currency) {
    return new RegularRegister(name, ownerType, sourceId, currency);
  }

  public static RegularRegister ofExisting (Long id, String name, RegisterOwnerType ownerType, Long sourceId, Currency currency) {
    return new RegularRegister(id, name, ownerType, sourceId, currency);
  }

  @Override public String getName() {
    return this.name;
  }

  @Override public RegisterOwnerType getOwnerType() {
    return this.ownerType;
  }
}
