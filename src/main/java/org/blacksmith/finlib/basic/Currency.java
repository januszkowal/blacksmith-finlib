package org.blacksmith.finlib.basic;

import lombok.Data;

@Data(staticConstructor = "of")
public class Currency {

  public static final Currency EUR = Currency.of("EUR");
  public static final Currency USD = Currency.of("USD");
  public static final Currency GBP = Currency.of("GBP");
  public static final Currency CHF = Currency.of("CHF");
  public static final Currency PLN = Currency.of("PLN");
  public static final Currency JPY = Currency.of("JPY");

  private final String isoCode;
}
