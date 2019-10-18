package org.blacksmith.finlib.accounting.register;

import org.blacksmith.finlib.accounting.Register;
import org.blacksmith.finlib.basic.Currency;

public class RegisterFactory {
  public static final String REF_NOSTRO = "NOSTRO";
  public static final String REG_NOSTRO = "NOSTRO";
  public static final String REF_DEAL = "DEAL";
  public static final String REF_PORTFOLIO = "PORTFOLIO";
  
  public static Register ofNostro(Long nostroId, Currency currency) {
    return Register.ofNew(REG_NOSTRO, currency, REF_NOSTRO, nostroId);
  }
  
  public static Register ofNostroWidthId(Long registerId, Long nostroId, Currency currency) {
    return Register.of(registerId, REG_NOSTRO, currency, REF_NOSTRO, nostroId);
  }
  
  public static Register ofDeal(String name, Long dealId, Currency currency) {
    return Register.ofNew(name, currency, REF_DEAL, dealId);
  }
  
  public static Register ofDealWidthId(Long registerId, String name, Long dealId, Currency currency) {
    return Register.of(registerId, name, currency, REF_DEAL, dealId);
  }

  public static Register ofProdDeal(String name, Long dealId, Currency currency, Long productId) {
    Register reg = Register.ofNew(name, currency, REF_DEAL, dealId);
    reg.addAttribute("PRODUCT", productId);
    return reg;
  }
  
  public static Register ofProdDealWidthId(Long registerId, String name, Long dealId, Currency currency, Long productId) {
    Register reg = Register.of(registerId, name, currency, REF_DEAL, dealId);
    reg.addAttribute("PRODUCT", productId);
    return reg;
  }

  public static Register ofPosCcy(Long portfolioId, String name, Currency currency, Currency repCcy) {
    Register reg = Register.ofNew(name, currency, REF_PORTFOLIO, portfolioId);
    reg.addAttribute("RCCY", repCcy.getIsoCode());
    return reg;
  }

  public static Register ofPosCcyWithId(Long registerId, Long portfolioId, String name, Currency currency, Currency repCcy) {
    Register reg = Register.of(registerId, name, currency, REF_PORTFOLIO, portfolioId);
    reg.addAttribute("RCCY", repCcy.getIsoCode());
    return reg;
  }

}
