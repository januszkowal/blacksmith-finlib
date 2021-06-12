package org.blacksmith.finlib.rate.marketdata;

import org.blacksmith.finlib.marketdata.MarketData;
import org.blacksmith.finlib.marketdata.MarketDataId;

public interface MarketDataService<I extends MarketDataId<V>, V extends MarketData> {
//  V getRate(K key, LocalDate date);
//
//  default <R> R getRateValue(K key, LocalDate date, Function<MarketData<V>, R> valueExtractor) {
//    return Optional.ofNullable(getRate(key, date))
//        .map(valueExtractor)
//        .orElse(null);
//  }
//
//  default V getRateValue(K key, LocalDate date) {
//    return Optional.ofNullable(getRate(key, date))
//        .map(MarketData::getValue)
//        .orElse(null);
//  }
}
