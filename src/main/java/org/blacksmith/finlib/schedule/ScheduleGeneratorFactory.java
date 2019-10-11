package org.blacksmith.finlib.schedule;

import org.blacksmith.finlib.basic.Frequency;
import org.blacksmith.finlib.interestbasis.ScheduleParameters;

public class ScheduleGeneratorFactory {
  public static ScheduleGeneratorFactory of() {
    return new ScheduleGeneratorFactory();
  }

  public ScheduleGenerator getGenerator(ScheduleParameters scheduleParameters) {
    if ((scheduleParameters.getCouponFrequency() == null || scheduleParameters.getCouponFrequency() == Frequency.TERM) || 
        (scheduleParameters.getFirstCouponDate().compareTo(scheduleParameters.getMaturityDate()) >= 0))
    {
      return new TermScheduleGenerator(scheduleParameters);
    } 
    else
      return new StandardScheduleGenerator(scheduleParameters);
  }
}
