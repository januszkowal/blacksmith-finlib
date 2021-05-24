package org.blacksmith.finlib.interest.schedule.policy;

import java.util.List;
import java.util.function.Function;

import org.blacksmith.finlib.interest.schedule.events.InterestEvent;

public interface ScheduleUpdater extends Function<List<InterestEvent>, List<InterestEvent>> {
}
