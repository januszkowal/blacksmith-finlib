package org.blacksmith.finlib.schedule;

import java.time.LocalDate;
import java.time.MonthDay;

import org.blacksmith.finlib.basic.calendar.BusinessDayCalendar;
import org.blacksmith.finlib.basic.calendar.BusinessDayCalendarWithPolicy;
import org.blacksmith.finlib.basic.calendar.policy.CombinedHolidayPolicy;
import org.blacksmith.finlib.basic.calendar.policy.DatePartHolidayPolicy;
import org.blacksmith.finlib.basic.calendar.policy.StandardWeekDayPolicy;
import org.blacksmith.finlib.basic.calendar.policy.helper.DatePartInMemoryProvider;
import org.blacksmith.finlib.basic.calendar.policy.helper.DatePartProvider;
import org.blacksmith.finlib.basic.calendar.policy.helper.StandardDatePartExtractors;
import org.blacksmith.finlib.basic.datetime.Frequency;
import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.dayconvention.StandardBusinessDayConvention;
import org.blacksmith.finlib.interestbasis.InterestAlgoritm;
import org.blacksmith.finlib.interestbasis.StandardInterestBasis;
import org.blacksmith.finlib.schedule.timetable.StandardTimetableGenerator;
import org.blacksmith.finlib.schedule.timetable.TimetableGeneratorFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StandardTimetableGeneratorTest {
  private ScheduleParameters createScheduleParameters1() {
    DatePartProvider<MonthDay> hyc = DatePartInMemoryProvider.of(MonthDay.of(1,1),
        MonthDay.of(5,1),
        MonthDay.of(5,3),
        MonthDay.of(12,25),
        MonthDay.of(12,26));
    DatePartHolidayPolicy<MonthDay> ymdProvider = new DatePartHolidayPolicy<>(StandardDatePartExtractors.MONTH_DAY,hyc);

    BusinessDayCalendar cal = new BusinessDayCalendarWithPolicy(
        CombinedHolidayPolicy.of(StandardWeekDayPolicy.SAT_SUN,ymdProvider));
    return ScheduleParameters.builder()
        .algorithm(InterestAlgoritm.SIMPLE)
        .firstCouponDate(LocalDate.of(2019,1,1))
        .startDate(LocalDate.of(2019,1,3))
        .maturityDate(LocalDate.of(2021,1,1))
        .couponFrequency(Frequency.P3M)
        .rateResetFrequency(Frequency.P1M)
        .basis(StandardInterestBasis.ACT_365)
        .businessDayConvention(StandardBusinessDayConvention.FOLLOWING)
        .fixedRate(Rate.of(5.0d))
        .businessDayCalendar(cal)
        .isEndOfMonthConvention(true)
        .principal(Amount.of(1000000L))
        //        .endPrincipal(Amount.of(200000L))
        .endPrincipal(Amount.of(0L))
        .build();
  }

  @Test
  public void schedule1() {
    var scheduleParameters = createScheduleParameters1();
    var generator = TimetableGeneratorFactory.getTimetableGenerator(scheduleParameters);
    assertEquals(StandardTimetableGenerator.class,generator.getClass());
    var schedule = generator.generate(scheduleParameters);
    assertEquals(8,schedule.size());
  }
}
