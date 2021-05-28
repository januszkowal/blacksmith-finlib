package org.blacksmith.finlib.curve.dbstruct;

import java.time.LocalDate;
import java.util.List;

import org.blacksmith.finlib.curve.types.Knot;

public interface YieldCurveSourceRatesService {
  List<Knot> getKnots(LocalDate asOfDate, List<KnotDefinition> knots);
}
