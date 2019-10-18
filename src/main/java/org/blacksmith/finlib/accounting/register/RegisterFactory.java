package org.blacksmith.finlib.accounting.register;

import java.util.HashMap;
import java.util.Map;
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
    Map<String,Object> keys = new HashMap<>();
    Long id = registerProvider.getRegister(keys);
    return Register.of(id, REG_NOSTRO, currency, REF_NOSTRO, nostroId);
  }
  
  public Register ofDeal(String name, Long dealId, Currency currency) {
    Map<String,Object> keys = new HashMap<>();
    Long id = registerProvider.getRegister(keys);
    return Register.of(id, name, currency, REF_DEAL, dealId);
  }
  
  public Register ofProdDeal(String name, Long dealId, Currency currency, Long productId) {
    Map<String,Object> keys = new HashMap<>();
    Long id = registerProvider.getRegister(keys);
    Register reg = Register.of(id, name, currency, REF_DEAL, dealId);
    reg.addAttribute(ATTR_PRODUCT, productId);
    return reg;
  }
  
  public Register ofPosCcy(Long portfolioId, String name, Currency currency, Currency repCcy) {
    Map<String,Object> keys = new HashMap<>();
    Long id = registerProvider.getRegister(keys);
    Register reg = Register.of(id, name, currency, REF_PORTFOLIO, portfolioId);
    reg.addKey(ATTR_REFCCY, repCcy.getIsoCode());
    return reg;
  }
}
