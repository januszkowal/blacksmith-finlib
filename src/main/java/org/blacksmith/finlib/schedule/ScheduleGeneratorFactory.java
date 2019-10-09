package org.blacksmith.finlib.schedule;

import org.blacksmith.finlib.basic.Frequency;
import org.blacksmith.finlib.interestbasis.ScheduleParameters;

public class ScheduleGeneratorFactory {
  public ScheduleGenerator getGenerator(ScheduleParameters parameters) {
    if (parameters.getCouponFrequency()==null || parameters.getCouponFrequency()== Frequency.TERM) {
      return new TermScheduleGenerator();
    }
    else
      return new StandardScheduleGenerator();
  }
}
