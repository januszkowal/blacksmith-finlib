package org.blacksmith.finlib.interest.schedule.timetable;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.basic.datetime.Frequency;
import org.blacksmith.finlib.interest.schedule.ScheduleParameters;

public class TimetableGeneratorFactory {

  public static TimetableGenerator getTimetableGenerator(ScheduleParameters scheduleParameters) {
    ArgChecker.notNull(scheduleParameters, "Schedule parameters must be not null");
    if ((scheduleParameters.getCouponFrequency() == null || scheduleParameters.getCouponFrequency() == Frequency.TERM) ||
        (scheduleParameters.getFirstCouponDate().compareTo(scheduleParameters.getMaturityDate()) >= 0)) {
      return new TermTimetableGenerator();
    } else {
      return new StandardTimetableGenerator();
    }
  }
}

