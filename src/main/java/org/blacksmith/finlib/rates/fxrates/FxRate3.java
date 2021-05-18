package org.blacksmith.finlib.rates.fxrates;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rates.MarketData;
import org.blacksmith.finlib.rates.basic.BasicMarketData;

import lombok.Value;

public class FxRate3 extends BasicMarketData<FxRate3.FxRate3Values> implements FxRateOperations<FxRate3> {

  public FxRate3(LocalDate date, FxRate3Values rate3Values) {
    super(date, rate3Values);
  }

  public static FxRate3 of(LocalDate date, FxRate3Values rate3Values) {
    return new FxRate3(date, rate3Values);
  }

  public static FxRate3 of(LocalDate date, FxRate3Values rate, int decimalPlaces) {
    return new FxRate3(date, new FxRate3Values(Rate.of(rate.buyRate, decimalPlaces),
        Rate.of(rate.sellRate, decimalPlaces),
        Rate.of(rate.avgRate, decimalPlaces)));
  }

  public static FxRate3 of(LocalDate date, Rate buyRate, Rate sellRate, Rate avgRate) {
    return new FxRate3(date, new FxRate3Values(buyRate, sellRate, avgRate));
  }

  public static FxRate3 of(LocalDate date, double buyRate, double sellRate, double avgRate) {
    return new FxRate3(date, new FxRate3Values(Rate.of(buyRate), Rate.of(sellRate), Rate.of(avgRate)));
  }

  public static FxRate3 of(LocalDate date, double buyRate, double sellRate, double avgRate, int decimalPlaces) {
    return new FxRate3(date, new FxRate3Values(Rate.of(buyRate, decimalPlaces), Rate.of(sellRate, decimalPlaces),
        Rate.of(avgRate, decimalPlaces)));
  }

  public static FxRate3 of(LocalDate date, BigDecimal buyRate, BigDecimal sellRate, BigDecimal avgRate) {
    return new org.blacksmith.finlib.rates.fxrates.FxRate3(date, new FxRate3Values(Rate.of(buyRate), Rate.of(sellRate), Rate.of(avgRate)));
  }

  public static FxRate3 of(MarketData<FxRate3> marketData) {
    return FxRate3.of(marketData.getDate(), marketData.getValue().getValue());
  }

  public FxRate3 inverse() {
    return new FxRate3(this.date, this.value.inverse());
  }

  @Override
  public FxRate3 add(double augend, int decimalPlaces) {
    return new FxRate3(this.date, this.value.add(augend, decimalPlaces));
  }

  @Override
  public FxRate3 add(double augend) {
    return new FxRate3(this.date, this.value.add(augend));
  }

  @Override
  public FxRate3 add(BigDecimal augend, int decimalPlaces) {
    return new FxRate3(this.date, this.value.add(augend, decimalPlaces));
  }

  @Override
  public FxRate3 add(BigDecimal augend) {
    return new FxRate3(this.date, this.value.multiply(augend));
  }

  @Override
  public FxRate3 multiply(double multiplicand, int decimalPlaces) {
    return new FxRate3(this.date, this.value.multiply(multiplicand, decimalPlaces));
  }

  @Override
  public FxRate3 multiply(double multiplicand) {
    return new FxRate3(this.date, this.value.multiply(multiplicand));
  }

  @Override
  public FxRate3 multiply(BigDecimal multiplicand, int decimalPlaces) {
    return new FxRate3(this.date, this.value.multiply(multiplicand, decimalPlaces));
  }

  @Override
  public FxRate3 multiply(BigDecimal multiplicand) {
    return new FxRate3(this.date, this.value.multiply(multiplicand));
  }

  @Override
  public FxRate3 divide(double divisor, int decimalPlaces) {
    return new FxRate3(this.date, this.value.divide(divisor, decimalPlaces));
  }

  @Override
  public FxRate3 divide(double divisor) {
    return new FxRate3(this.date, this.value.divide(divisor));
  }

  @Override
  public FxRate3 divide(BigDecimal divisor, int decimalPlaces) {
    return new FxRate3(this.date, this.value.divide(divisor, decimalPlaces));
  }

  @Override
  public FxRate3 divide(BigDecimal divisor) {
    return new FxRate3(this.date, this.value.divide(divisor));
  }

  @Override
  public FxRate3 crossDivide(double factor, FxRate3 divisor, int decimalPlaces) {
    return FxRate3.of(DateUtils.max(this.date, divisor.date),
        (this.value.buyRate.doubleValue() * factor) / divisor.value.buyRate.doubleValue(),
        (this.value.sellRate.doubleValue() * factor) / divisor.value.sellRate.doubleValue(),
        (this.value.avgRate.doubleValue() * factor) / divisor.value.avgRate.doubleValue(),
        decimalPlaces);
  }

  @Override
  public FxRate3 crossDivide(BigDecimal factor, FxRate3 divisor, int decimalPlaces) {
    return FxRate3.of(DateUtils.max(this.date, divisor.date),
        (this.value.buyRate.doubleValue() * factor.doubleValue()) / divisor.value.buyRate.doubleValue(),
        (this.value.sellRate.doubleValue() * factor.doubleValue()) / divisor.value.sellRate.doubleValue(),
        (this.value.avgRate.doubleValue() * factor.doubleValue()) / divisor.value.avgRate.doubleValue(),
        decimalPlaces);
  }

  @Override
  public FxRate3 crossDivide(double factor, FxRate3 divisor) {
    return crossDivide(factor, divisor, getValue().decimalPlaces());
  }

  @Override
  public FxRate3 crossDivide(BigDecimal factor, FxRate3 divisor) {
    return crossDivide(factor, divisor, getValue().decimalPlaces());
  }

  @Value(staticConstructor = "of")
  public static class FxRate3Values {

    Rate buyRate;
    Rate sellRate;
    Rate avgRate;

    public FxRate3Values(Rate buyRate, Rate sellRate, Rate avgRate) {
      ArgChecker.notNull(buyRate);
      ArgChecker.notNull(sellRate);
      ArgChecker.notNull(avgRate);
      this.buyRate = buyRate;
      this.sellRate = sellRate;
      this.avgRate = avgRate;
    }

    public static FxRate3Values of(double buyRate, double sellRate, double avgRate) {
      return new FxRate3Values(Rate.of(buyRate), Rate.of(sellRate), Rate.of(avgRate));
    }

    public static FxRate3Values of(double buyRate, double sellRate, double avgRate, int decimalPlaces) {
      return new FxRate3Values(Rate.of(buyRate, decimalPlaces), Rate.of(sellRate, decimalPlaces),
          Rate.of(avgRate, decimalPlaces));
    }

    public static FxRate3Values of(BigDecimal buyRate, BigDecimal sellRate, BigDecimal avgRate) {
      return new FxRate3Values(Rate.of(buyRate), Rate.of(sellRate), Rate.of(avgRate));
    }

    public FxRate3Values add(double augend, int decimalPlaces) {
      return new FxRate3Values(Rate.of(this.buyRate.add(augend, decimalPlaces)),
          Rate.of(this.sellRate.add(augend, decimalPlaces)),
          Rate.of(this.avgRate.add(augend, decimalPlaces)));
    }

    public FxRate3Values add(double augend) {
      return add(augend, decimalPlaces());
    }

    public FxRate3Values add(BigDecimal augend, int decimalPlaces) {
      return new FxRate3Values(Rate.of(this.buyRate.add(augend, decimalPlaces)),
          Rate.of(this.sellRate.add(augend, decimalPlaces)),
          Rate.of(this.avgRate.add(augend, decimalPlaces)));
    }

    public FxRate3Values add(BigDecimal augend) {
      return add(augend, decimalPlaces());
    }

    public FxRate3Values multiply(double multiplicand, int decimalPlaces) {
      return new FxRate3Values(Rate.of(this.buyRate.multiply(multiplicand, decimalPlaces)),
          Rate.of(this.sellRate.multiply(multiplicand, decimalPlaces)),
          Rate.of(this.avgRate.multiply(multiplicand, decimalPlaces)));
    }

    public FxRate3Values multiply(double multiplicand) {
      return multiply(multiplicand, decimalPlaces());
    }

    public FxRate3Values multiply(BigDecimal multiplicand, int decimalPlaces) {
      return new FxRate3Values(Rate.of(this.buyRate.multiply(multiplicand, decimalPlaces)),
          Rate.of(this.sellRate.multiply(multiplicand, decimalPlaces)),
          Rate.of(this.avgRate.multiply(multiplicand, decimalPlaces)));
    }

    public FxRate3Values multiply(BigDecimal multiplicand) {
      return multiply(multiplicand, decimalPlaces());
    }

    public FxRate3Values divide(double divisor, int decimalPlaces) {
      return new FxRate3Values(
          Rate.of(this.buyRate.divide(divisor, decimalPlaces)),
          Rate.of(this.sellRate.divide(divisor, decimalPlaces)),
          Rate.of(this.avgRate.divide(divisor, decimalPlaces)));
    }

    public FxRate3Values divide(double divisor) {
      return divide(divisor, decimalPlaces());
    }

    public FxRate3Values divide(BigDecimal divisor, int decimalPlaces) {
      return new FxRate3Values(
          Rate.of(this.buyRate.divide(divisor, decimalPlaces)),
          Rate.of(this.sellRate.divide(divisor, decimalPlaces)),
          Rate.of(this.avgRate.divide(divisor, decimalPlaces)));
    }

    public FxRate3Values divide(BigDecimal divisor) {
      return divide(divisor, decimalPlaces());
    }

    public FxRate3Values inverse() {
      return new FxRate3Values(buyRate.inverse(), sellRate.inverse(), avgRate.inverse());
    }

    private int decimalPlaces() {
      return this.avgRate.decimalPlaces();
    }
  }
}
