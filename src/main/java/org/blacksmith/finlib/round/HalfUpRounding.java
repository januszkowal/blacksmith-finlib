package org.blacksmith.finlib.round;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.blacksmith.commons.arg.Validate;

public class HalfUpRounding implements Rounding {
  private final int decimalPlaces;
  private final int fraction;
  private final transient BigDecimal fractionDecimal;
  private final transient int uniqueHashCode;

  private static final HalfUpRounding[] CACHE = new HalfUpRounding[16];

  static {
    for (int i = 0; i < 16; i++) {
      CACHE[i] = new HalfUpRounding(i, 0);
    }
  }

  public static HalfUpRounding ofDecimalPlaces(int decimalPlaces) {
    if (decimalPlaces >= 0 && decimalPlaces < 16) {
      return CACHE[decimalPlaces];
    }
    return new HalfUpRounding(decimalPlaces, 1);
  }

  public static HalfUpRounding ofFractionalDecimalPlaces(int decimalPlaces, int fraction) {
    return new HalfUpRounding(decimalPlaces, fraction);
  }

  private HalfUpRounding(
      int decimalPlaces,
      int fraction) {

    if (decimalPlaces < -255 || decimalPlaces > 255) {
      throw new IllegalArgumentException("Invalid decimal places, must be from 0 to 255 inclusive");
    }
    if (fraction < 0 || fraction > 256) {
      throw new IllegalArgumentException("Invalid fraction, must be from 0 to 256 inclusive");
    }
    this.decimalPlaces = decimalPlaces;
    this.fraction = (fraction <= 1 ? 0 : fraction);
    this.fractionDecimal = (fraction <= 1 ? null : BigDecimal.valueOf(this.fraction));
    this.uniqueHashCode = (this.decimalPlaces << 16) + this.fraction;
  }


  @Override public BigDecimal round(BigDecimal value) {
    if (fraction > 1) {
      return value
          .multiply(fractionDecimal)
          .setScale(decimalPlaces, RoundingMode.HALF_UP)
          .divide(fractionDecimal);
    }
    return value.setScale(decimalPlaces, RoundingMode.HALF_UP);
  }

  public int getDecimalPlaces() {
    return decimalPlaces;
  }

  @Override
  public int hashCode() {
    return uniqueHashCode;
  }

  @Override
  public String toString() {
    return "Round to " + (fraction > 1 ? "1/" + fraction + " of " : "") + decimalPlaces + "dp";
  }


}
