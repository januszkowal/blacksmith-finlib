package org.blacksmith.finlib.schedule;

import java.util.List;
import org.blacksmith.finlib.interestbasis.ScheduleParameters;
import org.blacksmith.finlib.schedule.events.ScheduleEvent;

public class AnnuityScheduleGenerator {

  private final ScheduleParameters scheduleParameters;
  private final List<ScheduleEvent> events;

  public AnnuityScheduleGenerator(ScheduleParameters scheduleParameters, List<ScheduleEvent> events) {
    this.scheduleParameters = scheduleParameters;
    this.events = events;
  }
  
  public void start() {
    
  }
}
