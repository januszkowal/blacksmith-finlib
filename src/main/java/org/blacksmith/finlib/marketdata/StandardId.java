package org.blacksmith.finlib.marketdata;

import org.blacksmith.commons.arg.ArgChecker;

import lombok.Value;

@Value
public class StandardId {
  //Symbology=OG-Ticker
  private final String scheme;
  //Ticker=EUR=ON
  private final String value;

  public StandardId(String scheme, String value) {
    ArgChecker.notBlank(scheme, "Scheme must be not blank");
    ArgChecker.notBlank(value, "Value must be not blank");
    this.scheme = scheme;
    this.value = value;
  }

  public static StandardId of(String scheme, String value) {
    return new StandardId(scheme, value);
  }

  public String toString() {
    return scheme + "~" + value;
  }
}
