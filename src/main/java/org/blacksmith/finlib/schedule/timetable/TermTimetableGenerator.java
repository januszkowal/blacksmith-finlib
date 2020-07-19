package org.blacksmith.finlib.schedule.timetable;

import java.util.Collections;
import java.util.List;
import org.blacksmith.finlib.schedule.ScheduleParameters;

public class TermTimetableGenerator implements TimetableGenerator {

  @Override
  public List<TimetableInterestEntry> generate(ScheduleParameters scheduleParameters) {
    return Collections.singletonList(TimetableInterestEntry.builder()
        .startDate(scheduleParameters.getStartDate())
        .endDate(scheduleParameters.getMaturityDate())
        .paymentDateUnadjusted(scheduleParameters.getMaturityDate())
        .paymentDate(scheduleParameters.getMaturityDate())
        .build());
  }
}
