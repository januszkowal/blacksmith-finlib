package org.blacksmith.finlib.schedule;

import java.util.List;

import org.blacksmith.finlib.interestbasis.InterestAlghoritm;
import org.blacksmith.finlib.math.solver.AlgSolverBuilder;
import org.blacksmith.finlib.rates.interestrates.InterestRateService;
import org.blacksmith.finlib.schedule.events.interest.CashflowInterestEvent;
import org.blacksmith.finlib.schedule.events.schedule.PrincipalsHolder;
import org.blacksmith.finlib.schedule.timetable.TimetableInterestEntry;
import org.blacksmith.finlib.schedule.policy.AnnuityPolicy;
import org.blacksmith.finlib.schedule.policy.NormalPolicy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScheduleGenerator {

  private final ScheduleParameters scheduleParameters;
  private final ScheduleComposePolicy schedulePolicy;
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

  private ScheduleComposePolicy createSchedulePolicy() {
    if (scheduleParameters.getAlgorithm()== InterestAlghoritm.ANNUITY) {
      return new AnnuityPolicy(
          AlgSolverBuilder.builder(AlgSolverBuilder.SolverAlgorithm.BI_SECTION),
          scheduleParameters);
    }
    else {
      return new NormalPolicy(scheduleParameters,principalHolder,interestRateService);
    }
  }

  public List<CashflowInterestEvent> create(List<TimetableInterestEntry> events) {
    return schedulePolicy.create(events);
  }
  public List<CashflowInterestEvent> update(List<CashflowInterestEvent> cashflows) {
    return schedulePolicy.update(cashflows);
  }
}
