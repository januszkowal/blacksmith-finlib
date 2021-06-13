package org.blacksmith.finlib.curve.iterator;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.curve.Curve;
import org.blacksmith.finlib.curve.iterator.CurveIterator;

public class CurveIntegerIterator extends CurveIterator<Integer> {
  public CurveIntegerIterator(LocalDate valuationDate, int min, int max, Curve curve) {
    super(valuationDate, min, max, curve);
  }

  public static CurveIntegerIterator of(LocalDate valuationDate, int min, int max, Curve curve) {
    return new CurveIntegerIterator(valuationDate, min, max, curve);
  }

  @Override
  public List<Index> getXPoints() {
    long count = max - min + 1;
    LocalDate start = valuationDate.plusDays(min);
    return Stream.iterate(start, date -> date.plusDays(1))
        .limit(count)
        .map(d -> Index.of(d, curve.getDayCount().yearFraction(valuationDate, d)))
        .collect(Collectors.toList());
  }
}
