package org.blacksmith.finlib.dayconvention;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.blacksmith.commons.enums.EnumUtils;
import org.blacksmith.commons.enums.EnumValueConverter;
import org.blacksmith.finlib.calendar.BusinessDayCalendar;

public enum StandardBusinessDayConvention implements BusinessDayConvention {
  /**
   * No adjustment
   */
  NO_ADJUST("NoAdjust") {
    @Override
    public LocalDate adjust(LocalDate date, BusinessDayCalendar calendar) {
      return date;
    }
  },
  /**
   * Next business day adjustment
   */
  FOLLOWING("Following") {
    @Override
    public LocalDate adjust(LocalDate date, BusinessDayCalendar calendar) {
      return calendar.nextOrSame(date);
    }
  },
  MODIFIED_FOLLOWING("ModifiedFollowing") {
    @Override
    public LocalDate adjust(LocalDate date, BusinessDayCalendar calendar) {
      return calendar.nextSameOrLastInMonth(date);
    }
  },
  PRECEDING("Preceding") {
    @Override
    public LocalDate adjust(LocalDate date, BusinessDayCalendar calendar) {
      return calendar.previousOrSame(date);
    }
  },
  MODIFIED_PRECEDING("ModifiedPreceding") {
    @Override
    public LocalDate adjust(LocalDate date, BusinessDayCalendar calendar) {
      return calendar.previousSameOrLastInMonth(date);
    }
  },
  END_OF_MONTH("EndOfMonth") {
    @Override
    public LocalDate adjust(LocalDate date, BusinessDayCalendar calendar) {
      return date.withDayOfMonth(date.lengthOfMonth());
    }
  },
  END_OF_MONTH_PRECEDING("EndOfMonthPreceding") {
    @Override
    public LocalDate adjust(LocalDate date, BusinessDayCalendar calendar) {
      return calendar.previousOrSame(date.withDayOfMonth(date.lengthOfMonth()));
    }
  },
  END_OF_MONTH_FOLLOWING("EndOfMonthFollowing") {
    @Override
    public LocalDate adjust(LocalDate date, BusinessDayCalendar calendar) {
      return calendar.nextOrSame(date.withDayOfMonth(date.lengthOfMonth()));
    }
  },
  /**
   * next business day if Sun/Mon, otherwise previous
   */
  NEAREST("Nearest") {
    @Override
    public LocalDate adjust(LocalDate date, BusinessDayCalendar calendar) {
      if (calendar.isBusinessDay(date)) {
        return date;
      }
      if (date.getDayOfWeek() == DayOfWeek.SUNDAY || date.getDayOfWeek() == DayOfWeek.MONDAY) {
        return calendar.next(date);
      } else {
        return calendar.previous(date);
      }
    }
  };
  private static final EnumValueConverter<String, StandardBusinessDayConvention> enumConverter =
      EnumValueConverter.of(StandardBusinessDayConvention.class, StandardBusinessDayConvention::shortName);
  final private String shortName;

  StandardBusinessDayConvention(String shortName) {
    this.shortName = shortName;
  }

  public static StandardBusinessDayConvention fromShortName(String shortName) {
    return enumConverter.convert(shortName);
  }

  public static StandardBusinessDayConvention fromName(String name) {
    return EnumUtils.getEnumByName(StandardBusinessDayConvention.class, name);
  }

  public String shortName() {
    return this.shortName;
  }

}
