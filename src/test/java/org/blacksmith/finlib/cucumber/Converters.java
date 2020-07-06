package org.blacksmith.finlib.cucumber;

import io.cucumber.java.ParameterType;
import java.time.LocalDate;
import org.blacksmith.commons.datetime.TimeUnit;
import org.blacksmith.finlib.basic.datetime.Frequency;

public class Converters {
  @ParameterType(name="frequency",value="(\\d+)(D|W|M|Q|H|Y)")
  public Frequency frequency(String amount, String unit) {
    return new Frequency(Integer.parseInt(amount), TimeUnit.ofSymbol(unit));
  }
  @ParameterType(name="date",value="\\d{4}-\\d{2}-\\d{2}")
  public LocalDate date(String date) {
    return LocalDate.parse(date);
  }
}
