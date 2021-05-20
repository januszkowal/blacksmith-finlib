package org.blacksmith.finlib.rates;

import java.time.LocalDate;
import java.util.List;

import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rates.basic.BasicMarketDataHolder;
import org.blacksmith.finlib.rates.basic.MarketDataMemoryService;
import org.blacksmith.finlib.rates.interestrates.InterestRate;
import org.blacksmith.finlib.rates.interestrates.InterestRateId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MarketDataMemoryServiceTest {

  static final MarketDataMemoryService<InterestRateId, Rate> interestRateService =
      new MarketDataMemoryService<>();

  @BeforeAll
  public static void setUp() {
    interestRateService.setMarketData(List.of(
        BasicMarketDataHolder
            .of(InterestRateId.of("WIBOR", "6M", Currency.of("EUR")),
                InterestRate.of(LocalDate.parse("2019-12-01"), Rate.of(3.0d))),
        BasicMarketDataHolder
            .of(InterestRateId.of("WIBOR", "6M", Currency.of("EUR")),
                InterestRate.of(LocalDate.parse("2020-01-01"), Rate.of(3.1d))),
        BasicMarketDataHolder
            .of(InterestRateId.of("EURIBOR", "3M", Currency.of("EUR")),
                InterestRate.of(LocalDate.parse("2020-01-02"), Rate.of(3.4d)))
    ));
  }

  @Test
  public void test1() {
    MarketData<Rate> val1 = interestRateService
        .getRate(InterestRateId.of("WIBOR", "6M", Currency.of("EUR")), LocalDate.parse("2020-01-15"));
    MarketData<Rate> val2 = interestRateService
        .getRate(InterestRateId.of("EURIBOR", "3M", Currency.of("EUR")), LocalDate.parse("2020-01-15"));
    assertEquals(3.1d, val1.getValue().doubleValue());
    assertEquals(3.4d, val2.getValue().doubleValue());
  }
}
