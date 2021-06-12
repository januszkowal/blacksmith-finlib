package org.blacksmith.finlib.interest.schedule;

import java.time.LocalDate;
import java.time.MonthDay;

import org.blacksmith.finlib.basic.datetime.Frequency;
import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.calendar.BusinessDayCalendar;
import org.blacksmith.finlib.calendar.BusinessDayCalendarWithPolicy;
import org.blacksmith.finlib.calendar.policy.HolidayPolicyComposite;
import org.blacksmith.finlib.calendar.policy.DatePartHolidayPolicy;
import org.blacksmith.finlib.calendar.policy.StandardWeekDayPolicy;
import org.blacksmith.finlib.calendar.provider.DatePartInMemoryProvider;
import org.blacksmith.finlib.calendar.provider.DatePartProvider;
import org.blacksmith.finlib.calendar.extractor.MonthDayExtractor;
import org.blacksmith.finlib.dayconvention.StandardBusinessDayConvention;
import org.blacksmith.finlib.interest.basis.InterestAlgorithm;
import org.blacksmith.finlib.interest.basis.StandardDayCounts;
import org.blacksmith.finlib.interest.schedule.timetable.StandardTimetableGenerator;
import org.blacksmith.finlib.interest.schedule.timetable.TimetableGeneratorFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StandardTimetableGeneratorTest {
  @Test
  public void schedule1() {
    var scheduleParameters = createScheduleParameters1();
    var generator = TimetableGeneratorFactory.getTimetableGenerator(scheduleParameters);
    assertEquals(StandardTimetableGenerator.class, generator.getClass());
    var schedule = generator.generate(scheduleParameters);
    assertEquals(8, schedule.size());
  }

  private ScheduleParameters createScheduleParameters1() {
    DatePartProvider<MonthDay> hyc = DatePartInMemoryProvider.of(MonthDay.of(1, 1),
        MonthDay.of(5, 1),
        MonthDay.of(5, 3),
        MonthDay.of(12, 25),
        MonthDay.of(12, 26));
    DatePartHolidayPolicy<MonthDay> ymdProvider = new DatePartHolidayPolicy<>(MonthDayExtractor.getInstance(), hyc);

    BusinessDayCalendar cal = BusinessDayCalendarWithPolicy.of(HolidayPolicyComposite.of(StandardWeekDayPolicy.SAT_SUN, ymdProvider));
    return ScheduleParameters.builder()
        .algorithm(InterestAlgorithm.SIMPLE)
        .firstCouponDate(LocalDate.of(2019, 1, 1))
        .startDate(LocalDate.of(2019, 1, 3))
        .maturityDate(LocalDate.of(2021, 1, 1))
        .couponFrequency(Frequency.P3M)
        .rateResetFrequency(Frequency.P1M)
        .basis(StandardDayCounts.ACT_365)
        .businessDayConvention(StandardBusinessDayConvention.FOLLOWING)
        .fixedRate(Rate.of(5.0d))
        .businessDayCalendar(cal)
        .isEndOfMonthConvention(true)
        .principal(Amount.of(1000000L))
        //        .endPrincipal(Amount.of(200000L))
        .endPrincipal(Amount.of(0L))
        .build();
  }
}
