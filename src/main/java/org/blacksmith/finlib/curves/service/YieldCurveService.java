package org.blacksmith.finlib.curves.service;

import java.time.LocalDate;

import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.basic.numbers.Rate;

public interface YieldCurveService {
  Rate getDcfValue(CurveRateId curveRateId, LocalDate asOfDate, LocalDate date);

  Rate getYieldValue(CurveRateId curveRateId, LocalDate asOfDate, LocalDate date);

  int getYearLength(CurveRateId curveRateId);

  default Rate getFra(CurveRateId curveRateId, LocalDate asOfDate, LocalDate d1, LocalDate d2) {
    int yearLength = getYearLength(curveRateId);
    long l1 = DateUtils.daysBetween(d1, asOfDate);
    long l2 = DateUtils.daysBetween(d2, asOfDate);
    Rate dcf1 = getDcfValue(curveRateId, asOfDate, d1);
    Rate dcf2 = getDcfValue(curveRateId, asOfDate, d2);
    return Rate.of((dcf1.doubleValue() / dcf2.doubleValue()) * ((double) yearLength / (l2 - l1)) * 100d);
  }
}
