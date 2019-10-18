package org.blacksmith.finlib.accounting.register;

import org.blacksmith.finlib.accounting.AccountingRegister;
import org.blacksmith.finlib.basic.Currency;

public class RegisterFactory {
  public static AccountingRegister ofNostro(Long nostroId, Currency currency) {
    return NostroRegister.ofNew(nostroId, currency);
  }
  public static AccountingRegister ofNostroWithId(Long id, Long nostroId, Currency currency) {
    return NostroRegister.ofExisting(id, nostroId, currency);
  }
  public static AccountingRegister ofPortoflio(Long portfolioId, Currency currency) {
    return PortfolioCashRegister.ofNew(portfolioId, currency);
  }
}
