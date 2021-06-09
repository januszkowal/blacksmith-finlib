package org.blacksmith.finlib.curve;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.blacksmith.finlib.curve.types.CurvePoint;

import lombok.Value;

public abstract class CurveIterator<T> {
  protected final Curve curve;
  protected final LocalDate valuationDate;
  protected final T min;
  protected final T max;

  public CurveIterator(LocalDate valuationDate, T min, T max, Curve curve) {
    this.valuationDate = valuationDate;
    this.min = min;
    this.max = max;
    this.curve = curve;
  }

  public abstract List<Index> getXPoints();

  public List<CurvePoint> values() {
    return getXPoints().stream()
        .map(xPoint -> createCurvePoint(xPoint))
        .collect(Collectors.toList());
  }

  public CurvePoint createCurvePoint(Index index) {
    return CurvePoint.of(index.date, index.x, curve.value(index.x));
  }

  @Value(staticConstructor = "of")
  public static class Index {
    LocalDate date;
    double x;
  }
}
