package org.blacksmith.finlib.basic;

import java.time.LocalDate;
import org.blacksmith.commons.date.LocalDateUtils;

public enum StandardInterestBasis implements  InterestBasis {
  ACT_360("ACT/360") {
    @Override public double getFactor(LocalDate startDate, LocalDate endDate) {
      return LocalDateUtils.daysBetween(startDate,endDate) / 360d;
    }
  },
  ACT_365("ACT/365") {
    @Override public double getFactor(LocalDate startDate, LocalDate endDate) {
      return LocalDateUtils.daysBetween(startDate,endDate) / 365d;
    }
  },
  ACT_365_25("ACT/365.25") {
    @Override
    public double getFactor(LocalDate startDate, LocalDate endDate) {
      return LocalDateUtils.daysBetween(startDate,endDate) / 365.25d;
    }
  },
  ACT_ACT_ISDA("Act/Act ISDA") {
    @Override public double getFactor(LocalDate startDate, LocalDate endDate) {
      int y1 = startDate.getYear();
      int y2 = endDate.getYear();
      int firstYearLength = startDate.lengthOfYear();
      if (y1 == y2) {
        return LocalDateUtils.daysBetween(startDate,endDate) / firstYearLength;
      }
      int firstRemainderOfYear = firstYearLength - startDate.getDayOfYear() + 1;
      int secondRemainderOfYear = endDate.getDayOfYear() - 1;
      int secondYearLength = endDate.lengthOfYear();
      return (y2 - y1 - 1) +
          firstRemainderOfYear / firstYearLength +
          secondRemainderOfYear / secondYearLength;
    }
  },
  ACT_ACT_ISDA_TEST("Act/Act ISDAxx") {
    @Override public double getFactor(LocalDate startDate, LocalDate endDate) {
      int y1 = startDate.getYear();
      int y2 = endDate.getYear();
      double firstYearLength = startDate.lengthOfYear();
      if (y1 == y2) {
        double actualDays = endDate.getDayOfYear() - startDate.getDayOfYear();
        return actualDays / firstYearLength;
      }
      double firstRemainderOfYear = firstYearLength - startDate.getDayOfYear() + 1;
      double secondRemainderOfYear = endDate.getDayOfYear() - 1;
      double secondYearLength = endDate.lengthOfYear();
      return firstRemainderOfYear / firstYearLength +
          secondRemainderOfYear / secondYearLength +
          (y2 - y1 - 1);
    }
  },
  X30_360_ISDA("30/360 ISDA") {
    @Override
    public double getFactor(LocalDate startDate, LocalDate endDate) {
      YMD date1 = YMD.of(startDate);
      YMD date2 = YMD.of(endDate);
      if (date1.getDay()==31)
        date1.setDay(30);
      if (date2.getDay()==31 && date1.getDay()==30) {
        date2.setDay(30);
      }
      return DayCountUtils.thirty360Days(date1,date2)/360d;
    }
  },
  X30_E_360_ISDA("30E/360 ISDA") {
    @Override
    public double getFactor(LocalDate startDate, LocalDate endDate) {
      YMD date1 = YMD.of(startDate);
      YMD date2 = YMD.of(endDate);
      if (date1.getDay()==31 || LocalDateUtils.isLastDayOfFebruary(startDate))
        date1.setDay(30);
      if (date2.getDay()==31 && LocalDateUtils.isLastDayOfFebruary(endDate)) {
        date2.setDay(30);
      }
      return DayCountUtils.thirty360Days(date1,date2)/360d;
    }
  },
  X30_E_360("30E/360") {
    @Override
    public double getFactor(LocalDate startDate, LocalDate endDate) {
      YMD date1 = YMD.of(startDate);
      YMD date2 = YMD.of(endDate);
      if (date1.getDay()==31)
        date1.setDay(30);
      if (date2.getDay()==31) {
        date2.setDay(30);
      }
      return DayCountUtils.thirty360Days(date1,date2)/360d;
    }
  },
  X30_EPLUS_360("30E+/360") {
    @Override
    public double getFactor(LocalDate startDate, LocalDate endDate) {
      YMD date1 = YMD.of(startDate);
      YMD date2 = YMD.of(endDate);
      if (date1.getDay()==31)
        date1.setDay(30);
      if (date2.getDay()==31) {
        date2.setDay(1);
        date2.setMonth(date2.getMonth()+1);
      }
      return DayCountUtils.thirty360Days(date1,date2)/360d;
    }
  };
  String code;
  StandardInterestBasis(String code) {
    this.code = code;
  }
}
