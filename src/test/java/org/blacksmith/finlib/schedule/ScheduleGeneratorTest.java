package org.blacksmith.finlib.schedule;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.List;
import java.util.stream.Collectors;
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
import org.blacksmith.finlib.interestbasis.ScheduleParameters;
import org.blacksmith.finlib.interestbasis.StandardDayCountConvention;
import org.blacksmith.finlib.schedule.events.ScheduleEvent;
import org.blacksmith.finlib.schedule.events.StandardScheduleEventsGenerator;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduleGeneratorTest {
  private final Logger LOGGER = LoggerFactory.getLogger(ScheduleGeneratorTest.class);
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
        .firstCouponDate(LocalDate.of(2019,1,1))
        .startDate(LocalDate.of(2019,1,3))
        .maturityDate(LocalDate.of(2021,1,1))        
        .couponFrequency(Frequency.P3M)
        .rateResetFrequency(Frequency.P1M)
        .basis(StandardDayCountConvention.ACT_365)
        .businessDayConvention(StandardBusinessDayConvention.FOLLOWING)
        .startInterestRate(Rate.of(5.0d))
        .businessDayCalendar(cal)
        .isEndOfMonthConvention(true)
        .principal(Amount.of(1000000L))
//        .endPrincipal(Amount.of(200000L))
        .endPrincipal(Amount.of(0L))
        .build();
  }

  @Test
  public void testSchedule1() {
    var scheduleParameters = createScheduleParameters1();
    var events = new StandardScheduleEventsGenerator().generate(scheduleParameters);
    LOGGER.info("events={}",scheduleText1(events));
    ScheduleGenerator generator = new ScheduleGenerator(scheduleParameters);
    var schedule = generator.generate(events);
    //LOGGER.info("schedule:{}",scheduleText2(schedule));
  }

  private String scheduleText1(List<ScheduleEvent> events) {
    return events.stream()
        .map(ScheduleEvent::toString)
        .collect(Collectors.joining("\n"));
  }

  private String scheduleText2(List<XEvent> events) {
    return events.stream()
        .map(XEvent::toString)
        .collect(Collectors.joining("\n"));
  }
}
