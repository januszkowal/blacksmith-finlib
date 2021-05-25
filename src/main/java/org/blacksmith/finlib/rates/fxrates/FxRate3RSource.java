package org.blacksmith.finlib.rates.fxrates;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rates.marketdata.MarketData;

import lombok.Getter;
import lombok.ToString;
import lombok.Value;

@ToString
@Getter
public class FxRate3RSource implements MarketData<FxRate3RSource.FxRate3RawValue> {
  private final LocalDate date;
  private final FxRate3RawValue value;

  public FxRate3RSource(LocalDate date, FxRate3RawValue value) {
    ArgChecker.notNull(date, "Date must be not null");
    ArgChecker.notNull(value, "Value must be not null");
    this.date = date;
    this.value = value;
  }

  public static FxRate3RSource of(LocalDate date, Rate buy, Rate sell, Rate avg) {
    return new FxRate3RSource(date, FxRate3RawValue.of(buy, sell, avg));
  }

  public static FxRate3RSource of(LocalDate date, BigDecimal buy, BigDecimal sell, BigDecimal avg, int decimalPlaces) {
    return new FxRate3RSource(date, FxRate3RawValue.of(Rate.of(buy, decimalPlaces), Rate.of(sell, decimalPlaces), Rate.of(avg)));
  }

  public static FxRate3RSource of(LocalDate date, double buyRate, double sellRate, double avgRate, int decimalPlaces) {
    return new FxRate3RSource(date, FxRate3RawValue
        .of(Rate.of(buyRate, decimalPlaces), Rate.of(sellRate, decimalPlaces), Rate.of(avgRate, decimalPlaces)));
  }

  @Value(staticConstructor = "of")
  public static class FxRate3RawValue {
    private final Rate buy;
    private final Rate sell;
    private final Rate avg;
  }
}
