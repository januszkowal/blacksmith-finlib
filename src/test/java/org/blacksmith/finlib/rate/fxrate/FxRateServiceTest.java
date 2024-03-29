package org.blacksmith.finlib.rate.fxrate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.marketdata.MarketDataInMemoryProvider;
import org.blacksmith.finlib.rate.fxccypair.CurrencyPairExt;
import org.blacksmith.finlib.rate.fxrate.impl.FxRateServiceImpl;
import org.blacksmith.finlib.rate.marketdata.BasicMarketDataWrapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FxRateServiceTest {
  static final int DECIMAL_PLACES = 4;
  static final int OUTPUT_DECIMAL_PLACES = 6;
  private static final Map<String, CurrencyPairExt> pairs = List.of(
      CurrencyPairExt.ofDirect(Currency.EUR, Currency.PLN, false, 1.0d),
      CurrencyPairExt.ofDirect(Currency.USD, Currency.PLN, false, 1.0d),
      CurrencyPairExt.ofDirect(Currency.EUR, Currency.USD, true, 1.0d),
      CurrencyPairExt.ofDirect(Currency.of("HUF"), Currency.PLN, false, 100d),
      CurrencyPairExt.ofDirect(Currency.EUR, Currency.of("HUF"), true, 1.0d))
      .stream()
      .collect(Collectors.toMap(pair -> pair.getBase().getCurrencyCode() + "/" + pair.getCounter().getCurrencyCode(), pair -> pair));
  static final LocalDate dateFail = LocalDate.parse("2019-12-31");
  static final LocalDate date1 = LocalDate.parse("2020-01-01");
  static final LocalDate date2 = LocalDate.parse("2020-01-02");
  static final LocalDate date3 = LocalDate.parse("2020-01-03");
  private static FxRateService rateService;

  @BeforeAll
  public static void setUp() {
    MarketDataInMemoryProvider<FxRateId, FxRate3> fxRateProvider = new MarketDataInMemoryProvider();
    fxRateProvider.setMarketData(List.of(
        BasicMarketDataWrapper.of(FxRateId.of("EUR", "PLN"),
            FxRate3.of(4.4439d, 4.5337d, 4.4888d, DECIMAL_PLACES, date1)),
        BasicMarketDataWrapper.of(FxRateId.of("EUR", "PLN"),
            FxRate3.of(4.45d, 4.54d, 4.495d, DECIMAL_PLACES, date2)),
        BasicMarketDataWrapper.of(FxRateId.of("EUR", "PLN"),
            FxRate3.of(4.44d, 4.53d, 4.485d, DECIMAL_PLACES, date3)),
        BasicMarketDataWrapper.of(FxRateId.of("USD", "PLN"),
            FxRate3.of(3.6471d, 3.7207d, 3.6839d, DECIMAL_PLACES, date1)),
        BasicMarketDataWrapper.of(FxRateId.of("HUF", "PLN"),
            FxRate3.of(1.2653d, 1.2909d, 1.2781d, DECIMAL_PLACES, date1))
    ));
    rateService = new FxRateServiceImpl(Currency.PLN, (ccy1, ccy2) -> pairs.get(ccy1 + "/" + ccy2), fxRateProvider, OUTPUT_DECIMAL_PLACES);
  }

  @Test
  public void shouldRetrieveSimpleRates() {
    //EUR->PLN
    assertRate3(4.4439d, 4.5337d, 4.4888d, "EUR/PLN", date1, "EUR/PLN-3");
    //PLN->EUR
    assertRate3(1 / 4.4439d, 1 / 4.5337d, 1 / 4.4888d, "PLN/EUR", date1, "PLN/EUR-3");
    //HUF->PLN
    assertRate3(1.2653d / 100, 1.2909d / 100, 1.2781d / 100, "HUF/PLN", date1, "HUF/PLN-3");
    //PLN->HUF
    assertRate3(100 / 1.2653d, 100 / 1.2909d, 100 / 1.2781d, "PLN/HUF", date1, "PLN/HUF-3");
  }

  @Test
  public void shouldRetrieveCrossRates() {
    //EUR->USD
    assertRate3(4.4439d / 3.6471d, 4.5337d / 3.7207d, 4.4888d / 3.6839d, "EUR/USD", date1, "EUR/USD-3");
    //USD->EUR
    assertRate3(3.6471d / 4.4439d, 3.7207d / 4.5337d, 3.6839d / 4.4888d, "USD/EUR", date1, "EUR/USD-3");
    //EUR->HUF
    assertRate3(100 * 4.4439d / 1.2653d, 100 * 4.5337d / 1.2909d, 100 * 4.4888d / 1.2781d, "EUR/HUF", date1, "EUR/HUF-3");
    //HUF->EUR
    assertRate3(1.2653d / 4.4439d / 100, 1.2909d / 4.5337d / 100, 1.2781d / 4.4888d / 100, "HUF/EUR", date1, "HUF/EUR-3");
  }

  @Test
  public void shouldGetRatesFromDifferentDates() {
    assertRate3WithDate(date1, date1, 4.4439d, 4.5337d, 4.4888d, "EUR/PLN", "EUR/PLN date1");
    assertRate3WithDate(date2, date2, 4.45d, 4.54d, 4.495d, "EUR/PLN", "EUR/PLN date2");
    assertRate3WithDate(date3, date3, 4.44d, 4.53d, 4.485d, "EUR/PLN", "EUR/PLN date3");
    assertRate3WithDate(date3.plusDays(1), date3, 4.44d, 4.53d, 4.485d, "EUR/PLN", "EUR/PLN date3");
  }

  @Test
  public void shouldFail() {
    assertThrows(IllegalArgumentException.class, () -> rateService.fxRateDouble(FxRateId.of("EUR", "USD"), dateFail, FxRateType.AVG),
        "No available rate EUR/PLN on 2019-12-31");
    assertThrows(IllegalArgumentException.class, () -> rateService.fxRateDouble(FxRateId.of("EUR", "PLN"), dateFail, FxRateType.AVG),
        "No available rate EUR/PLN on 2019-12-31");
  }

  private void assertRate3(double buyRate, double sellRate, double avgRate, String pair, LocalDate date, String description) {
    var fxRateId = FxRateId.of(pair);
//    var rate3 = rateService.fxRate3(fxRateId, date);
//    assertNotNull(rate3, description);
//    assertThat(rate3.getValue()).describedAs(description)
//        .extracting(FxRate3.Data::getBuy, FxRate3.Data::getSell, FxRate3.Data::getAvg)
//        .containsExactly(Rate.of(buyRate, OUTPUT_DECIMAL_PLACES), Rate.of(sellRate, OUTPUT_DECIMAL_PLACES),
//            Rate.of(avgRate, OUTPUT_DECIMAL_PLACES));
    assertRate1(buyRate, fxRateId, date, FxRateType.BUY, description + "-buy");
//    assertRate1(sellRate, fxRateId, date, FxRateType.SELL, description + "-sell");
//    assertRate1(avgRate, fxRateId, date, FxRateType.AVG, description + "-avg");
  }

  private void assertRate1(double rate, FxRateId fxRateId, LocalDate date, FxRateType type, String description) {
    var rate1 = rateService.fxRate(fxRateId, date, type);
    assertNotNull(rate1, description);
    assertThat(rate1.getValue()).describedAs(description)
        .isEqualTo(Rate.of(rate, OUTPUT_DECIMAL_PLACES));
  }

  private void assertRate3WithDate(LocalDate date, LocalDate expectedDate, double buyRate, double sellRate, double avgRate, String pair,
      String description) {
    var rate3 = rateService.fxRate3(FxRateId.of(pair), date);
    assertNotNull(rate3, description);
    assertThat(rate3).describedAs(description)
        .extracting(FxRate3::getDate, r3 -> r3.getValue().getBuy(), r3 -> r3.getValue().getSell(), r3 -> r3.getValue().getAvg())
        .containsExactly(expectedDate, Rate.of(buyRate, OUTPUT_DECIMAL_PLACES), Rate.of(sellRate, OUTPUT_DECIMAL_PLACES),
            Rate.of(avgRate, OUTPUT_DECIMAL_PLACES));
  }
}
