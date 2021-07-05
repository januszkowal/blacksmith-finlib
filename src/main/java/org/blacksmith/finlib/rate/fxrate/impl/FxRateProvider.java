package org.blacksmith.finlib.rate.fxrate.impl;

import java.time.LocalDate;
import java.util.function.Function;

import org.blacksmith.finlib.rate.fxrate.FxRate3;

public interface FxRateProvider {
  <R extends FxRateOperations<R>> R rate(CurrencyPairExt2 pair, LocalDate date, Function<FxRate3, R> extractor);
}
