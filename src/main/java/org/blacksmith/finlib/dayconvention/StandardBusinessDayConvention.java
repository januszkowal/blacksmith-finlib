package org.blacksmith.finlib.dayconvention;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.blacksmith.commons.enums.EnumUtils;
import org.blacksmith.finlib.calendar.BusinessDayCalendar;
import org.blacksmith.finlib.dayconvention.utils.Pair;

public enum StandardBusinessDayConvention implements BusinessDayConvention {
  NO_ADJUST("NO_ADJUST") {
    @Override
    public LocalDate adjust(LocalDate date, BusinessDayCalendar calendar) {
      return date;
    }

    @Override public Pair<LocalDate> adjustPair(LocalDate date, BusinessDayCalendar calendar) {
      return Pair.of(date,date);
    }
  },
  FOLLOWING("FOLLOWING") {
    @Override
    public LocalDate adjust(LocalDate date, BusinessDayCalendar calendar) {
      return calendar.nextOrSame(date);
    }

    @Override public Pair<LocalDate> adjustPair(LocalDate date, BusinessDayCalendar calendar) {
      LocalDate adjusted = adjust(date,calendar);
      return Pair.of(adjusted,adjusted);
    }
  },
  MODIFIED_FOLLOWING("MODIFIED_FOLLOWING") {
    @Override
    public LocalDate adjust(LocalDate date, BusinessDayCalendar calendar) {
      return calendar.nextSameOrLastInMonth(date);
    }

    @Override public Pair<LocalDate> adjustPair(LocalDate date, BusinessDayCalendar calendar) {
      LocalDate adjusted = adjust(date,calendar);
      return Pair.of(adjusted,adjusted.isBefore(date)?adjusted:date);
    }
  },
  PRECEDING("PRECEDING") {
    @Override
    public LocalDate adjust(LocalDate date, BusinessDayCalendar calendar) {
      return calendar.previousOrSame(date);
    }

    @Override public Pair<LocalDate> adjustPair(LocalDate date, BusinessDayCalendar calendar) {
      LocalDate adjusted = adjust(date,calendar);
      return Pair.of(adjusted,adjusted);
    }
  },
  MODIFIED_PRECEDING("MODIFIED_PRECEDING") {
    @Override
    public LocalDate adjust(LocalDate date, BusinessDayCalendar calendar) {
      return calendar.previousSameOrLastInMonth(date);
    }

    @Override public Pair<LocalDate> adjustPair(LocalDate date, BusinessDayCalendar calendar) {
      LocalDate adjusted = adjust(date,calendar);
      return Pair.of(adjusted,adjusted);
    }
  },
  END_OF_MONTH("END_OF_MONTH") {
    @Override
    public LocalDate adjust(LocalDate date, BusinessDayCalendar calendar) {
      return date.withDayOfMonth(date.lengthOfMonth());
    }

    @Override public Pair<LocalDate> adjustPair(LocalDate date, BusinessDayCalendar calendar) {
      LocalDate adjusted = adjust(date,calendar);
      return Pair.of(adjusted,adjusted);
    }
  },
  END_OF_MONTH_PRECEDING("END_OF_MONTH_PRECEDING") {
    @Override
    public LocalDate adjust(LocalDate date, BusinessDayCalendar calendar) {
      return calendar.previousOrSame(date.withDayOfMonth(date.lengthOfMonth()));
    }

    @Override public Pair<LocalDate> adjustPair(LocalDate date, BusinessDayCalendar calendar) {
      LocalDate adjusted = adjust(date,calendar);
      return Pair.of(adjusted,adjusted);
    }
  },
  END_OF_MONTH_FOLLOWING("END_OF_MONTH_FOLLOWING") {
    @Override
    public LocalDate adjust(LocalDate date, BusinessDayCalendar calendar) {
      return calendar.nextOrSame(date.withDayOfMonth(date.lengthOfMonth()));
    }

    @Override public Pair<LocalDate> adjustPair(LocalDate date, BusinessDayCalendar calendar) {
      LocalDate adjusted = adjust(date,calendar);
      return Pair.of(adjusted,adjusted);
    }
  };
  String shortName;
  private static Map<String, StandardBusinessDayConvention> shortNameMap =
      Arrays.stream(StandardBusinessDayConvention.values()).collect(Collectors.toMap(StandardBusinessDayConvention::getShortName, e -> e));

  StandardBusinessDayConvention(String shortName) {
    this.shortName = shortName;
  }

  public String getShortName() {
    return this.shortName;
  }

  public static StandardBusinessDayConvention fromShortName(String shortName) {
    return shortNameMap.get(shortName);
  }

  public static StandardBusinessDayConvention fromName(String name) {
    return EnumUtils.getEnumByName(StandardBusinessDayConvention.class, name);
  }

}
