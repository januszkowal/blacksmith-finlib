package org.blacksmith.finlib.rates.fxrates;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Value;
import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rates.MarketData;
import org.blacksmith.finlib.rates.basic.BasicMarketData;
import org.blacksmith.finlib.rates.fxrates.FxRate3.FxRateValues;

public class FxRate3 extends BasicMarketData<FxRateValues> implements FxRateOperations<FxRate3> {

  public FxRate3(LocalDate date, FxRateValues rate) {
    super(date, rate);
  }

  public static FxRate3 of(LocalDate date, FxRateValues rate) {
    return new FxRate3(date, new FxRateValues(
        Rate.of(rate.buyRate),
        Rate.of(rate.sellRate),
        Rate.of(rate.avgRate)));
  }

  public static FxRate3 of(LocalDate date, FxRateValues rate, int decimalPlaces) {
    return new FxRate3(date, new FxRateValues(
        Rate.of(rate.buyRate, decimalPlaces),
        Rate.of(rate.sellRate, decimalPlaces),
        Rate.of(rate.avgRate, decimalPlaces)));
  }

    public static FxRate3 of (LocalDate date, Rate buyRate, Rate sellRate, Rate avgRate) {
    return new FxRate3(date,new FxRateValues(buyRate,sellRate,avgRate));
  }

  public static FxRate3 of (LocalDate date, double buyRate, double sellRate, double avgRate) {
    return new FxRate3(date,new FxRateValues(Rate.of(buyRate),Rate.of(sellRate),Rate.of(avgRate)));
  }

  public static FxRate3 of(LocalDate date, double buyRate, double sellRate, double avgRate, int decimalPlaces) {
    return new FxRate3(date, new FxRateValues(Rate.of(buyRate, decimalPlaces), Rate.of(sellRate, decimalPlaces),
        Rate.of(avgRate, decimalPlaces)));
  }

  public static FxRate3 of (LocalDate date, BigDecimal buyRate, BigDecimal sellRate, BigDecimal avgRate) {
    return new FxRate3(date,new FxRateValues(Rate.of(buyRate),Rate.of(sellRate),Rate.of(avgRate)));
  }

  public static FxRate3 of (MarketData<FxRateValues> marketData) {
    return FxRate3.of(marketData.getDate(),marketData.getValue());
  }

  public FxRate3 inverse() {
    return new FxRate3(this.date, this.value.inverse());
  }

  @Override
  public FxRate3 multiply(double multiplicand, int decimalPlaces) {
    return new FxRate3(this.date, this.value.multiply(multiplicand, decimalPlaces));
  }

  @Override
  public FxRate3 divide(double divisor, int decimalPlaces) {
    return new FxRate3(this.date, this.value.divide(divisor, decimalPlaces));
  }

  @Override
  public FxRate3 crossDivide(double factor, FxRate3 divisor, int decimalPlaces) {
    return FxRate3.of(DateUtils.max(this.date, divisor.date),
        (this.value.buyRate.doubleValue() * factor) / divisor.value.buyRate.doubleValue(),
        (this.value.sellRate.doubleValue() * factor) / divisor.value.sellRate.doubleValue(),
        (this.value.avgRate.doubleValue() * factor) / divisor.value.avgRate.doubleValue(),
        decimalPlaces);
  }

  @Value(staticConstructor = "of")
  public static class FxRateValues {

    Rate buyRate;
    Rate sellRate;
    Rate avgRate;

    public FxRateValues(Rate buyRate, Rate sellRate, Rate avgRate) {
      ArgChecker.notNull(buyRate);
      ArgChecker.notNull(sellRate);
      ArgChecker.notNull(avgRate);
      this.buyRate = buyRate;
      this.sellRate = sellRate;
      this.avgRate = avgRate;
    }

    public static FxRateValues of(double buyRate, double sellRate, double avgRate) {
      return new FxRateValues(Rate.of(buyRate), Rate.of(sellRate), Rate.of(avgRate));
    }

    public static FxRateValues of(double buyRate, double sellRate, double avgRate, int decimalPlaces) {
      return new FxRateValues(Rate.of(buyRate, decimalPlaces), Rate.of(sellRate, decimalPlaces),
          Rate.of(avgRate, decimalPlaces));
    }

    public FxRateValues multiply(double multiplicand, int decimalPlaces) {
      return new FxRateValues(
          Rate.of(this.buyRate.doubleValue() * multiplicand, decimalPlaces),
          Rate.of(this.sellRate.doubleValue() * multiplicand, decimalPlaces),
          Rate.of(this.avgRate.doubleValue() * multiplicand, decimalPlaces));
    }

    public FxRateValues divide(double divisor, int decimalPlaces) {
      return new FxRateValues(
          Rate.of(this.buyRate.doubleValue() / divisor, decimalPlaces),
          Rate.of(this.sellRate.doubleValue() / divisor, decimalPlaces),
          Rate.of(this.avgRate.doubleValue() / divisor, decimalPlaces));
    }

    public FxRateValues inverse() {
      return new FxRateValues(buyRate.inverse(), sellRate.inverse(), avgRate.inverse());
    }
  }

}
