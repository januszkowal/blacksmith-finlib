package org.blacksmith.finlib.interest.schedule.policy;

import java.util.List;
import java.util.stream.Collectors;

import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.basic.numbers.DecimalRounded;
import org.blacksmith.finlib.interest.schedule.ScheduleParameters;
import org.blacksmith.finlib.interest.schedule.events.Event;
import org.blacksmith.finlib.interest.schedule.events.InterestEvent;
import org.blacksmith.finlib.interest.schedule.policy.helper.InterestUpdater;
import org.blacksmith.finlib.interest.schedule.timetable.TimetableInterestEntry;
import org.blacksmith.finlib.math.analysis.UnivariateDifferentiableFunction;
import org.blacksmith.finlib.math.analysis.UnivariateFunction;
import org.blacksmith.finlib.math.solver.BiSectionSolverBuilder;
import org.blacksmith.finlib.math.solver.Solver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AnnuityScheduleAlgorithm extends AbstractScheduleAlgorithm implements ScheduleAlgorithm {

  private final BiSectionSolverBuilder solverBuilder;
  private final InterestUpdater interestCalculator;

  public AnnuityScheduleAlgorithm(BiSectionSolverBuilder solverBuilder, ScheduleParameters scheduleParameters) {
    super(scheduleParameters);
    this.solverBuilder = solverBuilder;
    this.interestCalculator = new InterestUpdater(scheduleParameters);
  }

  @Override
  public List<InterestEvent> create(List<TimetableInterestEntry> events) {
    var cashflows = events.stream()
        .map(se -> InterestEvent.builder()
            .startDate(se.getStartDate())
            .endDate(se.getEndDate())
            .paymentDate(se.getPaymentDate())
            .interestRate(scheduleParameters.getFixedRate())
            .build())
        .collect(Collectors.toList());
    return update(cashflows);
  }

  @Override
  public List<InterestEvent> update(List<InterestEvent> cashflows) {
    var minPmt = scheduleParameters.getPrincipal().subtract(scheduleParameters.getEndPrincipal()).doubleValue() / cashflows.size();
    var maxPmt = scheduleParameters.getPrincipal().doubleValue() / 2d;
    Solver<UnivariateFunction> solver = createSolver(cashflows, minPmt, maxPmt);
    var sf = solverFunction(cashflows);
    solver.findRoot(sf, getEstimatedPayment(cashflows));
    log.info("final :{}", Event.eventsToString(cashflows));
    return cashflows;
  }

  public Solver<UnivariateFunction> createSolver(List<InterestEvent> cashflows, double minArg, double maxArg) {
    var minPmt = scheduleParameters.getPrincipal().subtract(scheduleParameters.getEndPrincipal()).doubleValue() / cashflows.size();
    var maxPmt = scheduleParameters.getPrincipal().doubleValue() / 2d;
    return solverBuilder.tolerance(0.01d).minArg(minArg).maxArg(maxArg).maxIterations(1000).breakIfCandidateNotChanging(true).build();
  }

  public UnivariateDifferentiableFunction solverFunction(List<InterestEvent> cashflows) {
    return new UnivariateDifferentiableFunction() {

      @Override
      public int numberOfDerivatives() {
        return 1;
      }

      @Override
      public double computeDerivative(int derivativeNumber, double arg) {
        return scheduleDerivativeValue(cashflows, arg);
      }

//      @Override
//      public double alignCandidate(double arg) {
//        return Rate.of(arg, 2).doubleValue();
//      }

      @Override
      public double value(double arg) {
        return recalculateAnnuity(cashflows, arg);
      }
    };
  }

  public double recalculateAnnuity(List<InterestEvent> cashflows, double arg) {
    Amount currentPrincipal = scheduleParameters.getPrincipal();
    Amount nextPrincipal = Amount.ZERO;
    Amount principalPayment;
    Amount payment = Amount.of(arg);
    Amount interestPayment;
    for (InterestEvent cashflow : cashflows) {
      cashflow.setPrincipal(currentPrincipal);
      if (currentPrincipal.compareTo(scheduleParameters.getEndPrincipal()) > 0) {
        interestPayment = interestCalculator.calculateInterest(cashflow);
        Amount maxPrincipalPayment = currentPrincipal.subtract(scheduleParameters.getEndPrincipal());
        Amount paymentAvailable = payment.subtract(interestPayment);
        if (paymentAvailable.isNegative()) {
          principalPayment = Amount.ZERO;
          nextPrincipal = currentPrincipal.add(paymentAvailable.negate());
        } else {
          principalPayment = DecimalRounded.min(maxPrincipalPayment, paymentAvailable);
          nextPrincipal = currentPrincipal.subtract(principalPayment);
        }
      } else {
        principalPayment = Amount.ZERO;
        interestPayment = currentPrincipal.isPositive() ? interestCalculator.calculateInterest(cashflow) : Amount.ZERO;
      }
      cashflow.setInterest(interestPayment);
      cashflow.setPrincipalPayment(principalPayment);
      cashflow.setAmount(cashflow.getPrincipalPayment().add(cashflow.getInterest()));
      currentPrincipal = nextPrincipal;
    }

    Amount unpaidPrincipal = currentPrincipal.subtract(scheduleParameters.getEndPrincipal());
    if (unpaidPrincipal.isPositive()) {
      var cashflow = cashflows.get(cashflows.size() - 1);
      cashflow.setPrincipalPayment(cashflow.getPrincipalPayment().add(unpaidPrincipal));
    }
    double total = cashflows.stream()
        .mapToDouble(c -> payment.doubleValue() - c.getInterest().doubleValue() - c.getPrincipalPayment().doubleValue())
        .sum();
    log.debug("$$$ pmt amount: {} principal:{} disc: {}", arg, currentPrincipal, total);
    log.debug("schedule:{}", Event.eventsToString(cashflows));
    return total;
  }

  public double scheduleDerivativeValue(List<InterestEvent> cashflows, double arg) {
    var sumPrincipalPayment = cashflows.stream().map(InterestEvent::getPrincipal).mapToDouble(Amount::doubleValue).sum();
    var sumInterestPayment = cashflows.stream().map(InterestEvent::getInterest).mapToDouble(Amount::doubleValue).sum();
    if (sumInterestPayment == 0.0d) {
      return sumPrincipalPayment;
    } else {
      double fff = scheduleParameters.getFixedRate().doubleValue();
      return sumPrincipalPayment / (sumInterestPayment * fff);
    }
  }

  private double getEstimatedPayment(List<InterestEvent> cashflows) {
    double principal = scheduleParameters.getPrincipal().subtract(scheduleParameters.getEndPrincipal()).doubleValue();
    if (scheduleParameters.getFixedRate().isZero()) {
      return principal / cashflows.size();
    } else {
      double periodicInterestRate =
          (scheduleParameters.getFixedRate().doubleValue() / 100) / scheduleParameters.getCouponFrequency()
              .eventsPerYearEstimate();
      double x = Math.pow((1.0d + periodicInterestRate), cashflows.size());
      return principal * (periodicInterestRate * x) / (x - 1.0d);
    }
  }
}
