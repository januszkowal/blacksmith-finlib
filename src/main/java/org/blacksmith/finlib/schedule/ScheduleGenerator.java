package org.blacksmith.finlib.schedule;

import java.util.List;

import org.blacksmith.finlib.interestbasis.InterestAlgoritm;
import org.blacksmith.finlib.math.solver.AlgSolverBuilder;
import org.blacksmith.finlib.rates.interestrates.InterestRateService;
import org.blacksmith.finlib.schedule.events.InterestEvent;
import org.blacksmith.finlib.schedule.policy.AnnuityScheduleAlgorithm;
import org.blacksmith.finlib.schedule.policy.ScheduleAlgorithm;
import org.blacksmith.finlib.schedule.policy.StandardScheduleAlgorithm;
import org.blacksmith.finlib.schedule.principal.PrincipalsHolder;
import org.blacksmith.finlib.schedule.timetable.TimetableInterestEntry;

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
    if (scheduleParameters.getAlgorithm() == InterestAlgoritm.ANNUITY) {
      return new AnnuityScheduleAlgorithm(
          AlgSolverBuilder.builder(AlgSolverBuilder.SolverAlgorithm.BI_SECTION),
          scheduleParameters);
    } else {
      return new StandardScheduleAlgorithm(scheduleParameters, principalHolder, interestRateService);
    }
  }
}
