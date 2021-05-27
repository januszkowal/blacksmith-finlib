package org.blacksmith.finlib.curves.algoritm;

import java.util.Arrays;

import org.blacksmith.finlib.curves.types.Point2D;

public class AlgorithmUtils {
  public static int getKnotIndex0(double[] a, double key) {
    int low = 0;
    int high = a.length - 1;

    while ((high - low) > 1) {
      int mid = (low + high) >>> 1;
      double midVal = a[mid];
      if (midVal < key)
        low = mid;  // Neither val is NaN, thisVal is smaller
      else if (midVal > key)
        high = mid; // Neither val is NaN, thisVal is larger
      else {
        long midBits = Double.doubleToLongBits(midVal);
        long keyBits = Double.doubleToLongBits(key);
        if (midBits == keyBits)     // Values are equal
          return mid;             // Key found
        else if (midBits < keyBits) // (-0.0, 0.0) or (!NaN, NaN)
          low = mid;
        else                        // (0.0, -0.0) or (NaN, !NaN)
          high = mid;
      }
    }
    return low;
  }

  public static int getKnotIndex(double[] a, double key) {
    int index = Arrays.binarySearch(a, key);
    if (index < 0) {
      index = -index - 2;
    }
    if (index >= a.length) {
      --index;
    }
    return index;
  }

  public static void checkOrder(double[] val) {
    double prior = val[0];
    for (int i=1; i < val.length; i++) {
      if (val[i] <= prior) {
        throw new IllegalArgumentException("Invalid data order");
      }
    }
  }

  public static void checkArraysSize(double[] xvals, double[] yvals) {
    if (xvals.length == 0)
      throw new IllegalArgumentException("Zero length x-values");
    if (yvals.length == 0)
      throw new IllegalArgumentException("Zero length y-values");
    if (xvals.length != yvals.length)
      throw new IllegalArgumentException("x-values and y-values length must be equal");
  }

  public static void checkMinSize(double[] xvals, int min) {
    if (xvals.length < min)
      throw new IllegalArgumentException("Minimum arrays size is: " + min);
  }
}
