package org.blacksmith.finlib.curve;

import java.time.LocalDate;

import org.blacksmith.finlib.NamedItem;
import org.blacksmith.finlib.math.analysis.UnivariateFunction;
import org.blacksmith.finlib.interest.basis.DayCount;

public interface Curve extends UnivariateFunction, NamedItem {
  double value(LocalDate date);
  DayCount getDayCount();
  boolean isKnot(int x);
}
