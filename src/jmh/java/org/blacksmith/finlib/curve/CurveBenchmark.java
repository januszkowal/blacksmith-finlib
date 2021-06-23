package org.blacksmith.finlib.curve;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.basic.datetime.Tenor;
import org.blacksmith.finlib.curve.definition.CurveDefinition;
import org.blacksmith.finlib.curve.iterator.CurveDateIterator;
import org.blacksmith.finlib.curve.node.CurveNodeDefinition;
import org.blacksmith.finlib.curve.node.CurveNodeReferenceData;
import org.blacksmith.finlib.curve.node.SimpleCurveNodeReferenceData;
import org.blacksmith.finlib.curve.types.CurvePoint;
import org.blacksmith.finlib.datetime.daycount.StandardDayCounts;
import org.blacksmith.finlib.marketdata.QuoteId;
import org.blacksmith.finlib.math.analysis.interpolation.InterpolationAlgorithm;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(2)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
public class CurveBenchmark {
  private static final LocalDate valuationDate = LocalDate.now();
  private static final CurveFactory curveFactory = new CurveFactory();

  @State(Scope.Benchmark)
  public static class BenchmarkData {
    @Param({ "1", "10" })
    int years;

    @Param({ "AKIMA_SPLINE_APACHE_COMMONS", "AKIMA_SPLINE", "LINEAR_APACHE_COMMONS", "LINEAR", "DOUBLE_QUADRATIC" })
    InterpolationAlgorithm interpolator;
    public List<CurveNodeReferenceData> nodes;
    private CurveDefinition curveDefinition;
    private CurveDateIterator curveIterator;

    @Setup(Level.Trial)
    public void setUp() {
      if (years == 1) {
        this.nodes = createNodes1Y();
      }
      else if (years == 10) {
        this.nodes = createNodes10Y();
      }
      this.curveDefinition = CurveDefinition.builder()
          .name("aaa")
          .currency(Currency.EUR)
          .dayCount(StandardDayCounts.ACT_360)
          .interpolator(interpolator)
          .nodes(List.of(EMPTY_CURVE_NODE()))
          .build();
      var curve = curveFactory.createCurve(LocalDate.now(), curveDefinition, nodes);
      this.curveIterator = new CurveDateIterator(valuationDate, valuationDate, valuationDate.plusYears(years), curve);
    }
  }

  @Benchmark
  public List<CurvePoint> generateCurve(BenchmarkData data) {
    return data.curveIterator.values();
  }

  private static List<CurveNodeReferenceData> createNodes1Y() {
    return List.of(SimpleCurveNodeReferenceData.of("1D", Tenor.TENOR_1D, 2.50d),
        SimpleCurveNodeReferenceData.of("7D", Tenor.TENOR_1W,3.07d),
        SimpleCurveNodeReferenceData.of("14D", Tenor.TENOR_2W, 3.36d),
        SimpleCurveNodeReferenceData.of("1M", Tenor.TENOR_1M, 3.71d),
        SimpleCurveNodeReferenceData.of("3M", Tenor.TENOR_3M, 4.27d),
        SimpleCurveNodeReferenceData.of("6M", Tenor.TENOR_6M, 4.38d),
        SimpleCurveNodeReferenceData.of("9M", Tenor.TENOR_9M, 4.47d),
        SimpleCurveNodeReferenceData.of("1Y", Tenor.TENOR_1Y, 4.52d));
  }

  private static List<CurveNodeReferenceData> createNodes10Y() {
    return List.of(SimpleCurveNodeReferenceData.of("1D", Tenor.TENOR_1D, 2.50d),
        SimpleCurveNodeReferenceData.of("7D", Tenor.TENOR_1W,3.07d),
        SimpleCurveNodeReferenceData.of("14D", Tenor.TENOR_2W, 3.36d),
        SimpleCurveNodeReferenceData.of("1M", Tenor.TENOR_1M, 3.71d),
        SimpleCurveNodeReferenceData.of("3M", Tenor.TENOR_3M, 4.27d),
        SimpleCurveNodeReferenceData.of("6M", Tenor.TENOR_6M, 4.38d),
        SimpleCurveNodeReferenceData.of("9M", Tenor.TENOR_9M, 4.47d),
        SimpleCurveNodeReferenceData.of("1Y", Tenor.TENOR_1Y, 4.52d),
        SimpleCurveNodeReferenceData.of("2Y", Tenor.TENOR_2Y, 4.55d),
        SimpleCurveNodeReferenceData.of("3Y", Tenor.TENOR_3Y, 4.59d),
        SimpleCurveNodeReferenceData.of("5Y", Tenor.TENOR_5Y, 4.7d),
        SimpleCurveNodeReferenceData.of("10Y", Tenor.TENOR_10Y, 4.8d));
  }

  private static CurveNodeDefinition EMPTY_CURVE_NODE() {
    return new CurveNodeDefinition() {
      @Override
      public String getLabel() {
        return null;
      }

      @Override
      public Tenor getTenor() {
        return null;
      }

      @Override
      public QuoteId getQuoteId() {
        return null;
      }

      @Override
      public double getSpread() {
        return 0;
      }
    };
  }
}
