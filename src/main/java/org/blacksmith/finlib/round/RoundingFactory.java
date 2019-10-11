package org.blacksmith.finlib.round;

import java.math.MathContext;
import java.util.Currency;

public class RoundingFactory {
  public static Rounding of(RoundingMode mode, int decimalPlaces) {
    if (mode==RoundingMode.UP) {
      return HalfUpRounding.ofDecimalPlaces(decimalPlaces);
    }
    else
    {
      return new HalfDownRounding();
    }
  }
  public static Rounding of(RoundingMode mode, int decimalPlaces, int fraction) {
    if (mode==RoundingMode.UP) {
      return HalfUpRounding.ofFractionalDecimalPlaces(decimalPlaces,fraction);
    }
    else
    {
      return new HalfDownRounding();
    }
  }
}
