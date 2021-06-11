package org.blacksmith.finlib.curve.iterator;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.blacksmith.finlib.curve.Curve;
import org.blacksmith.finlib.curve.iterator.CurveIterator;

public class CurveIntegerIterator extends CurveIterator<Integer> {
  public CurveIntegerIterator(LocalDate valuationDate, Integer min, Integer max, Curve curve) {
    super(valuationDate, min, max, curve);
  }

  @Override
  public List<Index> getXPoints() {
    return IntStream.rangeClosed(this.min, this.max)
        .mapToObj(i -> Index.of(valuationDate.plusDays(i), curve.getDayCount().yearFraction(valuationDate, valuationDate.plusDays(i))))
        .collect(Collectors.toList());
  }
}