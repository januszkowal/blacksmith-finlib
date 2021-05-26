package org.blacksmith.finlib.curves.algoritm;

import java.util.Arrays;

import org.blacksmith.finlib.curves.types.Point2D;

public class AlgorithmUtils {
  public static int binarySearch0(double[] a, double key) {
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

  public static int binarySearch0(Point2D[] a, double key) {
    int low = 0;
    int high = a.length - 1;

    while ((high - low) > 1) {
      int mid = (low + high) >>> 1;
      double midVal = a[mid].getX();
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

  public static int binarySearch1(Point2D[] a, double key) {
    int low = 0;
    int high = a.length - 1;

    while ((high - low) > 1) {
      int mid = (low + high) >>> 1;
      double midVal = a[mid].getX();
      if (midVal < key)
        low = mid + 1;  // Neither val is NaN, thisVal is smaller
      else if (midVal > key)
        high = mid - 1; // Neither val is NaN, thisVal is larger
      else {
        long midBits = Double.doubleToLongBits(midVal);
        long keyBits = Double.doubleToLongBits(key);
        if (midBits == keyBits)     // Values are equal
          return mid;             // Key found
        else if (midBits < keyBits) // (-0.0, 0.0) or (!NaN, NaN)
          low = mid + 1;
        else                        // (0.0, -0.0) or (NaN, !NaN)
          high = mid - 1;
      }
    }
    return low;
  }

  public static int binarySearchA(double[] a, double key) {
    int index = Arrays.binarySearch(a, key);
    if (index < 0) {
      index = -index - 2;
    }
    if (index >= a.length) {
      --index;
    }
    return index;
  }

  public static int binarySearchA(Point2D[] a, double key) {
    //faster but need additional check
    int index = binarySearch1(a, key);
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
}
