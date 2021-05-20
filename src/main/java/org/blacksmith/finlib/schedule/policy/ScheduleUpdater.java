package org.blacksmith.finlib.schedule.policy;

import java.util.List;
import java.util.function.Function;

import org.blacksmith.finlib.schedule.events.InterestEvent;

public interface ScheduleUpdater extends Function<List<InterestEvent>, List<InterestEvent>> {
}
