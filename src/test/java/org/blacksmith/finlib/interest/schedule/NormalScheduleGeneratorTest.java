package org.blacksmith.finlib.interest.schedule;

import java.time.LocalDate;
import java.time.MonthDay;

import org.blacksmith.finlib.basic.currency.Currency;
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
import org.blacksmith.finlib.rate.intrate.InterestRateService;
import org.blacksmith.finlib.interest.schedule.principal.PrincipalsHolder;
import org.blacksmith.finlib.interest.schedule.timetable.StandardTimetableGenerator;
import org.blacksmith.finlib.interest.schedule.timetable.TimetableGeneratorFactory;
import org.blacksmith.finlib.rate.intrate.InterestRateService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@Slf4j
public class NormalScheduleGeneratorTest {
  public InterestRateService createInterestRateService1() {
    var interestRateService = Mockito.mock(InterestRateService.class);
    //Mockito.when(interestRateService.getRate(any(),any())).thenReturn(BasicMarketData.of(LocalDate.now(),Rate.of(3d)));
    Mockito.when(interestRateService.getRateValue(any(), any())).thenReturn(Rate.of(3d));
    return interestRateService;
  }

  @Test
  public void schedule1() {
    var interestRateService = createInterestRateService1();
    var scheduleParameters = createScheduleParameters1();
    var timetableGenerator = TimetableGeneratorFactory.getTimetableGenerator(scheduleParameters);
    assertEquals(StandardTimetableGenerator.class, timetableGenerator.getClass());
    var timetable = timetableGenerator.generate(scheduleParameters);
    assertEquals(8, timetable.size());
    PrincipalsHolder principalsHolder = new PrincipalsHolder(scheduleParameters.getPrincipal());
    ScheduleGenerator scheduleGenerator = new ScheduleGenerator(scheduleParameters,
        interestRateService,
        principalsHolder);
    var schedule = scheduleGenerator.create(timetable);
    assertEquals(8, schedule.size());
    //    assertEquals(3,schedule.get(0).getInterestRate());
    log.info("schedule1: {}", schedule);
    Mockito.when(interestRateService.getRateValue(any(), any()))
        .thenReturn(Rate.of(2d));
    var schedule2 = scheduleGenerator.update(schedule);
    log.info("schedule2: {}", schedule);
  }

  private ScheduleParameters createScheduleParameters1() {
    DatePartProvider<MonthDay> hyc = DatePartInMemoryProvider.of(MonthDay.of(1, 1),
        MonthDay.of(5, 1),
        MonthDay.of(5, 3),
        MonthDay.of(12, 25),
        MonthDay.of(12, 26));
    DatePartHolidayPolicy<MonthDay> ymdProvider = new DatePartHolidayPolicy<>(MonthDayExtractor.getInstance(), hyc);

    BusinessDayCalendar cal = new BusinessDayCalendarWithPolicy(CombinedHolidayPolicy.of(StandardWeekDayPolicy.SAT_SUN, ymdProvider));
    return ScheduleParameters.builder()
        .algorithm(InterestAlgorithm.SIMPLE)
        .currency(Currency.PLN)
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
        .indexation(org.blacksmith.finlib.interest.schedule.InterestRateIndexation.FLOATING)
        .interestTable("LIBOR")
        .build();
  }

}
