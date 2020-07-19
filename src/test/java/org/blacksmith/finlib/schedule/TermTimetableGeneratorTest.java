package org.blacksmith.finlib.schedule;

import java.time.LocalDate;

import org.blacksmith.finlib.basic.datetime.Frequency;
import org.blacksmith.finlib.interestbasis.InterestAlghoritm;
import org.blacksmith.finlib.schedule.timetable.TermTimetableGenerator;
import org.blacksmith.finlib.schedule.timetable.TimetableGeneratorFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TermTimetableGeneratorTest {
  @Test
  public void termSchedule1() {
    var scheduleParameters = ScheduleParameters.builder()
        .algorithm(InterestAlghoritm.NORMAL)
        .firstCouponDate(LocalDate.of(2019,1,1))
        .startDate(LocalDate.of(2019,1,3))
        .maturityDate(LocalDate.of(2019,2,1))
        .couponFrequency(Frequency.TERM)
        .build();
    var generator = TimetableGeneratorFactory.getTimetableGenerator(scheduleParameters);
    assertEquals(TermTimetableGenerator.class,generator.getClass());
    var schedule = generator.generate(scheduleParameters);
    assertEquals(1,schedule.size());
    assertEquals(scheduleParameters.getStartDate(),schedule.get(0).getStartDate());
    assertEquals(scheduleParameters.getMaturityDate(),schedule.get(0).getEndDate());
    assertEquals(scheduleParameters.getMaturityDate(),schedule.get(0).getPaymentDate());
  }

  @Test
  public void termSchedule2() {
    var scheduleParameters = ScheduleParameters.builder()
        .algorithm(InterestAlghoritm.NORMAL)
        .firstCouponDate(LocalDate.of(2019,2,1))
        .startDate(LocalDate.of(2019,2,1))
        .maturityDate(LocalDate.of(2019,2,1))
        .couponFrequency(Frequency.P1W)
        .build();
    var generator = TimetableGeneratorFactory.getTimetableGenerator(scheduleParameters);
    assertEquals(TermTimetableGenerator.class,generator.getClass());
    var schedule = generator.generate(scheduleParameters);
    assertEquals(1,schedule.size());
    assertEquals(scheduleParameters.getStartDate(),schedule.get(0).getStartDate());
    assertEquals(scheduleParameters.getMaturityDate(),schedule.get(0).getEndDate());
    assertEquals(scheduleParameters.getMaturityDate(),schedule.get(0).getPaymentDate());
  }

  @Test
  public void termSchedule3() {
    var scheduleParameters = ScheduleParameters.builder()
        .algorithm(InterestAlghoritm.NORMAL)
        .firstCouponDate(LocalDate.of(2019,1,1))
        .startDate(LocalDate.of(2019,1,3))
        .maturityDate(LocalDate.of(2019,2,1))
        .couponFrequency(null)
        .build();
    var generator = TimetableGeneratorFactory.getTimetableGenerator(scheduleParameters);
    assertEquals(TermTimetableGenerator.class,generator.getClass());
    var schedule = generator.generate(scheduleParameters);
    assertEquals(1,schedule.size());
    assertEquals(scheduleParameters.getStartDate(),schedule.get(0).getStartDate());
    assertEquals(scheduleParameters.getMaturityDate(),schedule.get(0).getEndDate());
    assertEquals(scheduleParameters.getMaturityDate(),schedule.get(0).getPaymentDate());
  }
}
