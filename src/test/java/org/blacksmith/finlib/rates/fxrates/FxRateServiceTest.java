package org.blacksmith.finlib.rates.fxrates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.apache.groovy.util.Maps;
import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rates.basic.MarketDataMemoryService;
import org.blacksmith.finlib.rates.basic.BasicMarketDataHolder;
import org.blacksmith.finlib.rates.fxccypair.FxCurrencyPair;
import org.blacksmith.finlib.rates.fxrates.service.FxRateService;
import org.blacksmith.finlib.rates.fxrates.service.FxRateServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class FxRateServiceTest {

  static final int DECIMAL_PLACES = 4;
  static final int OUTPUT_DECIMAL_PLACES = 6;

  private static FxRateService rateService;
  private static final Map<String, FxCurrencyPair> pairs = Maps.of(
      "EUR/PLN", FxCurrencyPair.of(Currency.EUR, Currency.PLN, false, 1.0d),
      "USD/PLN", FxCurrencyPair.of(Currency.USD, Currency.PLN, false, 1.0d),
      "EUR/USD", FxCurrencyPair.of(Currency.EUR, Currency.USD, true, 1.0d),
      "HUF/PLN", FxCurrencyPair.of(Currency.of("HUF"), Currency.PLN, false, 100d),
      "EUR/HUF", FxCurrencyPair.of(Currency.EUR, Currency.of("HUF"), true, 0.0d),
      "XXX/PLN", FxCurrencyPair.of(Currency.of("XXX"), Currency.of("YYY"), false, 100d),
      "YYY/PLN", FxCurrencyPair.of(Currency.of("XXX"), Currency.of("YYY"), false, 10d),
      "XXX/YYY", FxCurrencyPair.of(Currency.of("XXX"), Currency.of("YYY"), true, 0d));

  private static final MarketDataMemoryService<FxRateId, FxRate3.FxRate3Values> fxRateSourceService =
      new MarketDataMemoryService<>();

  @BeforeAll
  public static void setUp() {
    fxRateSourceService.setMarketData(List.of(
        BasicMarketDataHolder
            .of(FxRateId.of("EUR", "PLN"), FxRate3.of(LocalDate.parse("2020-01-01"), 4d, 4.2d, 4.1234567d)),
        BasicMarketDataHolder
            .of(FxRateId.of("USD", "PLN"), FxRate3.of(LocalDate.parse("2020-01-01"), 3.7d, 3.8d, 3.913131313d)),
        BasicMarketDataHolder
            .of(FxRateId.of("HUF", "PLN"), FxRate3.of(LocalDate.parse("2020-01-01"), 1.25d, 1.27d, 1.2631312d)),
        BasicMarketDataHolder
            .of(FxRateId.of("XXX", "PLN"), FxRate3.of(LocalDate.parse("2020-01-01"), 17.3d, 18.1d, 19.25d)),
        BasicMarketDataHolder
            .of(FxRateId.of("YYY", "PLN"), FxRate3.of(LocalDate.parse("2020-01-01"), 40.7d, 41.5d, 42.6d))
    ));
    rateService = new FxRateServiceImpl(
        (ccy1,ccy2)->pairs.get(ccy1+"/"+ccy2),
        fxRateSourceService,
        Currency.PLN,
        DECIMAL_PLACES, OUTPUT_DECIMAL_PLACES);
  }

  private void assertRate3(double buyRate, double sellRate, double avgRate, FxRate3 rate3) {
    assertNotNull(rate3);
    assertEquals(Rate.of(buyRate, OUTPUT_DECIMAL_PLACES).doubleValue(),
        rate3.getValue().getBuyRate().doubleValue(), "Buy rate");
    assertEquals(Rate.of(sellRate, OUTPUT_DECIMAL_PLACES).doubleValue(),
        rate3.getValue().getSellRate().doubleValue(), "Sell rate");
    assertEquals(Rate.of(avgRate, OUTPUT_DECIMAL_PLACES).doubleValue(),
        rate3.getValue().getAvgRate().doubleValue(), "Avg rate");
  }

  private double rate(double rate) {
    return Rate.of(rate, OUTPUT_DECIMAL_PLACES).doubleValue();
  }

  @Test
  public void testSimple() {
    FxRate3 rate3 = null;
    //EUR->PLN
    assertRate3(4.0d, 4.2d, 4.1235d,
        rateService.getRate3(FxRateId.of("EUR", "PLN"), LocalDate.parse("2020-01-01")));
    assertEquals(4.1235d,
        rateService.getRateDouble(FxRateId.of("EUR", "PLN"), LocalDate.parse("2020-01-01"), FxRateType.AVG));
    //PLN->EUR
    assertRate3(1 / 4.0d, 1 / 4.2d, 1 / 4.1235d,
        rateService.getRate3(FxRateId.of("PLN", "EUR"), LocalDate.parse("2020-01-01")));
    assertEquals(Rate.of(1 / 4.1235d, OUTPUT_DECIMAL_PLACES).doubleValue(),
        rateService.getRateDouble(FxRateId.of("PLN", "EUR"), LocalDate.parse("2020-01-01"), FxRateType.AVG));
    //HUF->PLN
    assertEquals(Rate.of(1.2631d / 100, OUTPUT_DECIMAL_PLACES).doubleValue(),
        rateService.getRateDouble(FxRateId.of("HUF", "PLN"), LocalDate.parse("2020-01-01"), FxRateType.AVG));
    assertRate3(1.25d / 100, 1.27d / 100, 1.2631d / 100,
        rateService.getRate3(FxRateId.of("HUF", "PLN"), LocalDate.parse("2020-01-01")));
    //PLN->HUF
    assertEquals(Rate.of(1 / 0.012631d, OUTPUT_DECIMAL_PLACES).doubleValue(),
        rateService.getRateDouble(FxRateId.of("PLN", "HUF"), LocalDate.parse("2020-01-01"), FxRateType.AVG));
    assertRate3(100 * 1 / 1.25d, 100 * 1 / 1.27d, 100 * 1 / 1.2631d,
        rateService.getRate3(FxRateId.of("PLN", "HUF"), LocalDate.parse("2020-01-01")));
  }

  @Test
  public void testCross() {
    //EUR->USD
    assertEquals(Rate.of(4.1235d / 3.9131d, OUTPUT_DECIMAL_PLACES).doubleValue(),
        rateService.getRateDouble(FxRateId.of("EUR", "USD"), LocalDate.parse("2020-01-01"), FxRateType.AVG));
    assertRate3(4.0d / 3.7d, 4.2d / 3.8d, 4.1235d / 3.9131d,
        rateService.getRate3(FxRateId.of("EUR", "USD"), LocalDate.parse("2020-01-01")));
    //USD->EUR
    assertEquals(Rate.of(3.9131d / 4.1235d, OUTPUT_DECIMAL_PLACES).doubleValue(),
        rateService.getRateDouble(FxRateId.of("USD", "EUR"), LocalDate.parse("2020-01-01"), FxRateType.AVG));
    assertRate3(3.7d / 4.0d, 3.8d / 4.2d, 3.9131d / 4.1235d,
        rateService.getRate3(FxRateId.of("USD", "EUR"), LocalDate.parse("2020-01-01")));
    //EUR->HUF
    assertEquals(Rate.of(100 * 4.1235d / 1.2631, OUTPUT_DECIMAL_PLACES).doubleValue(),
        rateService.getRateDouble(FxRateId.of("EUR", "HUF"), LocalDate.parse("2020-01-01"), FxRateType.AVG));
    assertRate3(100 * 4.0d / 1.25d, 100 * 4.2d / 1.27d, 100 * 4.1235d / 1.2631d,
        rateService.getRate3(FxRateId.of("EUR", "HUF"), LocalDate.parse("2020-01-01")));
    //HUF->EUR
    assertEquals(Rate.of(1.2631d / 4.1235d / 100, OUTPUT_DECIMAL_PLACES).doubleValue(),
        rateService.getRateDouble(FxRateId.of("HUF", "EUR"), LocalDate.parse("2020-01-01"), FxRateType.AVG));
    assertRate3(1.25d / 4.0d / 100, 1.27d / 4.2d / 100, 1.2631d / 4.1235d / 100,
        rateService.getRate3(FxRateId.of("HUF", "EUR"), LocalDate.parse("2020-01-01")));
    //XXX->YYY
    assertRate3(0.1d*17.3d / 40.7d, 0.1d*18.1d / 41.5d, 0.1d*19.25d / 42.6d,
        rateService.getRate3(FxRateId.of("XXX", "YYY"), LocalDate.parse("2020-01-01")));
    //YYY->XXX
    assertRate3(10d*40.7d/17.3d, 10d*41.5d/18.1d, 10d*42.6d/19.25d,
        rateService.getRate3(FxRateId.of("YYY", "XXX"), LocalDate.parse("2020-01-01")));
  }

}
