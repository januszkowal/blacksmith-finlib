package org.blacksmith.finlib.rates.marketdata;

import java.time.LocalDate;
import java.util.List;

import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rates.interestrates.InterestRate;
import org.blacksmith.finlib.rates.interestrates.InterestRateId;
import org.blacksmith.finlib.rates.marketdata.BasicMarketDataWrapper;
import org.blacksmith.finlib.rates.marketdata.MarketData;
import org.blacksmith.finlib.rates.marketdata.MarketDataMemoryService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MarketDataMemoryServiceTest {

  static final MarketDataInMemoryProvider<InterestRateId, Rate> interestRateService =
      new MarketDataInMemoryProvider<>();

  @BeforeAll
  public static void setUp() {
    interestRateService.setMarketData(List.of(
        BasicMarketDataWrapper
            .of(InterestRateId.of("WIBOR", "6M", Currency.of("EUR")),
                InterestRate.of(LocalDate.parse("2019-12-01"), Rate.of(3.0d))),
        BasicMarketDataWrapper
            .of(InterestRateId.of("WIBOR", "6M", Currency.of("EUR")),
                InterestRate.of(LocalDate.parse("2020-01-01"), Rate.of(3.1d))),
        BasicMarketDataWrapper
            .of(InterestRateId.of("EURIBOR", "3M", Currency.of("EUR")),
                InterestRate.of(LocalDate.parse("2020-01-02"), Rate.of(3.4d)))
    ));
  }

  @Test
  public void test1() {
    MarketData<Rate> val1 = interestRateService.getRate(InterestRateId.of("WIBOR", "6M", Currency.of("EUR")),
        LocalDate.parse("2020-01-15"));
    MarketData<Rate> val2 = interestRateService.getRate(InterestRateId.of("EURIBOR", "3M", Currency.of("EUR")),
        LocalDate.parse("2020-01-15"));
    assertThat(val1.getValue().doubleValue()).isEqualTo(3.1d);
    assertThat(val2.getValue().doubleValue()).isEqualTo(3.4d);
  }
}