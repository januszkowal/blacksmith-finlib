package org.blacksmith.finlib.accounting.register;

import org.blacksmith.finlib.accounting.AccountingRegister;
import org.blacksmith.finlib.basic.Currency;
import org.blacksmith.finlib.accounting.RegisterOwnerType;

public class ProductTradeRegister extends AbstractAccountingRegister implements AccountingRegister {

  private final static RegisterOwnerType OWNER_TYPE = new ProductRegisterOwner();

  private final String name;
  private final Long productId;

  public ProductTradeRegister(Long id, String name, Long sourceId, Currency currency, Long productId) {
    super(id, sourceId, currency);
    this.name = name;
    this.productId = productId;
  }

  public ProductTradeRegister(String name, Long sourceId, Currency currency, Long productId) {
    super(sourceId, currency);
    this.name = name;
    this.productId = productId;
  }

  public static ProductTradeRegister ofNew (String name, Long sourceId, Currency currency, Long productId) {
    return new ProductTradeRegister(name, sourceId, currency, productId);
  }

  public static ProductTradeRegister ofExisting (Long id, String name, Long sourceId, Currency currency, Long productId) {
    return new ProductTradeRegister(id, name, sourceId, currency, productId);
  }

  public static class ProductRegisterOwner implements RegisterOwnerType {
    @Override public String name() {
      return "PRODUCT";
    }
  }

  @Override public String getName() {
    return this.name;
  }

  @Override
  public RegisterOwnerType getOwnerType() {
    return OWNER_TYPE;
  }

  public Long getProductId() {
    return this.productId;
  }
}
