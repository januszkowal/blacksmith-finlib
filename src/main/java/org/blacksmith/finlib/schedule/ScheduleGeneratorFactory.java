package org.blacksmith.finlib.schedule;

import java.util.List;

import org.blacksmith.finlib.basic.datetime.Frequency;
import org.blacksmith.finlib.interestbasis.InterestAlghoritm;
import org.blacksmith.finlib.interestbasis.ScheduleParameters;
import org.blacksmith.finlib.schedule.events.ScheduleEventsGenerator;
import org.blacksmith.finlib.schedule.events.StandardScheduleEventsGenerator;
import org.blacksmith.finlib.schedule.events.TermScheduleEventsGenerator;
import org.blacksmith.finlib.schedule.policy.AnnuityPolicy;

public class ScheduleGeneratorFactory {
//  public static ScheduleGeneratorFactory of() {
//    return new ScheduleGeneratorFactory();
//  }

  public static ScheduleEventsGenerator getEventsGenerator(ScheduleParameters scheduleParameters) {
    if ((scheduleParameters.getCouponFrequency() == null || scheduleParameters.getCouponFrequency() == Frequency.TERM) ||
        (scheduleParameters.getFirstCouponDate().compareTo(scheduleParameters.getMaturityDate()) >= 0))
    {
      return new TermScheduleEventsGenerator();
    } 
    else
      return new StandardScheduleEventsGenerator();
  }

  public static SchedulePolicy getSchedulePolicy(ScheduleParameters scheduleParameters,
      List<XEvent> schedule) {
    if (scheduleParameters.getAlgorithm()== InterestAlghoritm.ANNUITY) {
      return new AnnuityPolicy(scheduleParameters, schedule);
    }
    return null;
  }
}
