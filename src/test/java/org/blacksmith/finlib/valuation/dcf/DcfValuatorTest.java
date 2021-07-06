package org.blacksmith.finlib.valuation.dcf;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.basic.rounding.HalfUpRounding;
import org.blacksmith.finlib.curve.discount.CurveFactors;
import org.blacksmith.finlib.valuation.dto.Cashflow;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.eq;

public class DcfValuatorTest {
  private final static LocalDate valuationDate = LocalDate.of(2020, 5, 7);
  private final static LocalDate dt1 = valuationDate.plusDays(1);
  private final static LocalDate dt2 = valuationDate.plusDays(2);
  private static DcfValuator valuator;

  @BeforeAll
  public static void setUp() {
    CurveFactors curveDiscountFactor = Mockito.mock(CurveFactors.class);
    Mockito.when(curveDiscountFactor.discountFactor(eq(valuationDate))).thenReturn(1d);
    Mockito.when(curveDiscountFactor.discountFactor(eq(dt1))).thenReturn(0.95d);
    Mockito.when(curveDiscountFactor.discountFactor(eq(dt2))).thenReturn(0.9d);
    valuator = DcfValuator.of(curveDiscountFactor, HalfUpRounding.ofDecimalPlaces(2));
  }

  @Test
  public void evaluateSingleCashflow() {
    var cashflows = List.of(Cashflow.of(valuationDate, BigDecimal.valueOf(123.99d), Currency.EUR));
    Assertions.assertThat(valuator.value(cashflows)).isEqualTo(BigDecimal.valueOf(123.99d));
  }

  @Test
  public void evaluateTwoCashflows() {
    var cashflows = List.of(Cashflow.of(valuationDate, BigDecimal.valueOf(123.99d), Currency.EUR),
        Cashflow.of(dt1, BigDecimal.valueOf(100.00d), Currency.EUR)
    );
    Assertions.assertThat(valuator.value(cashflows)).isEqualTo(BigDecimal.valueOf(123.99d + 95.00d));
  }

}
