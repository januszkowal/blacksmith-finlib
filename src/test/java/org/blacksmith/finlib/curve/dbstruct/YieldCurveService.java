package org.blacksmith.finlib.curve.dbstruct;

import java.time.LocalDate;

import org.blacksmith.finlib.curve.utils.RateUtils;

public interface YieldCurveService {
  int getYearLength(CurveRateId curveRateId);

  double getDcfValue(CurveRateId curveRateId, LocalDate date);

  double getInterestRateValue(CurveRateId curveRateId);

  default double getFra(CurveRateId curveRateId, LocalDate d1, LocalDate d2) {
    int yearLength = getYearLength(curveRateId);
    double dcf1 = getDcfValue(curveRateId, d1);
    double dcf2 = getDcfValue(curveRateId, d2);
    return RateUtils.calculateFra(curveRateId.getAsOfDate(), d1, d2, dcf1, dcf2, yearLength);
  }

  default double getFra(CurveRateId curveRateId, int l1, int l2) {
    int yearLength = getYearLength(curveRateId);
    LocalDate d1 = curveRateId.getAsOfDate().plusDays(l1);
    LocalDate d2 = curveRateId.getAsOfDate().plusDays(l2);
    double dcf1 = getDcfValue(curveRateId, d1);
    double dcf2 = getDcfValue(curveRateId, d2);
    return RateUtils.calculateFra(curveRateId.getAsOfDate(), d1, d2, dcf1, dcf2, yearLength);
  }
}
