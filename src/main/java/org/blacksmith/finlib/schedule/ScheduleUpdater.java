package org.blacksmith.finlib.schedule;

import java.util.List;
import java.util.function.Function;

import org.blacksmith.finlib.schedule.events.interest.CashflowInterestEvent;

public interface ScheduleUpdater extends Function<List<CashflowInterestEvent>,List<CashflowInterestEvent>> {
}
