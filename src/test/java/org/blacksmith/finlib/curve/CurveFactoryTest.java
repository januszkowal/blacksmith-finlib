package org.blacksmith.finlib.curve;

import java.time.LocalDate;

import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.basic.datetime.Tenor;
import org.blacksmith.finlib.curve.definition.CurveDefinition;
import org.blacksmith.finlib.curve.discount.CurveDiscountFactorImpl;
import org.blacksmith.finlib.curve.discount.ZeroRateDiscountFactor;
import org.blacksmith.finlib.curve.node.SimpleCurveNodeDefinition;
import org.blacksmith.finlib.datetime.daycount.StandardDayCounts;
import org.blacksmith.finlib.marketdata.QuoteId;
import org.blacksmith.finlib.marketdata.QuoteProvider;
import org.blacksmith.finlib.marketdata.StandardId;
import org.blacksmith.finlib.math.analysis.interpolation.InterpolationAlgorithm;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class CurveFactoryTest {
  LocalDate valuationDate = LocalDate.of(2021, 5, 15);
  private static final double QUOTE_1D_VALUE = 0.02d;
  private static final double QUOTE_1W_VALUE = 0.022d;
  private static final double QUOTE_2W_VALUE = 0.023d;
  private static final double QUOTE_1M_VALUE = 0.025d;
  private static final double QUOTE_3M_VALUE = 0.027d;
  private static final double QUOTE_6M_VALUE = 0.029d;
  private static final double QUOTE_1Y_VALUE = 0.033d;
  private static final double QUOTE_3Y_VALUE = 0.036d;
  private static final double QUOTE_5Y_VALUE = 0.038d;
  QuoteProvider quoteProvider = Mockito.mock(QuoteProvider.class);
  private CurveFactoryDef curveFactory = new CurveFactoryDef(quoteProvider);

  @Test
  public void shouldReturnKnots() {
    Mockito.when(quoteProvider.getQuote(any(LocalDate.class), eq(quote("WIBOR-EUR-1D")))).thenReturn(QUOTE_1D_VALUE);
    Mockito.when(quoteProvider.getQuote(any(LocalDate.class), eq(quote("WIBOR-EUR-1W")))).thenReturn(QUOTE_1W_VALUE);
    Mockito.when(quoteProvider.getQuote(any(LocalDate.class), eq(quote("WIBOR-EUR-2W")))).thenReturn(QUOTE_2W_VALUE);
    Mockito.when(quoteProvider.getQuote(any(LocalDate.class), eq(quote("WIBOR-EUR-1M")))).thenReturn(QUOTE_1M_VALUE);
    Mockito.when(quoteProvider.getQuote(any(LocalDate.class), eq(quote("WIBOR-EUR-3M")))).thenReturn(QUOTE_3M_VALUE);
    Mockito.when(quoteProvider.getQuote(any(LocalDate.class), eq(quote("WIBOR-EUR-6M")))).thenReturn(QUOTE_6M_VALUE);
    Mockito.when(quoteProvider.getQuote(any(LocalDate.class), eq(quote("WIBOR-EUR-1Y")))).thenReturn(QUOTE_1Y_VALUE);
    Mockito.when(quoteProvider.getQuote(any(LocalDate.class), eq(quote("WIBOR-EUR-3Y")))).thenReturn(QUOTE_3Y_VALUE);
    Mockito.when(quoteProvider.getQuote(any(LocalDate.class), eq(quote("WIBOR-EUR-5Y")))).thenReturn(QUOTE_5Y_VALUE);

    CurveDefinition definition = CurveDefinition.builder()
        .name("EUR-ZERO")
        .interpolator(InterpolationAlgorithm.AKIMA_SPLINE_BLACKSMITH)
        .dayCount(StandardDayCounts.ACT_360)
        .currency(Currency.EUR)
        .node(SimpleCurveNodeDefinition.of("WIBOR-1D", Tenor.TENOR_1D, quote("WIBOR-EUR-1D"), 0))
        .node(SimpleCurveNodeDefinition.of("WIBOR-1W", Tenor.TENOR_1W, quote("WIBOR-EUR-1W"), 0))
        .node(SimpleCurveNodeDefinition.of("WIBOR-2W", Tenor.TENOR_2W, quote("WIBOR-EUR-2W"), 0))
        .node(SimpleCurveNodeDefinition.of("WIBOR-1M", Tenor.TENOR_1M, quote("WIBOR-EUR-1M"), 0))
        .node(SimpleCurveNodeDefinition.of("WIBOR-3M", Tenor.TENOR_3M, quote("WIBOR-EUR-3M"), 0))
        .node(SimpleCurveNodeDefinition.of("WIBOR-6M", Tenor.TENOR_6M, quote("WIBOR-EUR-6M"), 0))
        .node(SimpleCurveNodeDefinition.of("WIBOR-1Y", Tenor.TENOR_1Y, quote("WIBOR-EUR-1Y"), 0))
        .node(SimpleCurveNodeDefinition.of("WIBOR-3Y", Tenor.TENOR_3Y, quote("WIBOR-EUR-3Y"), 0))
        .node(SimpleCurveNodeDefinition.of("WIBOR-5Y", Tenor.TENOR_5Y, quote("WIBOR-EUR-5Y"), 0))
        .build();

    var curve = curveFactory.createCurve(valuationDate, definition);
    CurveDiscountFactorImpl df = CurveDiscountFactorImpl.of(curve, ZeroRateDiscountFactor.of());

    assertThat(curve.value(valuationDate.minusDays(10))).isEqualTo(QUOTE_1D_VALUE);
    assertThat(curve.value(valuationDate)).isEqualTo(QUOTE_1D_VALUE);
    assertThat(curve.value(valuationDate.plusDays(7))).isEqualTo(QUOTE_1W_VALUE);
    assertThat(curve.value(valuationDate.plusDays(14))).isEqualTo(QUOTE_2W_VALUE);
    assertThat(curve.value(valuationDate.plusMonths(1))).isEqualTo(QUOTE_1M_VALUE);
    assertThat(curve.value(valuationDate.plusMonths(3))).isEqualTo(QUOTE_3M_VALUE);
    assertThat(curve.value(valuationDate.plusMonths(6))).isEqualTo(QUOTE_6M_VALUE);
    assertThat(curve.value(valuationDate.plusYears(1))).isEqualTo(QUOTE_1Y_VALUE);
    assertThat(curve.value(valuationDate.plusYears(3))).isEqualTo(QUOTE_3Y_VALUE);
    assertThat(curve.value(valuationDate.plusYears(5))).isEqualTo(QUOTE_5Y_VALUE);
    assertThat(curve.value(valuationDate.plusYears(6))).isEqualTo(QUOTE_5Y_VALUE);

    assertThat(df.discountFactor(valuationDate)).isEqualTo(1d);
    assertThat(df.discountFactor(valuationDate.plusYears(1))).isEqualTo(0.9670952060255066d);
  }

  private QuoteId quote(String value) {
    return QuoteId.of(StandardId.of("TICKER", value), "Value");
  }
}
