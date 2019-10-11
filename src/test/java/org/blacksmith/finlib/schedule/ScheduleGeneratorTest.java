package org.blacksmith.finlib.schedule;

import java.time.LocalDate;
import java.time.MonthDay;
import org.blacksmith.finlib.basic.Amount;
import org.blacksmith.finlib.basic.Frequency;
import org.blacksmith.finlib.calendar.BusinessDayCalendar;
import org.blacksmith.finlib.calendar.BusinessDayCalendarWithPolicy;
import org.blacksmith.finlib.calendar.policy.HolidaySetProvider;
import org.blacksmith.finlib.calendar.policy.StandardHolidayPolicy;
import org.blacksmith.finlib.calendar.policy.WeekDaySetPolicy;
import org.blacksmith.finlib.calendar.policy.helper.StandardDateToPartConverters;
import org.blacksmith.finlib.dayconvention.StandardBusinessDayConvention;
import org.blacksmith.finlib.interestbasis.ScheduleParameters;
import org.blacksmith.finlib.interestbasis.StandardDayCountConvention;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduleGeneratorTest {
  private final Logger LOGGER = LoggerFactory.getLogger(ScheduleGeneratorTest.class);
  private ScheduleParameters createScheduleParamters1() {
    HolidaySetProvider<MonthDay> ymdProvider = new HolidaySetProvider<>(StandardDateToPartConverters.MONTH_DAY);
    ymdProvider.add(MonthDay.of(1,1));
    ymdProvider.add(MonthDay.of(5,1));
    ymdProvider.add(MonthDay.of(5,3));
    ymdProvider.add(MonthDay.of(12,25));
    ymdProvider.add(MonthDay.of(12,26));
    BusinessDayCalendar cal = new BusinessDayCalendarWithPolicy(new StandardHolidayPolicy(WeekDaySetPolicy.SAT_SUN,ymdProvider));
    return ScheduleParameters.builder()
        .firstCouponDate(LocalDate.of(2019,1,1))
        .startDate(LocalDate.of(2019,1,3))
        .maturityDate(LocalDate.of(2021,1,1))        
        .couponFrequency(Frequency.P3M)
        .basis(StandardDayCountConvention.ACT_365)
        .businessDayConvention(StandardBusinessDayConvention.FOLLOWING)
        .businessDayCalendar(cal)
        .isEndOfMonthConvention(true)
        .notional(Amount.of(1000000L))
        .build();
  }

  @Test
  public void testSchedule1() {
    ScheduleGenerator generator = ScheduleGeneratorFactory.of().getGenerator(createScheduleParamters1());
    LOGGER.info("schedule={}",generator.generate());
  }
}