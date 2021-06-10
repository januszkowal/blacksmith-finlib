package org.blacksmith.finlib.curve;

import java.time.LocalDate;
import java.util.List;

import org.blacksmith.finlib.math.analysis.interpolation.AlgorithmType;
import org.blacksmith.finlib.math.analysis.interpolation.InterpolatorFactory;
import org.blacksmith.finlib.curve.types.Knot;
import org.blacksmith.finlib.interest.basis.DayCount;
import org.blacksmith.finlib.interest.basis.StandardDayCounts;
import org.junit.jupiter.api.Test;

class CurveIteratorTest {

  final InterpolatorFactory factory = new InterpolatorFactory();

  @Test
  public void shouldDatesIterate() {
    LocalDate d1 = LocalDate.of(2021, 7, 20);
    LocalDate d2 = LocalDate.of(2021, 9, 1);
    DayCount dayCount = StandardDayCounts.D30_E_360;
    var akimaInterpolatorBlackSmith = factory.createFunction(AlgorithmType.AKIMA_SPLINE_BLACKSMITH, create365DayKnots());
  }

  @Test
  public void shouldDatesIterate2() {
    LocalDate d1 = LocalDate.of(2021, 7, 20);
    LocalDate d2 = LocalDate.of(2021, 9, 1);
    DayCount dayCount = StandardDayCounts.D30_E_360;
    var akimaInterpolatorBlackSmith = factory.createFunction(AlgorithmType.AKIMA_SPLINE_BLACKSMITH, create365DayKnots());
  }

  private List<Knot> create365DayKnots() {
    return List.of(Knot.of(0, 2.43d),
        Knot.of(1, 2.50d),
        Knot.of(7, 3.07d),
        Knot.of(14, 3.36d),
        Knot.of(30, 3.71d),
        Knot.of(90, 4.27d),
        Knot.of(182, 4.38d),
        Knot.of(273, 4.47d),
        Knot.of(365, 4.52d));
  }

}
