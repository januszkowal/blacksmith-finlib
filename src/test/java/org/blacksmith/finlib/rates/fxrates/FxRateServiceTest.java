package org.blacksmith.finlib.rates.fxrates;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rates.BasicMarketDataWrapper;
import org.blacksmith.finlib.rates.MarketDataMemoryService;
import org.blacksmith.finlib.rates.fxccypair.FxCurrencyPair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FxRateServiceTest {
  static final int DECIMAL_PLACES = 4;
  static final int OUTPUT_DECIMAL_PLACES = 6;
  private static final Map<String, FxCurrencyPair> pairs = List.of(
      FxCurrencyPair.of(Currency.EUR, Currency.PLN, false, 1.0d),
      FxCurrencyPair.of(Currency.USD, Currency.PLN, false, 1.0d),
      FxCurrencyPair.of(Currency.EUR, Currency.USD, true, 1.0d),
      FxCurrencyPair.of(Currency.of("HUF"), Currency.PLN, false, 100d),
      FxCurrencyPair.of(Currency.EUR, Currency.of("HUF"), true, 1.0d))
      .stream()
      .collect(Collectors.toMap(pair -> pair.getBase().getCurrencyCode() + "/" + pair.getCounter().getCurrencyCode(), pair -> pair));
  static LocalDate dateFail = LocalDate.parse("2019-12-31");
  static LocalDate date1 = LocalDate.parse("2020-01-01");
  static LocalDate date2 = LocalDate.parse("2020-01-02");
  static LocalDate date3 = LocalDate.parse("2020-01-03");
  private static FxRateService rateService;

  @BeforeAll
  public static void setUp() {
    MarketDataMemoryService<FxRateId, FxRate3Raw.FxRateRaw3Data> fxRateSourceService =
        new MarketDataMemoryService<>(List.of(
            BasicMarketDataWrapper.of(FxRateId.of("EUR", "PLN"),
                FxRate3Raw.of(date1, 4.4439d, 4.5337d, 4.4888d, DECIMAL_PLACES)),
            BasicMarketDataWrapper.of(FxRateId.of("EUR", "PLN"),
                FxRate3Raw.of(date2, 4.45d, 4.54d, 4.495d, DECIMAL_PLACES)),
            BasicMarketDataWrapper.of(FxRateId.of("EUR", "PLN"),
                FxRate3Raw.of(date3, 4.44d, 4.53d, 4.485d, DECIMAL_PLACES)),
            BasicMarketDataWrapper.of(FxRateId.of("USD", "PLN"),
                FxRate3Raw.of(date1, 3.6471d, 3.7207d, 3.6839d, DECIMAL_PLACES)),
            BasicMarketDataWrapper.of(FxRateId.of("HUF", "PLN"),
                FxRate3Raw.of(date1, 1.2653d, 1.2909d, 1.2781d, DECIMAL_PLACES))
            ));
    rateService = new FxRateServiceImpl(Currency.PLN, (ccy1, ccy2) -> pairs.get(ccy1 + "/" + ccy2),
        fxRateSourceService, OUTPUT_DECIMAL_PLACES);
  }

  @Test
  public void shouldRetrieveSimpleRates() {
    //EUR->PLN
    assertRate3(4.4439d, 4.5337d, 4.4888d, "EUR/PLN", date1, "EUR/PLN-3");
    assertThat(4.4439d).describedAs("EUR/PLN-buy")
        .isEqualTo(rateService.getRateDouble(FxRateId.of("EUR/PLN"), date1, FxRateType.BUY));
    assertThat(4.5337d).describedAs("EUR/PLN-buy")
        .isEqualTo(rateService.getRateDouble(FxRateId.of("EUR/PLN"), date1, FxRateType.SELL));
    assertThat(4.4888d).describedAs("EUR/PLN-avg")
        .isEqualTo(rateService.getRateDouble(FxRateId.of("EUR/PLN"), date1, FxRateType.AVG));
    //PLN->EUR
    assertRate3(1 / 4.4439d, 1 / 4.5337d, 1 / 4.4888d, "PLN/EUR", date1, "PLN/EUR-3");
    assertThat(Rate.of(1 / 4.4439d, OUTPUT_DECIMAL_PLACES).doubleValue()).describedAs("PLN/EUR")
        .isEqualTo(rateService.getRateDouble(FxRateId.of("PLN/EUR"), date1, FxRateType.BUY));
    assertThat(Rate.of(1 / 4.5337d, OUTPUT_DECIMAL_PLACES).doubleValue()).describedAs("PLN/EUR")
        .isEqualTo(rateService.getRateDouble(FxRateId.of("PLN/EUR"), date1, FxRateType.SELL));
    assertThat(Rate.of(1 / 4.4888d, OUTPUT_DECIMAL_PLACES).doubleValue()).describedAs("PLN/EUR")
        .isEqualTo(rateService.getRateDouble(FxRateId.of("PLN/EUR"), date1, FxRateType.AVG));
    //HUF->PLN
    assertRate3(1.2653d / 100, 1.2909d / 100, 1.2781d / 100, "HUF/PLN", date1, "HUF/PLN-3");
    assertThat(Rate.of(1.2653d / 100, OUTPUT_DECIMAL_PLACES).doubleValue()).describedAs("HUF/PLN-buy")
        .isEqualTo(rateService.getRateDouble(FxRateId.of("HUF/PLN"), date1, FxRateType.BUY));
    assertThat(Rate.of(1.2909d / 100, OUTPUT_DECIMAL_PLACES).doubleValue()).describedAs("HUF/PLN-sell")
        .isEqualTo(rateService.getRateDouble(FxRateId.of("HUF/PLN"), date1, FxRateType.SELL));
    assertThat(Rate.of(1.2781d / 100, OUTPUT_DECIMAL_PLACES).doubleValue()).describedAs("HUF/PLN-avg")
        .isEqualTo(rateService.getRateDouble(FxRateId.of("HUF/PLN"), date1, FxRateType.AVG));
    //PLN->HUF
    assertRate3(100 / 1.2653d, 100 / 1.2909d, 100 / 1.2781d, "PLN/HUF", date1, "PLN/HUF-3");
    assertThat(Rate.of(100 / 1.2653d, OUTPUT_DECIMAL_PLACES).doubleValue()).describedAs("PLN/HUF-buy")
        .isEqualTo(rateService.getRateDouble(FxRateId.of("PLN/HUF"), date1, FxRateType.BUY));
    assertThat(Rate.of(100 / 1.2909d, OUTPUT_DECIMAL_PLACES).doubleValue()).describedAs("PLN/HUF-sell")
        .isEqualTo(rateService.getRateDouble(FxRateId.of("PLN/HUF"), date1, FxRateType.SELL));
    assertThat(Rate.of(100 / 1.2781d, OUTPUT_DECIMAL_PLACES).doubleValue()).describedAs("PLN/HUF-avg")
        .isEqualTo(rateService.getRateDouble(FxRateId.of("PLN/HUF"), date1, FxRateType.AVG));
  }

  @Test
  public void shouldRetrieveCrossRates() {
    //EUR->USD
    assertRate3(4.4439d / 3.6471d, 4.5337d / 3.7207d, 4.4888d / 3.6839d, "EUR/USD", date1, "EUR/USD-3");
    assertThat(Rate.of(4.4439d / 3.6471d, OUTPUT_DECIMAL_PLACES).doubleValue()).describedAs("EUR/USD-buy")
        .isEqualTo(rateService.getRateDouble(FxRateId.of("EUR/USD"), date1, FxRateType.BUY));
    assertThat(Rate.of(4.5337d / 3.7207d, OUTPUT_DECIMAL_PLACES).doubleValue()).describedAs("EUR/USD-sell")
        .isEqualTo(rateService.getRateDouble(FxRateId.of("EUR/USD"), date1, FxRateType.SELL));
    assertThat(Rate.of(4.4888d / 3.6839d, OUTPUT_DECIMAL_PLACES).doubleValue()).describedAs("EUR/USD-avg")
        .isEqualTo(rateService.getRateDouble(FxRateId.of("EUR/USD"), date1, FxRateType.AVG));
    //USD->EUR
    assertRate3(3.6471d / 4.4439d, 3.7207d / 4.5337d, 3.6839d / 4.4888d, "USD/EUR", date1, "EUR/USD-3");
    assertThat(Rate.of(3.6471d / 4.4439d, OUTPUT_DECIMAL_PLACES).doubleValue()).describedAs("USD/EUR-buy")
        .isEqualTo(rateService.getRateDouble(FxRateId.of("USD/EUR"), date1, FxRateType.BUY));
    assertThat(Rate.of(3.7207d / 4.5337d, OUTPUT_DECIMAL_PLACES).doubleValue()).describedAs("USD/EUR-sell")
        .isEqualTo(rateService.getRateDouble(FxRateId.of("USD/EUR"), date1, FxRateType.SELL));
    assertThat(Rate.of(3.6839d / 4.4888d, OUTPUT_DECIMAL_PLACES).doubleValue()).describedAs("USD/EUR-avg")
        .isEqualTo(rateService.getRateDouble(FxRateId.of("USD/EUR"), date1, FxRateType.AVG));
    //EUR->HUF
    assertRate3(100 * 4.4439d / 1.2653d, 100 * 4.5337d / 1.2909d, 100 * 4.4888d / 1.2781d, "EUR/HUF", date1, "EUR/HUF-3");
    assertThat(Rate.of(100 * 4.4439d / 1.2653d, OUTPUT_DECIMAL_PLACES).doubleValue()).describedAs("EUR/HUF-buy")
        .isEqualTo(rateService.getRateDouble(FxRateId.of("EUR/HUF"), date1, FxRateType.BUY));
    assertThat(Rate.of(100 * 4.5337d / 1.2909d, OUTPUT_DECIMAL_PLACES).doubleValue()).describedAs("EUR/HUF-sell")
        .isEqualTo(rateService.getRateDouble(FxRateId.of("EUR/HUF"), date1, FxRateType.SELL));
    assertThat(Rate.of(100 * 4.4888d / 1.2781d, OUTPUT_DECIMAL_PLACES).doubleValue()).describedAs("EUR/HUF-avg")
        .isEqualTo(rateService.getRateDouble(FxRateId.of("EUR/HUF"), date1, FxRateType.AVG));
    //HUF->EUR
    assertRate3(1.2653d / 4.4439d / 100, 1.2909d / 4.5337d / 100, 1.2781d / 4.4888d / 100, "HUF/EUR", date1, "HUF/EUR-3");
    assertThat(Rate.of(1.2781d / 4.4888d / 100, OUTPUT_DECIMAL_PLACES).doubleValue()).describedAs("HUF/EUR")
        .isEqualTo(rateService.getRateDouble(FxRateId.of("HUF/EUR"), date1, FxRateType.AVG));
  }

  @Test
  public void shouldGetRatesFromDifferentDates() {
    assertRate3(date1, date1,4.4439d, 4.5337d, 4.4888d, "EUR/PLN", "EUR/PLN date1");
    assertRate3(date2, date2,4.45d, 4.54d, 4.495d, "EUR/PLN", "EUR/PLN date2");
    assertRate3(date3, date3,4.44d, 4.53d, 4.485d, "EUR/PLN", "EUR/PLN date3");
    assertRate3(date3.plusDays(1), date3,4.44d, 4.53d, 4.485d, "EUR/PLN", "EUR/PLN date3");
  }

  @Test
  public void shouldFail() {
    assertThrows(IllegalArgumentException.class, () -> rateService.getRateDouble(FxRateId.of("EUR", "USD"), dateFail, FxRateType.AVG),
        "No available rate EUR/PLN on 2019-12-31");
    assertThrows(IllegalArgumentException.class, () -> rateService.getRateDouble(FxRateId.of("EUR", "PLN"), dateFail, FxRateType.AVG),
        "No available rate EUR/PLN on 2019-12-31");
  }

  private void assertRate3(double buyRate, double sellRate, double avgRate, String pair, LocalDate date, String description) {
    var rate3 = rateService.getRate3(FxRateId.of(pair), date);
    assertNotNull(rate3, description);
    assertThat(rate3.getValue()).describedAs(description)
        .extracting(FxRate3.FxRate3Data::getBuy, FxRate3.FxRate3Data::getSell, FxRate3.FxRate3Data::getAvg)
        .containsExactly(Rate.of(buyRate, OUTPUT_DECIMAL_PLACES), Rate.of(sellRate, OUTPUT_DECIMAL_PLACES),
            Rate.of(avgRate, OUTPUT_DECIMAL_PLACES));
  }

  private void assertRate3(LocalDate date, LocalDate expectedDate, double buyRate, double sellRate, double avgRate, String pair, String description) {
    var rate3 = rateService.getRate3(FxRateId.of(pair), date);
    assertNotNull(rate3, description);
    assertThat(rate3).describedAs(description)
        .extracting(FxRate3::getDate, r3 -> r3.getValue().getBuy(), r3 -> r3.getValue().getSell(), r3 -> r3.getValue().getAvg())
        .containsExactly(expectedDate, Rate.of(buyRate, OUTPUT_DECIMAL_PLACES), Rate.of(sellRate, OUTPUT_DECIMAL_PLACES),
            Rate.of(avgRate, OUTPUT_DECIMAL_PLACES));
  }

  private double rate(double rate) {
    return Rate.of(rate, OUTPUT_DECIMAL_PLACES).doubleValue();
  }

}
