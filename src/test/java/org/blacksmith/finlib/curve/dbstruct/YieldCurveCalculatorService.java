package org.blacksmith.finlib.curve.dbstruct;

import java.time.LocalDate;
import java.util.List;

import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.curve.types.Knot;
import org.blacksmith.finlib.curve.CurveDefinition;
import org.blacksmith.finlib.curve.YieldCurveCalculator;
import org.blacksmith.finlib.curve.YieldCurveRate;

public class YieldCurveCalculatorService {
  YieldCurveDefinitionService definitionService;
  YieldCurveCalculator curveCalculator;
  YieldCurveSourceRatesService yieldCurveSourceRatesService;

  public void calculate(String curveName, Currency currency, LocalDate asOfDate) {
    CurveDefinitionExt definitionExt = definitionService.getDefinition(curveName, currency, asOfDate);
    List<Knot> knots = yieldCurveSourceRatesService.getKnots(asOfDate, definitionExt.getKnots());
    CurveDefinition definition = CurveDefinition.of(curveName, definitionExt.getCurveType(), definitionExt.getYearLength());
    var curveRates = curveCalculator.calculate(asOfDate, definition, knots);
    store(curveName, currency, asOfDate, curveRates);
  }

  private void store(String curveName, Currency currency, LocalDate asOfDate, List<YieldCurveRate> curveRates) {

  }
}