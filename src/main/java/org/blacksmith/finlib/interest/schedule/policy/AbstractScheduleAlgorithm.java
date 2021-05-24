package org.blacksmith.finlib.interest.schedule.policy;

import org.blacksmith.finlib.interest.schedule.ScheduleParameters;

public class AbstractScheduleAlgorithm {
  protected final ScheduleParameters scheduleParameters;

  public AbstractScheduleAlgorithm(ScheduleParameters scheduleParameters) {
    this.scheduleParameters = scheduleParameters;
  }

}
