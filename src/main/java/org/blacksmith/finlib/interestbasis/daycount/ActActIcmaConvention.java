package org.blacksmith.finlib.interestbasis.daycount;

import static org.blacksmith.commons.datetime.DateUtils.daysBetween;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.basic.datetime.Frequency;
import org.blacksmith.finlib.dayconvention.utils.DayCountUtils;
import org.blacksmith.finlib.dayconvention.utils.YmdDate;
import org.blacksmith.finlib.interestbasis.ScheduleInfo;

@Slf4j
public class ActActIcmaConvention implements DayCountConventionCalculator {

  @Override
  public boolean requireScheduleInfo() {
    return true;
  }

  @Override
  public long calculateDays(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    return DateUtils.daysBetween(startDate, endDate);
  }

  @Override
  public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    // calculation is based on the schedule period, firstDate assumed to be the start of the period
    LocalDate scheduleStartDate = scheduleInfo.getStartDate();
    LocalDate scheduleEndDate = scheduleInfo.getEndDate();
    LocalDate couponStartDate = startDate;
    LocalDate couponEndDate = scheduleInfo.getCouponEndDate();
    Frequency freq = scheduleInfo.getCouponFrequency();
    boolean eom = scheduleInfo.isEndOfMonthConvention();
    if (couponEndDate.equals(scheduleEndDate)) {
      //final period calculated forward from last coupon start date
      return forwardPeriod(endDate, couponStartDate, couponEndDate, freq, eom);
    }
    else if (scheduleStartDate.equals(startDate)) {
      // initial period calculated backward from first coupon end date
      return backwardPeriod(endDate, couponStartDate, couponEndDate, freq, eom);
    }
    else {
      if (isRegularPeriod(endDate, scheduleInfo)) {
        return regularPeriod(endDate, couponStartDate, couponEndDate, freq);
      }
      else {
        return forwardPeriod(endDate, couponStartDate, couponEndDate, freq, eom);
      }
    }
  }

  // calculate sub-periods backwards from "Coupon end date"
  private double backwardPeriod(LocalDate calcDate, LocalDate couponStartDate, LocalDate couponEndDate, Frequency freq,
      boolean eom) {
    log.debug("backward period: on={} cpn={}#{}", calcDate, couponStartDate, couponEndDate);
    LocalDate periodEnd = couponEndDate;
    LocalDate periodStart = eom(couponEndDate, freq.minusFrom(periodEnd), eom);
    double result = 0;
    while (periodStart.isAfter(couponStartDate)) {
      if (calcDate.isAfter(periodStart)) {
        result += calcPeriod(periodStart, DateUtils.min(calcDate, periodEnd), periodStart, periodEnd, freq);
      }
      periodEnd = periodStart;
      periodStart = eom(couponEndDate, freq.minusFrom(periodEnd), eom);
    }
    log.info("last: {} {} {}",calcDate,periodEnd,DateUtils.min(calcDate,periodEnd));
    return result + calcPeriod(couponStartDate, DateUtils.min(calcDate, periodEnd), periodStart, periodEnd, freq);
  }


  // calculate sub-periods forwards from "Coupon start date"
  private double forwardPeriod(LocalDate calcDate, LocalDate couponStartDate, LocalDate couponEndDate, Frequency freq,
      boolean eom) {
    log.debug("forward period: on={} cpn={}#{}", calcDate, couponStartDate, couponEndDate);
    LocalDate periodStart = couponStartDate;
    LocalDate periodEnd = eom(couponStartDate, freq.addTo(periodStart), eom);
    double result = 0;
    while (periodEnd.isBefore(couponEndDate)) {
      if (calcDate.isAfter(periodStart)) {
        result += calcPeriod(periodStart, DateUtils.min(calcDate, periodEnd), periodStart, periodEnd, freq);
      }
      periodStart = periodEnd;
      periodEnd = eom(couponStartDate, freq.addTo(periodEnd), eom);
    }
    return result + calcPeriod(periodStart, DateUtils.min(calcDate, periodEnd), periodStart, periodEnd, freq);
  }

  private double regularPeriod(LocalDate calcDate, LocalDate couponStartDate, LocalDate couponEndDate, Frequency freq) {
    long actualDays = daysBetween(couponStartDate, calcDate);
    long periodDays = daysBetween(couponStartDate, couponEndDate);
    double denominator = getDenominator(freq, periodDays);
    log.debug("regular period: on={} cpn={}#{} factor={}/{}",
        calcDate,
        couponStartDate, couponEndDate,
        actualDays,
        denominator);
    return denominator == 0d ? 0d : actualDays / denominator;
  }

  // calculate the result
  private double calcPeriod(LocalDate calcStart, LocalDate calcEnd, LocalDate periodStart, LocalDate periodEnd,
      Frequency freq) {
    long calcStartEpochDay = calcStart.toEpochDay();
    long calcEndEpochDay = calcEnd.toEpochDay();
    long periodStartEpochDay = periodStart.toEpochDay();
    long periodEndEpochDay = periodEnd.toEpochDay();
    long periodDays = periodEndEpochDay - periodStartEpochDay;
//    long actualDays = Math.min(calcEndEpochDay, periodEndEpochDay) - Math.max(calcStartEpochDay,periodStartEpochDay);
    long actualDays = calcEndEpochDay - calcStartEpochDay;
    double denominator = getDenominator(freq, periodDays);
    log.debug("period factor={}/{} pd={} calc={}#{} pe={}#{}",
        actualDays, denominator, periodDays,
        calcStart, calcEnd, periodStart, periodEnd);
    return denominator == 0d ? 0d : actualDays / denominator;
  }

  public static boolean isRegularPeriod(LocalDate calcDate, ScheduleInfo scheduleInfo) {
    if (scheduleInfo.getCouponFrequency().getEventsPerYear() == 0) {
      return false;
    }
    if (DayCountUtils.months360(scheduleInfo.getCouponStartDate(), scheduleInfo.getCouponEndDate()) != scheduleInfo
        .getCouponFrequency().getMonths()) {
      return false;
    }

    if (scheduleInfo.isEndOfMonthConvention()) {
      //ACT/ACT Ultimo
      YmdDate start = YmdDate.of(scheduleInfo.getCouponStartDate());
      YmdDate end = YmdDate.of(scheduleInfo.getCouponEndDate());
      return (start.getDay() == end.getDay()) ||
          (!DateUtils.isValidDate(start.getYear(), start.getMonth(), end.getDay()) && DateUtils
              .isLastDayOfMonth(scheduleInfo.getCouponEndDate())) ||
          (!DateUtils.isValidDate(end.getYear(), end.getMonth(), start.getDay()) && DateUtils
              .isLastDayOfMonth(scheduleInfo.getCouponStartDate()));
    } else {
      //ACT/ACT Normal
      return DateUtils.isLastDayOfMonth(scheduleInfo.getCouponStartDate()) &&
          DateUtils.isLastDayOfMonth(scheduleInfo.getCouponEndDate());
    }
  }

  private double getDenominator(Frequency frequency, long periodDays) {
    return frequency.eventsPerYear() * periodDays;
  }

  // apply eom convention
  private LocalDate eom(LocalDate base, LocalDate calc, boolean eom) {
    return (eom && base.getDayOfMonth() == base.lengthOfMonth() ? calc.withDayOfMonth(calc.lengthOfMonth()) : calc);
  }
}
