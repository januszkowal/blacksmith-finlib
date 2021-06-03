package org.blacksmith.finlib.interest.schedule;

import java.util.List;

import org.blacksmith.finlib.interest.basis.InterestAlgorithm;
import org.blacksmith.finlib.rate.intrate.InterestRateService;
import org.blacksmith.finlib.interest.schedule.events.InterestEvent;
import org.blacksmith.finlib.interest.schedule.policy.AnnuityScheduleAlgorithm;
import org.blacksmith.finlib.interest.schedule.policy.ScheduleAlgorithm;
import org.blacksmith.finlib.interest.schedule.policy.StandardScheduleAlgorithm;
import org.blacksmith.finlib.interest.schedule.principal.PrincipalsHolder;
import org.blacksmith.finlib.interest.schedule.timetable.TimetableInterestEntry;
import org.blacksmith.finlib.math.solver.BiSectionSolverBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScheduleGenerator {

  private final ScheduleParameters scheduleParameters;
  private final ScheduleAlgorithm schedulePolicy;
  private final InterestRateService interestRateService;
  private final PrincipalsHolder principalHolder;

  public ScheduleGenerator(ScheduleParameters scheduleParameters,
      InterestRateService interestRateService,
      PrincipalsHolder principalsHolder) {
    this.scheduleParameters = scheduleParameters;
    this.interestRateService = interestRateService;
    this.principalHolder = principalsHolder;
    this.schedulePolicy = createSchedulePolicy();
  }

  public List<InterestEvent> create(List<TimetableInterestEntry> events) {
    return schedulePolicy.create(events);
  }

  public List<InterestEvent> update(List<InterestEvent> cashflows) {
    return schedulePolicy.update(cashflows);
  }

  private ScheduleAlgorithm createSchedulePolicy() {
    if (scheduleParameters.getAlgorithm() == InterestAlgorithm.ANNUITY) {
      return new AnnuityScheduleAlgorithm(
          BiSectionSolverBuilder.builder(),
          scheduleParameters);
    } else {
      return new StandardScheduleAlgorithm(scheduleParameters, principalHolder, interestRateService);
    }
  }
}
