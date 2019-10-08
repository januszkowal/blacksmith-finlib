package org.blacksmith.finlib.dayconvention;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.blacksmith.commons.enums.EnumUtils;
import org.blacksmith.finlib.calendar.BusinessDayCalendar;

public enum StandardBusinessDayConvention implements BusinessDayConvention {
  /**
   * No adjustment
   */
  NO_ADJUST("NO_ADJUST") {
    @Override
    public LocalDate adjust(LocalDate date, BusinessDayCalendar calendar) {
      return date;
    }
  },
  /**
   * Next business day adjustment
   */
  FOLLOWING("FOLLOWING") {
    @Override
    public LocalDate adjust(LocalDate date, BusinessDayCalendar calendar) {
      return calendar.nextOrSame(date);
    }
  },
  MODIFIED_FOLLOWING("MODIFIED_FOLLOWING") {
    @Override
    public LocalDate adjust(LocalDate date, BusinessDayCalendar calendar) {
      return calendar.nextSameOrLastInMonth(date);
    }
  },
  PRECEDING("PRECEDING") {
    @Override
    public LocalDate adjust(LocalDate date, BusinessDayCalendar calendar) {
      return calendar.previousOrSame(date);
    }
  },
  MODIFIED_PRECEDING("MODIFIED_PRECEDING") {
    @Override
    public LocalDate adjust(LocalDate date, BusinessDayCalendar calendar) {
      return calendar.previousSameOrLastInMonth(date);
    }
  },
  END_OF_MONTH("END_OF_MONTH") {
    @Override
    public LocalDate adjust(LocalDate date, BusinessDayCalendar calendar) {
      return date.withDayOfMonth(date.lengthOfMonth());
    }
  },
  END_OF_MONTH_PRECEDING("END_OF_MONTH_PRECEDING") {
    @Override
    public LocalDate adjust(LocalDate date, BusinessDayCalendar calendar) {
      return calendar.previousOrSame(date.withDayOfMonth(date.lengthOfMonth()));
    }
  },
  END_OF_MONTH_FOLLOWING("END_OF_MONTH_FOLLOWING") {
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
 };;
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
