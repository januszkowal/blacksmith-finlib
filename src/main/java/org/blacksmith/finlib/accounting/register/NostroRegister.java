package org.blacksmith.finlib.accounting.register;

import org.blacksmith.finlib.accounting.AccountingRegister;
import org.blacksmith.finlib.basic.Currency;
import org.blacksmith.finlib.accounting.RegisterOwnerType;

public class NostroRegister extends AbstractAccountingRegister implements AccountingRegister {

  private final static RegisterOwnerType OWNER_TYPE = new NostroRegisterOwner();

  public NostroRegister(Long sourceId, Currency currency) {
    super(sourceId, currency);
  }

  public NostroRegister(Long id, Long sourceId, Currency currency) {
    super(id, sourceId, currency);
  }

  public static NostroRegister ofNew (Long sourceId, Currency currency) {
    return new NostroRegister(sourceId, currency);
  }

  public static NostroRegister ofExisting (Long id, Long sourceId, Currency currency) {
    return new NostroRegister(id, sourceId, currency);
  }


  public static class NostroRegisterOwner implements RegisterOwnerType {
    @Override public String name() {
      return "NOSTRO";
    }
  }

  @Override
  public String getName() {
    return "NOSTRO";
  }

  @Override
  public RegisterOwnerType getOwnerType() {
    return OWNER_TYPE;
  }
}
