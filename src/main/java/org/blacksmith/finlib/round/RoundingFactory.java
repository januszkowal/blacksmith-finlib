package org.blacksmith.finlib.round;

public class RoundingFactory {
  public static Rounding of(RoundingMode mode, int decimalPlaces) {
    if (mode==RoundingMode.UP) {
      return HalfUpRounding.ofDecimalPlaces(decimalPlaces);
    }
    else
    {
      return HalfDownRounding.ofDecimalPlaces(decimalPlaces);
    }
  }
  
  public static Rounding of(RoundingMode mode, int decimalPlaces, int fraction) {
    if (mode==RoundingMode.UP) {
      return HalfUpRounding.ofFractionalDecimalPlaces(decimalPlaces,fraction);
    }
    else
    {
      if (fraction > 1) {
        throw new IllegalArgumentException("Fraction is not suported for truncating");
      }
      return HalfDownRounding.ofDecimalPlaces(decimalPlaces);
    }
  }
}
