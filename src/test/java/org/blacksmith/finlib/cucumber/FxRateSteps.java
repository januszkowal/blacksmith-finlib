package org.blacksmith.finlib.cucumber;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rates.BasicMarketDataWrapper;
import org.blacksmith.finlib.rates.MarketDataMemoryService;
import org.blacksmith.finlib.rates.MarketDataWrapper;
import org.blacksmith.finlib.rates.fxccypair.FxCurrencyPair;
import org.blacksmith.finlib.rates.fxrates.FxRate3;
import org.blacksmith.finlib.rates.fxrates.FxRate3Raw;
import org.blacksmith.finlib.rates.fxrates.FxRateId;
import org.blacksmith.finlib.rates.fxrates.FxRateService;
import org.blacksmith.finlib.rates.fxrates.impl.FxRateServiceImpl;
import org.blacksmith.finlib.rates.fxrates.FxRateType;

import groovy.lang.GroovyShell;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class FxRateSteps {

  final GroovyShell shell = new GroovyShell();
  final MarketDataMemoryService<FxRateId, FxRate3Raw.FxRateRaw3Data> fxRateSourceService = new MarketDataMemoryService<>();
  Map<String, FxCurrencyPair> pairs;
  private FxRateService fxRateService;
  private int precision;

  @Given("Define currency pairs")
  public void definePairs(List<FxCurrencyPair> inputPairs) {
    log.info("Currency pairs");
    inputPairs.forEach(pair -> {
      log.info(pair.toString());
    });
    this.pairs = inputPairs.stream()
        .collect(Collectors.toMap(pair -> pair.getBase().getCurrencyCode() + "/" + pair.getCounter().getCurrencyCode(), pair -> pair));
  }

  @Given("Source rates with precision {int}")
  public void defineSourceRates(int precision, DataTable inputFxRates) {
    var rates = createRates(inputFxRates, precision);
    fxRateSourceService.setMarketData(rates);
    log.info("Source rates");
    fxRateSourceService.getMarketData().forEach(rate -> {
      log.info(rate.toString());
    });
  }

  @When("Create service with local currency {currency} and precision {int}")
  public void createService(Currency localCurrency, int precision) {
    this.precision = precision;
    this.fxRateService = new FxRateServiceImpl(localCurrency,
        (ccy1, ccy2) -> pairs.get(ccy1 + "/" + ccy2),
        this.fxRateSourceService, precision);
  }

  @Then("Verify output triple rates")
  public void verifyOutputTripleRates(List<FxRate3Input> inputRates) {
    for (FxRate3Input input : inputRates) {
      var rate3 = fxRateService.getRate3(input.getKey(), input.getDate());
      log.info("Rate {} value {}", input.getKey(), rate3);
      assertNotNull(rate3, input.toString());
      assertThat(rate3.getValue()).describedAs(input.toString())
          .extracting(FxRate3.FxRate3Data::getBuy, FxRate3.FxRate3Data::getSell, FxRate3.FxRate3Data::getAvg)
          .containsExactly(input.getBuy(), input.getSell(), input.getAvg());
    }
  }

  @Then("Verify output single rates")
  public void verifyOutputSingleRates(List<FxRate1Input> inputRates) {
    for (FxRate1Input input : inputRates) {
      var rate3 = fxRateService.getRate(input.getKey(), input.getDate(), input.getType());
      assertNotNull(rate3, input.toString());
      assertThat(rate3.getValue()).describedAs(input.toString()).isEqualTo(input.getRate());
    }
  }

  @DataTableType
  public FxCurrencyPair createFxCurrencyPair(Map<String, String> row) {
    return FxCurrencyPair.of(Currency.of(row.get("base")), Currency.of(row.get("counter")), Boolean.valueOf(row.get("cross")),
        Double.valueOf(row.get("factor")));
  }

  @DataTableType
  public FxRate3Input createFxRate3Input(Map<String, String> row) {
    return FxRate3Input.of(FxRateId.of(Currency.of(row.get("from")), Currency.of(row.get("to"))),
        LocalDate.parse(row.get("date"), DateTimeFormatter.ISO_LOCAL_DATE),
        Rate.of(evaluate(row.get("buy")), this.precision),
        Rate.of(evaluate(row.get("sell")), this.precision),
        Rate.of(evaluate(row.get("avg")), this.precision));
  }

  @DataTableType
  public FxRate1Input createFxRate1Input(Map<String, String> row) {
    return FxRate1Input.of(FxRateId.of(Currency.of(row.get("from")), Currency.of(row.get("to"))),
        LocalDate.parse(row.get("date"), DateTimeFormatter.ISO_LOCAL_DATE),
        FxRateType.valueOf(row.get("type")),
        Rate.of(evaluate(row.get("rate")), this.precision));
  }

  public List<MarketDataWrapper<FxRateId, FxRate3Raw.FxRateRaw3Data>> createRates(DataTable table, int precision) {
    return table.asMaps().stream()
        .map(row -> BasicMarketDataWrapper.of(FxRateId.of(row.get("from"), row.get("to")),
            FxRate3Raw.of(LocalDate.parse(row.get("date"), DateTimeFormatter.ISO_LOCAL_DATE),
                evaluate(row.get("buy")), evaluate(row.get("sell")), evaluate(row.get("avg")), precision)))
        .collect(Collectors.toList());
  }

  private double evaluate(String text) {
    Object result = shell.evaluate(text);
//    log.debug("evaluate text={} result={} class={}", text, result, result.getClass());
    if (Number.class.isAssignableFrom(result.getClass())) {
      return ((Number) result).doubleValue();
    } else {
      return 0d;
    }
  }
}