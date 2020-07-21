package org.blacksmith.finlib.schedule.policy;

import org.blacksmith.finlib.schedule.ScheduleParameters;

public class AbstractScheduleAlgorithm {
  protected final ScheduleParameters scheduleParameters;

  public AbstractScheduleAlgorithm(ScheduleParameters scheduleParameters) {
    this.scheduleParameters = scheduleParameters;
  }

}
