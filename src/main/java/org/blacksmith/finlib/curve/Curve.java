package org.blacksmith.finlib.curve;

import java.time.LocalDate;

import org.blacksmith.finlib.NamedItem;
import org.blacksmith.finlib.math.analysis.UnivariateFunction;
import org.blacksmith.finlib.datetime.daycount.DayCount;

public interface Curve extends NamedItem {
  double value(double x);
  double value(LocalDate date);
  DayCount getDayCount();
  LocalDate getValuationDate();
}
