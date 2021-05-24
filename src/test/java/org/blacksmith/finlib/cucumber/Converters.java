package org.blacksmith.finlib.cucumber;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.blacksmith.commons.datetime.TimeUnit;
import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.basic.datetime.Frequency;

import io.cucumber.java.ParameterType;

public class Converters {
  @ParameterType(name = "frequency", value = "(\\d+)(D|W|M|Q|H|Y)")
  public Frequency frequency(String amount, String unit) {
    return new Frequency(Integer.parseInt(amount), TimeUnit.ofSymbol(unit));
  }

  @ParameterType(name = "date", value = "\\d{4}-\\d{2}-\\d{2}")
  public LocalDate date(String date) {
    return LocalDate.parse(date);
  }

  @ParameterType(name = "currency", value = "[A-Z0-9]{3}")
  public Currency frequency(String currency) {
    return Currency.of(currency);
  }

  @ParameterType(name = "stringList", value = "(\"([^\"]+)\"(\\s*(([,]?\\s*)|(and\\s?))\"[^\"]+\")*)")
  public List<String> stringList(String strings) {
    final Pattern compile = Pattern.compile("\\s*(,|and)?\\s*\"([^\"]+)\"");
    final Matcher matcher = compile.matcher(strings);
    List<String> result = new ArrayList<>();
    while (matcher.find()) {
      String item = "";
      if (matcher.group(1) == null) {
        item = matcher.group(0).replace("\"", "");
      } else {
        item = matcher.group(0).trim().replaceFirst(matcher.group(1), " ").trim().replace("\"", "");
      }
      result.add(item);
    }
    return result;
  }
}
