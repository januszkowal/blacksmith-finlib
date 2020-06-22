package org.blacksmith.finlib.interestbasis.daycount;

import static org.blacksmith.commons.datetime.DateUtils.daysBetween;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.blacksmith.commons.arg.Validate;
import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.datetime.Frequency;
import org.blacksmith.finlib.interestbasis.ScheduleInfo;

@Slf4j
public class ActActIsmaConvention implements DayCountConventionCalculator {

  @Override
  public int calculateDays(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    return DateUtils.daysBetween(startDate, endDate);
  }

  @Override
  public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    Validate.notNull(scheduleInfo, "Schedule info must be not null");
    Validate.notNull(scheduleInfo.getStartDate(), "Schedule start date must be not null");
    Validate.notNull(scheduleInfo.getEndDate(), "Schedule end date must be not null");
    //Validate.notNull(scheduleInfo.getCouponStartDate(), "Coupon start date must be not null");
    Validate.notNull(scheduleInfo.getCouponEndDate(), "Coupon end date must be not null");
    Validate.notNull(scheduleInfo.getCouponFrequency(), "Frequency must be not null");
    // calculation is based on the schedule period, firstDate assumed to be the start of the period
    LocalDate scheduleStartDate = scheduleInfo.getStartDate();
    LocalDate scheduleEndDate = scheduleInfo.getEndDate();
    LocalDate couponStartDate = startDate;
    LocalDate couponEndDate = scheduleInfo.getCouponEndDate();
    Frequency freq = scheduleInfo.getCouponFrequency();
    boolean eom = scheduleInfo.isEndOfMonthConvention();

    // final period, also handling single period schedules
    if (couponEndDate.equals(scheduleEndDate)) {
      LocalDate endCalculated = eom(startDate, freq.addTo(startDate), eom);
      if (endCalculated.isBefore(scheduleEndDate)) {
        return backwardPeriod(endDate, couponStartDate, couponEndDate, freq, eom);
      } else {
        return forwardPeriod(endDate, couponStartDate, couponEndDate, freq, eom);
      }
    }
    // initial period
    else if (scheduleStartDate.equals(startDate)) {
      return backwardPeriod(endDate, couponStartDate, couponEndDate, freq, eom);
    } else {
      //return regularPeriod(endDate, couponStartDate, couponEndDate, freq);
      return forwardPeriod(endDate, couponStartDate, couponEndDate, freq, eom);
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
      if (calcDate.isAfter(periodStart))
        result += calcPeriod(periodStart, DateUtils.min(calcDate,periodEnd), periodStart, periodEnd, freq);
      periodEnd = periodStart;
      periodStart = eom(couponEndDate, freq.minusFrom(periodEnd), eom);
    }
    return result + calcPeriod(couponStartDate, DateUtils.min(calcDate,periodEnd), periodStart, periodEnd, freq);
  }


  // calculate sub-periods forwards from "Coupon start date"
  private double forwardPeriod(LocalDate calcDate, LocalDate couponStartDate, LocalDate couponEndDate, Frequency freq,
      boolean eom) {
    log.debug("forward period: on={} cpn={}#{}", calcDate, couponStartDate, couponEndDate);
    LocalDate periodStart = couponStartDate;
    LocalDate periodEnd = eom(couponStartDate, freq.addTo(periodStart), eom);
    double result = 0;
    while (periodEnd.isBefore(couponEndDate)) {
      if (calcDate.isAfter(periodStart))
        result += calcPeriod(periodStart, DateUtils.min(calcDate,periodEnd), periodStart, periodEnd, freq);
      periodStart = periodEnd;
      periodEnd = eom(couponStartDate, freq.addTo(periodEnd), eom);
    }
    return result + calcPeriod(periodStart, DateUtils.min(calcDate,periodEnd), periodStart, periodEnd, freq);
  }

  private double regularPeriod(LocalDate calcDate, LocalDate couponStartDate, LocalDate couponEndDate, Frequency freq) {
    int actualDays = daysBetween(couponStartDate, calcDate);
    int periodDays = daysBetween(couponStartDate, couponEndDate);
    double denominator = getDenominator(freq, periodDays);
    log.debug("regular period: on={} cpn={}#{} factor={}/{}",
        calcDate,
        couponStartDate, couponEndDate,
        actualDays,
        denominator);
    return denominator == 0d ? 0d : actualDays / denominator;
  }

  // calculate the result
  private double calcPeriod(LocalDate calcStart, LocalDate calcEnd, LocalDate periodStart, LocalDate periodEnd, Frequency freq) {
    //if (calcStart.isBefore(periodStart)) return 0;
    //if (calcStart.isAfter(periodEnd)) return 0;
    //if (calcEnd.isBefore(periodStart)) return 0;
    //if (calcEnd.isAfter(periodEnd)) return 0;
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
        calcStart,calcEnd, periodStart, periodEnd);
    return denominator == 0d ? 0d : actualDays / denominator;
  }

  private double getDenominator(Frequency frequency, long periodDays) {
    return frequency.eventsPerYear() * periodDays;
    //return periodDays;
  }

  // apply eom convention
  private LocalDate eom(LocalDate base, LocalDate calc, boolean eom) {
    return (eom && base.getDayOfMonth() == base.lengthOfMonth() ? calc.withDayOfMonth(calc.lengthOfMonth()) : calc);
  }
}
