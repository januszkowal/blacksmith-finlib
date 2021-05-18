package org.blacksmith.finlib.rates.fxrates;

import java.util.function.Function;

import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rates.MarketData;

public enum FxRateType {
  BUY(r3 -> r3.getValue().getBuyRate()),
  SELL(r3 -> r3.getValue().getSellRate()),
  AVG(r3 -> r3.getValue().getAvgRate());

  private final Function<FxRate3, Rate> rateExtractor;

  FxRateType(Function<FxRate3, Rate> rateExtractor) {
    this.rateExtractor = rateExtractor;
  }

  public Rate toRate(MarketData<FxRate3> rate) {
    return rateExtractor.apply(rate.getValue());
  }

  public Rate toRate(FxRate3 rate) {
    return rateExtractor.apply(rate);
  }

  public FxRate toFxRate(MarketData<FxRate3> rate) {
    return FxRate.of(rate.getDate(), rateExtractor.apply(rate.getValue()));
  }

  public FxRate toFxRate(FxRate3 rate) {
    return FxRate.of(rate.getDate(), rateExtractor.apply(rate));
  }
}
