package org.blacksmith.finlib.accounting.register;

import org.blacksmith.finlib.accounting.Register;
import org.blacksmith.finlib.basic.Currency;

public class RegisterFactory {
  public static final String REF_NOSTRO = "NOSTRO";
  public static final String REG_NOSTRO = "NOSTRO";
  public static final String REF_DEAL = "DEAL";
  public static final String REF_PORTFOLIO = "PORTFOLIO";
  public static final String ATTR_PRODUCT = "PRODUCT";
  public static final String ATTR_REFCCY = "REFCCY";
  
  private RegisterProvider registerProvider;
  
  public RegisterFactory(RegisterProvider registerProvider) {
    this.registerProvider = registerProvider;
  }
  
  public Register ofNostro(Long nostroId, Currency currency) {
    Long id = registerProvider.getRegister();
    return Register.of(id, REG_NOSTRO, currency, REF_NOSTRO, nostroId);
  }
  
  public Register ofDeal(String name, Long dealId, Currency currency) {
    Long id = registerProvider.getRegister();
    return Register.of(id, name, currency, REF_DEAL, dealId);
  }
  
  public Register ofProdDeal(String name, Long dealId, Currency currency, Long productId) {
    Long id = registerProvider.getRegister();
    Register reg = Register.of(id, name, currency, REF_DEAL, dealId);
    reg.addAttribute(ATTR_PRODUCT, productId);
    return reg;
  }
  
  public Register ofPosCcy(Long portfolioId, String name, Currency currency, Currency repCcy) {
    Long id = registerProvider.getRegister();
    Register reg = Register.of(id, name, currency, REF_PORTFOLIO, portfolioId);
    reg.addKeyAttribute(ATTR_REFCCY, repCcy.getIsoCode());
    return reg;
  }
}
