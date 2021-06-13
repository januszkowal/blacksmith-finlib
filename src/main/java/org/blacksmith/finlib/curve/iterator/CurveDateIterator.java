package org.blacksmith.finlib.curve.iterator;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.blacksmith.commons.datetime.DateRange;
import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.curve.Curve;
import org.blacksmith.finlib.curve.iterator.CurveIterator;

public class CurveDateIterator extends CurveIterator<LocalDate> {
  public CurveDateIterator(LocalDate valuationDate, LocalDate min, LocalDate max, Curve curve) {
    super(valuationDate, min, max, curve);
  }
  public static CurveDateIterator of(LocalDate valuationDate, LocalDate min, LocalDate max, Curve curve) {
    return new CurveDateIterator(valuationDate, min, max, curve);
  }

  @Override
  public List<Index> getXPoints() {
    long count = DateUtils.daysBetween(min, max) + 1;
    return Stream.iterate(min, date -> date.plusDays(1))
        .limit(count)
        .map(d -> Index.of(d, curve.getDayCount().yearFraction(valuationDate, d)))
        .collect(Collectors.toList());
  }
}
