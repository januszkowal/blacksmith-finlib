package org.blacksmith.finlib.curve;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.blacksmith.commons.datetime.DateUtils;

public class CurveDateIterator extends CurveIterator<LocalDate> {
  public CurveDateIterator(LocalDate valuationDate, LocalDate min, LocalDate max, Curve curve) {
    super(valuationDate, min, max, curve);
  }

  @Override
  public List<Index> getXPoints() {
    int iMin = Math.toIntExact(DateUtils.daysBetween(valuationDate, min));
    int iMax = Math.toIntExact(DateUtils.daysBetween(valuationDate, max));
    return IntStream.rangeClosed(iMin, iMax)
        .mapToObj(i -> Index.of(valuationDate.plusDays(i), curve.getDayCount().yearFraction(valuationDate, valuationDate.plusDays(i))))
        .collect(Collectors.toList());
  }
}
