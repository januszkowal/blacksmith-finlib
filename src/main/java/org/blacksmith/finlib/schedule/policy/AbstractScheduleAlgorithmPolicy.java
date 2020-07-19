package org.blacksmith.finlib.schedule.policy;

import org.blacksmith.finlib.schedule.ScheduleParameters;

public class AbstractScheduleAlgorithmPolicy {
  protected final ScheduleParameters scheduleParameters;

  public AbstractScheduleAlgorithmPolicy(ScheduleParameters scheduleParameters) {
    this.scheduleParameters = scheduleParameters;
  }

}
