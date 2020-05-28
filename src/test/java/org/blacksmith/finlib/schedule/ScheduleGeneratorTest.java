package org.blacksmith.finlib.schedule;

import java.time.LocalDate;
import java.time.MonthDay;
import org.blacksmith.finlib.calendar.BusinessDayCalendar;
import org.blacksmith.finlib.calendar.BusinessDayCalendarWithPolicy;
import org.blacksmith.finlib.calendar.policy.CombinedHolidayPolicy;
import org.blacksmith.finlib.calendar.policy.lookup.HolidayLookupContainer;
import org.blacksmith.finlib.calendar.policy.HolidayLookupPolicy;
import org.blacksmith.finlib.calendar.policy.WeekDayPolicy;
import org.blacksmith.finlib.calendar.helper.StandardDateToPartConverters;
import org.blacksmith.finlib.datetime.Frequency;
import org.blacksmith.finlib.dayconvention.StandardBusinessDayConvention;
import org.blacksmith.finlib.interestbasis.ScheduleParameters;
import org.blacksmith.finlib.interestbasis.StandardDayCountConvention;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.blacksmith.finlib.basic.Amount;

public class ScheduleGeneratorTest {
  private final Logger LOGGER = LoggerFactory.getLogger(ScheduleGeneratorTest.class);
  private ScheduleParameters createScheduleParameters1() {
    HolidayLookupContainer<MonthDay> hyc = HolidayLookupContainer.of(MonthDay.of(1,1),
      MonthDay.of(5,1),
      MonthDay.of(5,3),
      MonthDay.of(12,25),
      MonthDay.of(12,26));
    HolidayLookupPolicy<MonthDay> ymdProvider = new HolidayLookupPolicy<>(StandardDateToPartConverters.MONTH_DAY,hyc);

    BusinessDayCalendar cal = new BusinessDayCalendarWithPolicy(CombinedHolidayPolicy.of(WeekDayPolicy.SAT_SUN,ymdProvider));
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
    ScheduleGenerator generator = ScheduleGeneratorFactory.of().getGenerator(createScheduleParameters1());
    LOGGER.info("schedule={}",generator.generate());
  }
}
