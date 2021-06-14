package org.blacksmith.finlib.datetime.adjust;

import java.time.LocalDate;

import org.blacksmith.commons.enums.EnumUtils;
import org.blacksmith.commons.enums.EnumValueConverter;

public enum StandardDateAdjuster implements DateAdjuster {
  END_OF_MONTH("EndOfMonth") {
    @Override
    public LocalDate adjust(LocalDate date) {
      return date.withDayOfMonth(date.lengthOfMonth());
    }
  };

  private static final EnumValueConverter<String, StandardDateAdjuster> shortNameEnumConverter =
      EnumValueConverter.of(StandardDateAdjuster.class, StandardDateAdjuster::shortName);
  final private String shortName;

  StandardDateAdjuster(String shortName) {
    this.shortName = shortName;
  }

  public static StandardDateAdjuster fromShortName(String shortName) {
    return shortNameEnumConverter.convert(shortName);
  }

  public static StandardDateAdjuster fromName(String name) {
    return EnumUtils.getEnumByName(StandardDateAdjuster.class, name);
  }

  public String shortName() {
    return this.shortName;
  }
}
