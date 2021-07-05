package org.blacksmith.finlib.rate.marketdata;

import java.time.LocalDate;
import java.util.List;

import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.marketdata.MarketData;
import org.blacksmith.finlib.marketdata.MarketDataInMemoryProvider;
import org.blacksmith.finlib.rate.intrate.InterestRate;
import org.blacksmith.finlib.rate.intrate.InterestRateId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MarketDataMemoryServiceTest {

  static final MarketDataInMemoryProvider<InterestRateId, InterestRate> interestRateService =
      new MarketDataInMemoryProvider<>();

  @BeforeAll
  public static void setUp() {
    interestRateService.setMarketData(List.of(
        BasicMarketDataWrapper
            .of(InterestRateId.of("WIBOR", "6M", Currency.of("EUR")),
                InterestRate.ofRate(Rate.of(3.0d), LocalDate.parse("2019-12-01"))),
        BasicMarketDataWrapper
            .of(InterestRateId.of("WIBOR", "6M", Currency.of("EUR")),
                InterestRate.ofRate(Rate.of(3.1d), LocalDate.parse("2020-01-01"))),
        BasicMarketDataWrapper
            .of(InterestRateId.of("EURIBOR", "3M", Currency.of("EUR")),
                InterestRate.ofRate(Rate.of(3.4d), LocalDate.parse("2020-01-02")))
    ));
  }

  @Test
  public void test1() {
    InterestRate val1 = interestRateService.get(InterestRateId.of("WIBOR", "6M", Currency.of("EUR")),
        LocalDate.parse("2020-01-15"));
    InterestRate val2 = interestRateService.get(InterestRateId.of("EURIBOR", "3M", Currency.of("EUR")),
        LocalDate.parse("2020-01-15"));
    assertThat(val1.getValue().doubleValue()).isEqualTo(3.1d);
    assertThat(val2.getValue().doubleValue()).isEqualTo(3.4d);
  }
}
