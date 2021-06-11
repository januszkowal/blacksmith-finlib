package org.blacksmith.finlib.interest.basis.daycount;

import java.time.LocalDate;

import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.basic.datetime.Frequency;
import org.blacksmith.finlib.dayconvention.utils.DayCountUtils;
import org.blacksmith.finlib.dayconvention.utils.YmdDate;
import org.blacksmith.finlib.interest.basis.ScheduleInfo;

import lombok.extern.slf4j.Slf4j;

import static org.blacksmith.commons.datetime.DateUtils.daysBetween;

@Slf4j
public class ActActIcmaConvention extends AbstractConvention {

  public ActActIcmaConvention() {
    super(true);
  }

  public static boolean isRegularPeriod(LocalDate calcDate, ScheduleInfo scheduleInfo) {
    if (scheduleInfo.getCouponFrequency().getEventsPerYear() == 0) {
      return false;
    }
    if (DayCountUtils.months360(scheduleInfo.getPeriodStartDate(), scheduleInfo.getPeriodEndDate()) != scheduleInfo
        .getCouponFrequency().getMonths()) {
      return false;
    }

    if (scheduleInfo.isEndOfMonthConvention()) {
      //ACT/ACT Ultimo
      YmdDate start = YmdDate.of(scheduleInfo.getPeriodStartDate());
      YmdDate end = YmdDate.of(scheduleInfo.getPeriodEndDate());
      return (start.getDay() == end.getDay()) ||
          (!DateUtils.isValidDate(start.getYear(), start.getMonth(), end.getDay()) && DateUtils.isLastDayOfMonth(scheduleInfo.getPeriodEndDate())) ||
          (!DateUtils.isValidDate(end.getYear(), end.getMonth(), start.getDay()) && DateUtils.isLastDayOfMonth(scheduleInfo.getPeriodStartDate()));
    } else {
      //ACT/ACT Normal
      return DateUtils.isLastDayOfMonth(scheduleInfo.getPeriodStartDate()) &&
          DateUtils.isLastDayOfMonth(scheduleInfo.getPeriodEndDate());
    }
  }

  @Override
  public long calculateDays(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo) {
    return DateUtils.daysBetween(firstDate, secondDate);
  }

  @Override
  public double calculateYearFraction(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo) {
    // calculation is based on the schedule period, firstDate assumed to be the start of the period
    LocalDate scheduleStartDate = scheduleInfo.getStartDate();
    LocalDate scheduleEndDate = scheduleInfo.getEndDate();
    LocalDate periodEndDate = scheduleInfo.getPeriodEndDate();
    Frequency freq = scheduleInfo.getCouponFrequency();
    boolean eom = scheduleInfo.isEndOfMonthConvention();
    if (periodEndDate.equals(scheduleEndDate)) {
      //final period calculated forward from last coupon start date, also handling single period schecules
      return forwardPeriod(secondDate, firstDate, periodEndDate, freq, eom);
    } else if (scheduleStartDate.equals(firstDate)) {
      // initial period calculated backward from first coupon end date
      return backwardPeriod(secondDate, firstDate, periodEndDate, freq, eom);
    } else {
      if (isRegularPeriod(secondDate, scheduleInfo)) {
        return regularPeriod(secondDate, firstDate, periodEndDate, freq);
      } else {
        return forwardPeriod(secondDate, firstDate, periodEndDate, freq, eom);
      }
    }
  }

  // calculate sub-periods backwards from "Coupon end date"
  private double backwardPeriod(LocalDate calcDate, LocalDate periodStartDate, LocalDate periodEndDate, Frequency freq,
      boolean eom) {
    log.debug("backward period: on={} period={}#{}", calcDate, periodStartDate, periodEndDate);
    LocalDate currEnd = periodEndDate;
    LocalDate currStart = eom(periodEndDate, freq.subtractFrom(currEnd), eom);
    double result = 0;
    while (currStart.isAfter(periodStartDate)) {
      if (calcDate.isAfter(currStart)) {
        result += calcPeriod(currStart, DateUtils.min(calcDate, currEnd), currStart, currEnd, freq);
      }
      currEnd = currStart;
      currStart = eom(periodEndDate, freq.subtractFrom(currEnd), eom);
    }
    log.info("last: {} {} {}", calcDate, currEnd, DateUtils.min(calcDate, currEnd));
    return result + calcPeriod(periodStartDate, DateUtils.min(calcDate, currEnd), currStart, currEnd, freq);
  }

  // calculate sub-periods forwards from "Coupon start date"
  private double forwardPeriod(LocalDate calcDate, LocalDate periodStartDate, LocalDate periodEndDate, Frequency freq,
      boolean eom) {
    log.debug("forward period: on={} period={}#{}", calcDate, periodStartDate, periodEndDate);
    LocalDate currStart = periodStartDate;
    LocalDate currEnd = eom(periodStartDate, freq.addTo(currStart), eom);
    double result = 0;
    while (currEnd.isBefore(periodEndDate)) {
      if (calcDate.isAfter(currStart)) {
        result += calcPeriod(currStart, DateUtils.min(calcDate, currEnd), currStart, currEnd, freq);
      }
      currStart = currEnd;
      currEnd = eom(periodStartDate, freq.addTo(currEnd), eom);
    }
    return result + calcPeriod(currStart, DateUtils.min(calcDate, currEnd), currStart, currEnd, freq);
  }

  private double regularPeriod(LocalDate calcDate, LocalDate periodStartDate, LocalDate periodEndDate, Frequency freq) {
    long actualDays = daysBetween(periodStartDate, calcDate);
    long periodDays = daysBetween(periodStartDate, periodEndDate);
    double denominator = getDenominator(freq, periodDays);
    log.debug("regular period: on={} cpn={}#{} factor={}/{}",
        calcDate,
        periodStartDate, periodEndDate,
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

  private double getDenominator(Frequency frequency, long periodDays) {
    return frequency.eventsPerYear() * periodDays;
  }

  // apply eom convention
  private LocalDate eom(LocalDate base, LocalDate calc, boolean eom) {
    return (eom && base.getDayOfMonth() == base.lengthOfMonth() ? calc.withDayOfMonth(calc.lengthOfMonth()) : calc);
  }
}
