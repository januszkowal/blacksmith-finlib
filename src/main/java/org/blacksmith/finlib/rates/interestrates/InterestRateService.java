package org.blacksmith.finlib.rates.interestrates;

import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rates.marketdata.MarketDataProvider;

public interface InterestRateService extends MarketDataProvider<InterestRateId, Rate> {
}
