package org.blacksmith.finlib.rate.intrate;

import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rate.marketdata.MarketDataProvider;

public interface InterestRateService extends MarketDataProvider<InterestRateId, Rate> {
}
