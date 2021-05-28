package org.blacksmith.finlib.curve.dbstruct;

import java.time.LocalDate;

import org.blacksmith.finlib.basic.currency.Currency;

public interface YieldCurveDefinitionService {
  CurveDefinitionExt getDefinition(String curveName, Currency currency, LocalDate asOfDate);
}
