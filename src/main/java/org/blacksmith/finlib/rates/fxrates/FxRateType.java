package org.blacksmith.finlib.rates.fxrates;

import org.blacksmith.finlib.basic.Rate;
import org.blacksmith.finlib.rates.MarketData;
import org.blacksmith.finlib.rates.MarketDataExtractor;
import org.blacksmith.finlib.rates.fxrates.FxRate3.FxRateValues;

public enum FxRateType {
  BUY(r3->r3.getValue().getBuyRate()),
  SELL(r3->r3.getValue().getSellRate()),
  AVG(r3->r3.getValue().getAvgRate());

  private final MarketDataExtractor<FxRateValues,Rate> rateExtractor;

  FxRateType(MarketDataExtractor<FxRateValues,Rate> rateExtractor) {
    this.rateExtractor = rateExtractor;
  }

  public Rate extractRate(MarketData<?, FxRateValues> rate) {
    return rateExtractor.extract(rate);
  }

  public FxRate1 extractFxRate(MarketData<?, FxRateValues> rate) {
    return FxRate1.of(rate.getDate(),rateExtractor.extract(rate));
  }
}
