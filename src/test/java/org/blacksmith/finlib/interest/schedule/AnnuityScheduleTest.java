package org.blacksmith.finlib.interest.schedule;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.List;
import java.util.stream.Collectors;

import org.blacksmith.finlib.basic.datetime.Frequency;
import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.calendar.BusinessDayCalendar;
import org.blacksmith.finlib.calendar.BusinessDayCalendarWithPolicy;
import org.blacksmith.finlib.calendar.policy.CombinedHolidayPolicy;
import org.blacksmith.finlib.calendar.policy.DatePartHolidayPolicy;
import org.blacksmith.finlib.calendar.policy.StandardWeekDayPolicy;
import org.blacksmith.finlib.calendar.policy.helper.DatePartInMemoryProvider;
import org.blacksmith.finlib.calendar.policy.helper.DatePartProvider;
import org.blacksmith.finlib.calendar.policy.helper.MonthDayExtractor;
import org.blacksmith.finlib.dayconvention.StandardBusinessDayConvention;
import org.blacksmith.finlib.interest.basis.InterestAlgorithm;
import org.blacksmith.finlib.interest.basis.StandardInterestBasis;
import org.blacksmith.finlib.interest.schedule.events.Event;
import org.blacksmith.finlib.interest.schedule.principal.PrincipalUpdatePolicyByAmount;
import org.blacksmith.finlib.interest.schedule.principal.PrincipalsGenerator;
import org.blacksmith.finlib.interest.schedule.principal.PrincipalsHolder;
import org.blacksmith.finlib.interest.schedule.timetable.StandardTimetableGenerator;
import org.blacksmith.finlib.interest.schedule.timetable.TimetableInterestEntry;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnuityScheduleTest {
  private final Logger LOGGER = LoggerFactory.getLogger(AnnuityScheduleTest.class);

  @Test
  public void testSchedule1() {
    var scheduleParameters = createScheduleParameters1();
    var scheduleInterestEvents = new StandardTimetableGenerator().generate(scheduleParameters);
    //
    PrincipalsHolder ph;
    if (scheduleParameters.getAlgorithm() == InterestAlgorithm.DECREASING_PRINCIPAL) {
      var pg = new PrincipalsGenerator(false, false);
      var startDates = Event.getDates(scheduleInterestEvents, TimetableInterestEntry::getStartDate);
      var principalResetDates = pg.generate(scheduleParameters.getPrincipal(),
          startDates, new PrincipalUpdatePolicyByAmount(scheduleParameters.getEndPrincipal(), Amount.TEN));
      ph = new PrincipalsHolder(scheduleParameters.getPrincipal(), principalResetDates);
    } else {
      ph = new PrincipalsHolder(scheduleParameters.getPrincipal());
    }
    LOGGER.info("events={}", scheduleText1(scheduleInterestEvents));
    ScheduleGenerator generator = new ScheduleGenerator(scheduleParameters, null, ph);
    var schedule = generator.create(scheduleInterestEvents);
    //LOGGER.info("schedule:{}",scheduleText2(schedule));
  }

  private ScheduleParameters createScheduleParameters1() {
    DatePartProvider<MonthDay> hyc = DatePartInMemoryProvider.of(MonthDay.of(1, 1),
        MonthDay.of(5, 1),
        MonthDay.of(5, 3),
        MonthDay.of(12, 25),
        MonthDay.of(12, 26));
    DatePartHolidayPolicy<MonthDay> ymdProvider = new DatePartHolidayPolicy<>(MonthDayExtractor.getInstance(), hyc);

    BusinessDayCalendar cal = new BusinessDayCalendarWithPolicy(
        CombinedHolidayPolicy.of(StandardWeekDayPolicy.SAT_SUN, ymdProvider));
    return ScheduleParameters.builder()
        .algorithm(InterestAlgorithm.ANNUITY)
        .firstCouponDate(LocalDate.of(2019, 1, 1))
        .startDate(LocalDate.of(2019, 1, 3))
        .maturityDate(LocalDate.of(2021, 1, 1))
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

  private String scheduleText1(List<TimetableInterestEntry> events) {
    return events.stream()
        .map(TimetableInterestEntry::toString)
        .collect(Collectors.joining("\n"));
  }

}
