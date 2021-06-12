package org.blacksmith.finlib.curve.dbstruct;

import java.time.LocalDate;

import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.curve.definition.CurveDefinition;

public interface YieldCurveDefinitionService {
  CurveDefinition getDefinition(String curveName, LocalDate asOfDate);
}
