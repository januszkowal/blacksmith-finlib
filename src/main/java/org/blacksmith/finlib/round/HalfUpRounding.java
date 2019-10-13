package org.blacksmith.finlib.round;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.blacksmith.commons.arg.Validate;

public class HalfUpRounding implements Rounding, Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 134917347960240196L;
  private final int decimalPlaces;
  private final int fraction;
  private final transient BigDecimal fractionDecimal;

  private static final int MIN_DECIMAL_PLACES = -3;
  private static final int MAX_DECIMAL_PLACES = 15;
  private static final int CACHE_SIZE = MAX_DECIMAL_PLACES - MIN_DECIMAL_PLACES + 1;
  //Cache contains objects with decimalPlaces={-3..15}
  private static final HalfUpRounding[] CACHE = new HalfUpRounding[CACHE_SIZE];
  
  static {
    for (int i = 0; i < CACHE_SIZE; i++) {
      CACHE[i] = new HalfUpRounding(i+MIN_DECIMAL_PLACES, 0);
    }
  }

  public static HalfUpRounding ofDecimalPlaces(int decimalPlaces) {
    if (decimalPlaces >= MIN_DECIMAL_PLACES && decimalPlaces < (CACHE_SIZE+MIN_DECIMAL_PLACES)) {
      return CACHE[decimalPlaces-MIN_DECIMAL_PLACES];
    }
    return new HalfUpRounding(decimalPlaces, 0);
  }

  public static HalfUpRounding ofFractionalDecimalPlaces(int decimalPlaces, int fraction) {
    return new HalfUpRounding(decimalPlaces, fraction);
  }

  private HalfUpRounding(
      int decimalPlaces,
      int fraction) {

    if (decimalPlaces < -9 || decimalPlaces > 255) {
      throw new IllegalArgumentException("Invalid decimal places, must be from -9 to 255 inclusive");
    }
    if (fraction < 0 || fraction > 256) {
      throw new IllegalArgumentException("Invalid fraction, must be from 0 to 256 inclusive");
    }
    this.decimalPlaces = decimalPlaces;
    this.fraction = (fraction <= 1 ? 0 : fraction);
    this.fractionDecimal = (fraction <= 1 ? null : BigDecimal.valueOf(this.fraction));
  }


  @Override 
  public BigDecimal round(BigDecimal value) {
    if (fraction > 1) {
      return value
          .multiply(fractionDecimal)
          .setScale(decimalPlaces, java.math.RoundingMode.HALF_UP)
          .divide(fractionDecimal);
    }
    else
      return value.setScale(decimalPlaces, java.math.RoundingMode.HALF_UP);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    HalfUpRounding other = (HalfUpRounding) obj;
    if ((decimalPlaces != other.decimalPlaces) || (fraction != other.fraction))
      return false;
    return true;
  }

  public int getDecimalPlaces() {
    return decimalPlaces;
  }

  @Override
  public int hashCode() {
    return (this.decimalPlaces << 16) + this.fraction;
  }

  @Override
  public String toString() {
    return "Round to " + (fraction > 1 ? "1/" + fraction + " of " : "") + decimalPlaces + "dp";
  }


}
