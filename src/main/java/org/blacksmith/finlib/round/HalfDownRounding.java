package org.blacksmith.finlib.round;

import java.math.BigDecimal;

public class HalfDownRounding implements Rounding {
  /**
   * 
   */
  private static final long serialVersionUID = 134917347960240196L;
  private final int decimalPlaces;

  private static final int MIN_DECIMAL_PLACES = -3;
  private static final int CACHE_SIZE = 19;
  private static final HalfDownRounding[] CACHE = new HalfDownRounding[CACHE_SIZE];
  
  static {
    for (int i = 0; i < CACHE_SIZE; i++) {
      CACHE[i] = new HalfDownRounding(i+MIN_DECIMAL_PLACES);
    }
  }
  
  public static HalfDownRounding ofDecimalPlaces(int decimalPlaces) {
    if (decimalPlaces >= MIN_DECIMAL_PLACES && decimalPlaces < (CACHE_SIZE+MIN_DECIMAL_PLACES)) {
      return CACHE[decimalPlaces-MIN_DECIMAL_PLACES];
    }
    return new HalfDownRounding(decimalPlaces);
  }

  private HalfDownRounding(
      int decimalPlaces) {

    if (decimalPlaces < -9 || decimalPlaces > 255) {
      throw new IllegalArgumentException("Invalid decimal places, must be from -9 to 255 inclusive");
    }
    this.decimalPlaces = decimalPlaces;
  }


  @Override 
  public BigDecimal round(BigDecimal value) {
    return value.setScale(decimalPlaces, java.math.RoundingMode.DOWN);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    HalfDownRounding other = (HalfDownRounding) obj;
    if (decimalPlaces != other.decimalPlaces)
      return false;
    return true;
  }

  public int getDecimalPlaces() {
    return decimalPlaces;
  }

  @Override
  public int hashCode() {
    return (this.decimalPlaces << 16);
  }

  @Override
  public String toString() {
    return "Truncate to " + decimalPlaces + "dp";
  }
}
