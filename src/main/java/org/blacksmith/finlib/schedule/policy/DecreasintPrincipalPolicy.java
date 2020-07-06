package org.blacksmith.finlib.schedule.policy;

import java.math.BigDecimal;
import java.util.List;

import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.basic.numbers.DecimalRounded;
import org.blacksmith.finlib.interestbasis.ScheduleParameters;
import org.blacksmith.finlib.schedule.SchedulePolicy;
import org.blacksmith.finlib.schedule.XEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DecreasintPrincipalPolicy extends AbstractPolicy implements SchedulePolicy {

  public DecreasintPrincipalPolicy(ScheduleParameters scheduleParameters, List<XEvent> cashflows) {
    super(scheduleParameters, cashflows);
  }

  @Override
  public void update() {
    BigDecimal sum = BigDecimal.ZERO;
    Amount currentPrincipal = scheduleParameters.getPrincipal();
    Amount amountToPayOff = scheduleParameters.getPrincipal().subtract(scheduleParameters.getEndPrincipal());
    Amount couponPayOff = amountToPayOff.divide(cashflows.size());
    for (int i = 0; i < cashflows.size(); i++) {
      var cashflow = cashflows.get(i);
      cashflow.setPrincipal(currentPrincipal);
      if (amountToPayOff.isPositive()) {
        Amount principalPayOff = DecimalRounded.min(amountToPayOff, couponPayOff);
        cashflow.setPrincipal(currentPrincipal);
        cashflow.setPrincipalPayment(principalPayOff);
        currentPrincipal = currentPrincipal.subtract(principalPayOff);
      }
      else {
        cashflow.setPrincipalPayment(Amount.ZERO);
      }
      if (cashflow.getPrincipal().isPositive()) {
        cashflow.setInterestPayment(calculateCouponInterest(cashflow));
      }
      else {
        cashflow.setInterestPayment(Amount.ZERO);
      }
    }
  }
}
