package org.blacksmith.finlib.accounting.register;

import org.blacksmith.finlib.accounting.AccountingRegister;
import org.blacksmith.finlib.basic.Currency;
import org.blacksmith.finlib.accounting.RegisterOwnerType;

public class PortfolioCashRegister extends AbstractAccountingRegister implements AccountingRegister {

  private final static RegisterOwnerType OWNER_TYPE = new PortfolioRegisterOwner();

  public PortfolioCashRegister(Long sourceId, Currency currency) {
    super(sourceId, currency);
  }

  public PortfolioCashRegister(Long id, Long sourceId, Currency currency) {
    super(id, sourceId, currency);
  }

  public static PortfolioCashRegister ofExisting(Long id, Long sourceId, Currency currency) {
    return new PortfolioCashRegister(id, sourceId,currency);
  }

  public static PortfolioCashRegister ofNew(Long sourceId, Currency currency) {
    return new PortfolioCashRegister(sourceId,currency);
  }

  public static class PortfolioRegisterOwner implements RegisterOwnerType {
    @Override public String name() {
      return "PORTFOLIO";
    }
  }

  @Override
  public String getName() {
    return "CASH";
  }

  @Override
  public RegisterOwnerType getOwnerType() {
    return OWNER_TYPE;
  }

}
