package org.blacksmith.finlib.schedule.policy;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.basic.numbers.DecimalRounded;
import org.blacksmith.finlib.interestbasis.ScheduleParameters;
import org.blacksmith.finlib.math.solver.SolverFunction1stDerivative;
import org.blacksmith.finlib.math.solver.NewtonRaphsonSolverBuilder;
import org.blacksmith.finlib.math.solver.Solver;
import org.blacksmith.finlib.schedule.SchedulePolicy;
import org.blacksmith.finlib.schedule.XEvent;

@Slf4j
public class AnnuityPolicy extends AbstractPolicy implements SchedulePolicy {

  public AnnuityPolicy(ScheduleParameters scheduleParameters, List<XEvent> cashflows) {
    super(scheduleParameters, cashflows);
  }

  @Override
  public void update() {
    var solver = createSolver();
    solver.solve(solverFunction(), 0.00d, getEstimatedPayment());
    log.info("final :{}", cashflowsToString());
  }

  public Solver<SolverFunction1stDerivative> createSolver() {
//      public Solver<Function> createSolver() {
    return
//            return
//              BiSectionSolverBuilder.builder()
        NewtonRaphsonSolverBuilder.builder()
//                    .minArg(1.0d)
//                    .maxArg(scheduleParameters.getPrincipal().doubleValue())
            .tolerance(0.01d)
            .iterations(1000)
            .argAligner((a) -> Amount.of(a).doubleValue())
            .breakIfTheSameCandidate(true)
            .build();
  }

  public SolverFunction1stDerivative solverFunction() {
    return new SolverFunction1stDerivative() {
      @Override
      public double derivative(double arg) {
        return scheduleDerivativeValue(arg);
      }

      @Override
      public double value(double arg) {
        return recalculateAnnuity(arg);
      }
    };
  }

  public double recalculateAnnuity(double arg) {
    Amount currentPrincipal = scheduleParameters.getPrincipal();
    Amount nextPrincipal = Amount.ZERO;
    Amount principalPayment = Amount.ZERO;
    Amount payment = Amount.of(arg);
    Amount interestPayment = Amount.ZERO;
    for (int i = 0; i < cashflows.size(); i++) {
      var cashflow = cashflows.get(i);
      cashflow.setPrincipal(currentPrincipal);
      if (currentPrincipal.compareTo(scheduleParameters.getEndPrincipal())>0) {
        interestPayment = calculateCouponInterest(cashflow);
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
        interestPayment = Amount.ZERO;
        principalPayment = Amount.ZERO;
      }
      cashflow.setInterestPayment(interestPayment);
      cashflow.setPrincipalPayment(principalPayment);
      cashflow.setAmount(cashflow.getPrincipalPayment().add(cashflow.getInterestPayment()));
      currentPrincipal = nextPrincipal;
    }

    Amount unpaidPrincipal = currentPrincipal.subtract(scheduleParameters.getEndPrincipal());
    if (unpaidPrincipal.isPositive()) {
      var cashflow = cashflows.get(cashflows.size() - 1);
      cashflow.setPrincipalPayment(cashflow.getPrincipalPayment().add(unpaidPrincipal));
    }
    double total = cashflows.stream()
        .mapToDouble(c->payment.doubleValue()-c.getInterestPayment().doubleValue()-c.getPrincipalPayment().doubleValue())
        .sum();
    log.debug("$$$ pmt amount: {} principal:{} disc: {}", arg, currentPrincipal, total);
    log.debug("schedule:{}", cashflowsToString());
    return total;
  }

  public double scheduleDerivativeValue(double arg) {
    var sumPrincipalPayment = cashflows.stream().map(XEvent::getPrincipal).mapToDouble(Amount::doubleValue).sum();
    var sumInterestPayment = cashflows.stream().map(XEvent::getInterestPayment).mapToDouble(Amount::doubleValue).sum();
    if (sumInterestPayment==0.0d) {
      return sumPrincipalPayment;
    } else {
      double fff = scheduleParameters.getStartInterestRate().doubleValue();
      return sumPrincipalPayment / (sumInterestPayment*fff);
    }
  }

  private double getEstimatedPayment() {
    double principal = scheduleParameters.getPrincipal().subtract(scheduleParameters.getEndPrincipal()).doubleValue();
    if (scheduleParameters.getStartInterestRate().isZero()) {
      return principal / cashflows.size();
    } else {
      double periodicInterestRate =
          (scheduleParameters.getStartInterestRate().doubleValue() / 100) / scheduleParameters.getCouponFrequency()
              .eventsPerYearEstimate();
      double x = Math.pow((1.0d + periodicInterestRate), cashflows.size());
      return principal * (periodicInterestRate * x) / (x - 1.0d);
    }
  }
}
