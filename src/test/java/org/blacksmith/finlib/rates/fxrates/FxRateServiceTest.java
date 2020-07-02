package org.blacksmith.finlib.rates.fxrates;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.apache.groovy.util.Maps;
import org.blacksmith.finlib.basic.Currency;
import org.blacksmith.finlib.basic.Rate;
import org.blacksmith.finlib.rates.basic.MarketDataMemoryService;
import org.blacksmith.finlib.rates.basic.BasicMarketDataHolder;
import org.blacksmith.finlib.rates.fxccypair.FxCurrencyPair;
import org.blacksmith.finlib.rates.fxccypair.FxCurrencyPairProvider;
import org.blacksmith.finlib.rates.fxrates.FxRate3.FxRateValues;
import org.blacksmith.finlib.rates.fxrates.service.FxRateService;
import org.blacksmith.finlib.rates.fxrates.service.FxRateServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class FxRateServiceTest {

  static int DECIMAL_PLACES = 4;
  static int OUTPUT_DECIMAL_PLACES = 6;

  private static FxRateService rateService;
  private static Map<String, FxCurrencyPair> pairs = Maps.of(
      "EURPLN", FxCurrencyPair.of(Currency.EUR, Currency.PLN, false, 1.0d),
      "USDPLN", FxCurrencyPair.of(Currency.USD, Currency.PLN, false, 1.0d),
      "EURUSD", FxCurrencyPair.of(Currency.EUR, Currency.USD, true, 1.0d),
      "HUFPLN", FxCurrencyPair.of(Currency.of("HUF"), Currency.PLN, false, 0.01d),
      "EURHUF", FxCurrencyPair.of(Currency.EUR, Currency.of("HUF"), true, 1.0d));

  private static MarketDataMemoryService<FxRateId, FxRateValues> fxRateSourceService =
      new MarketDataMemoryService<>();

  @BeforeAll
  public static void setUp() {
    FxCurrencyPairProvider currencyPairProvider = new FxCurrencyPairProvider() {
      @Override
      public FxCurrencyPair getPair(String ccy1, String ccy2) {
        return pairs.get(ccy1 + ccy2);
      }
    };
    fxRateSourceService.setMarketData(List.of(
        BasicMarketDataHolder.of(FxRateId.of("EUR", "PLN"), FxRate3.of(LocalDate.parse("2020-01-01"), 4d, 4.2d, 4.1234567d)),
        BasicMarketDataHolder.of(FxRateId.of("USD", "PLN"), FxRate3.of(LocalDate.parse("2020-01-01"), 3.7d, 3.8d, 3.913131313d)),
        BasicMarketDataHolder
            .of(FxRateId.of("HUF", "PLN"), FxRate3.of(LocalDate.parse("2020-01-01"), 1.25d, 1.27d, 1.2631312d)),
        BasicMarketDataHolder
            .of(FxRateId.of("EUR", "PLN"), FxRate3.of(LocalDate.parse("2020-01-02"), 3.9d, 4.3d, 4.15312312d))
    ));
    rateService = new FxRateServiceImpl(currencyPairProvider, fxRateSourceService, Currency.PLN,
        DECIMAL_PLACES,OUTPUT_DECIMAL_PLACES);
  }

  @Test
  public void testSimple() {
    assertEquals(4.1235d,
        rateService.getRateDouble(FxRateId.of("EUR", "PLN"), LocalDate.parse("2020-01-01"), FxRateType.AVG));
    assertEquals(Rate.of(1/4.1235d,OUTPUT_DECIMAL_PLACES).doubleValue(),
        rateService.getRateDouble(FxRateId.of("PLN", "EUR"), LocalDate.parse("2020-01-01"), FxRateType.AVG));
    assertEquals(Rate.of(0.012631d,OUTPUT_DECIMAL_PLACES).doubleValue(),
        rateService.getRateDouble(FxRateId.of("HUF", "PLN"), LocalDate.parse("2020-01-01"), FxRateType.AVG));
    assertEquals(Rate.of(1/0.012631d,OUTPUT_DECIMAL_PLACES).doubleValue(),
        rateService.getRateDouble(FxRateId.of("PLN", "HUF"), LocalDate.parse("2020-01-01"), FxRateType.AVG));
  }

  @Test
  public void testCross() {
    assertEquals(Rate.of(4.1235d/3.9131d,OUTPUT_DECIMAL_PLACES).doubleValue(),
        rateService.getRateDouble(FxRateId.of("EUR", "USD"), LocalDate.parse("2020-01-01"), FxRateType.AVG));
    assertEquals(Rate.of(3.9131d/4.1235d,OUTPUT_DECIMAL_PLACES).doubleValue(),
        rateService.getRateDouble(FxRateId.of("USD","EUR"), LocalDate.parse("2020-01-01"), FxRateType.AVG));
    assertEquals(Rate.of(100*4.1235d/1.2631,OUTPUT_DECIMAL_PLACES).doubleValue(),
        rateService.getRateDouble(FxRateId.of("EUR", "HUF"), LocalDate.parse("2020-01-01"), FxRateType.AVG));
    assertEquals(Rate.of(1.2631d/4.1235d/100,OUTPUT_DECIMAL_PLACES).doubleValue(),
        rateService.getRateDouble(FxRateId.of("HUF", "EUR"), LocalDate.parse("2020-01-01"), FxRateType.AVG));
  }


}