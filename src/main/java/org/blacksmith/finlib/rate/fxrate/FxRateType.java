package org.blacksmith.finlib.rate.fxrate;

import java.util.function.Function;

import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.marketdata.MarketData;

public enum FxRateType {
  BUY(r3 -> r3.getValue().getBuy()),
  SELL(r3 -> r3.getValue().getSell()),
  AVG(r3 -> r3.getValue().getAvg());

  private final Function<FxRate3, Rate> rateExtractor;

  FxRateType(Function<FxRate3, Rate> rateExtractor) {
    this.rateExtractor = rateExtractor;
  }

  public Rate toRate(FxRate3 rate) {
    return rateExtractor.apply(rate);
  }

  public FxRate toFxRate(FxRate3 rate) {
    return FxRate.of(rate.getDate(), rateExtractor.apply(rate));
  }
}
