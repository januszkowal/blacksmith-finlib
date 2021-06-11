package org.blacksmith.finlib.math.analysis.interpolation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InterpolationUtils {
  public static int getKnotIndex0(double[] a, double key) {
    int low = 0;
    int high = a.length - 1;

    while (high >= low) {
      int mid = (low + high) >>> 1;
      double midVal = a[mid];
      if (midVal < key)
        low = mid + 1;
      else if (midVal > key)
        high = mid - 1;
      else {
        return mid;
      }
    }
    return low - 1;
  }

  public static int getKnotIndex1(double[] knots, double key) {
    int index = Arrays.binarySearch(knots, key);
    if (index < 0) {
      index = -index - 2;
    }
    if (index >= knots.length) {
      --index;
    }
    return index;
  }

  public static void checkOrder(double[] val, String message) {
    for (int i = 1; i < val.length; i++) {
      if (val[i] < val[i - 1]) {
        throw new IllegalArgumentException(message);
      }
    }
  }

  public static void checkIncreasing(double[] val, String message) {
    for (int i = 1; i < val.length; i++) {
      if (val[i] <= val[i - 1]) {
        throw new IllegalArgumentException(message);
      }
    }
  }

  public static void checkArraysSize(double[] a, int size, String message) {
    if (a.length != size)
      throw new IllegalArgumentException(message);
  }

  public static void checkMinSize(double[] xValues, int min) {
    if (xValues.length < min)
      throw new IllegalArgumentException("Minimum arrays size is: " + min);
  }

  public static List<CalcRange> getCalculationRanges(int min, int max, double[] knots, int polynomialLength) {
    List<CalcRange> ranges = new ArrayList<>();
    int knotIndex = InterpolationUtils.getKnotIndex0(knots, min);
    if (knotIndex < 0)
      throw new IllegalArgumentException("Invalid calculation range: " + min + " - " + max);
    int rangeStart = min;

    int lastKnot = Math.min(polynomialLength, knots.length) - 1;
    while (knotIndex <= lastKnot) {
      if (knotIndex == lastKnot) {
        ranges.add(new CalcRange(rangeStart, max, knotIndex));
        break;
      } else {
        // Range end is equal next knot ceil - 1
        int rangeEnd = (int) Math.ceil(knots[knotIndex + 1]) - 1;
        ranges.add(new CalcRange(rangeStart, Math.min(rangeEnd, max), knotIndex));
        if (rangeEnd >= max)
          break;
        rangeStart = rangeEnd + 1;
      }
      knotIndex++;
    }
    return ranges;
  }

  public static class CalcRange {
    final int start;
    final int end;
    final int knotIndex;

    public CalcRange(int start, int end, int knotIndex) {
      this.start = start;
      this.end = end;
      this.knotIndex = knotIndex;
    }
  }
}
