package org.blacksmith.finlib.schedule.events.interest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.blacksmith.commons.datetime.DateRange;
import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.schedule.events.InterestEvent;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class CashflowInterestEvent implements InterestEvent {
  @NonNull
  LocalDate startDate;
  @NonNull
  LocalDate endDate;
  @NonNull
  LocalDate paymentDate;
  @NonNull
  @Builder.Default
  Amount principal = Amount.ZERO;
  @NonNull
  @Builder.Default
  Rate interestRate = Rate.ZERO;
  @NonNull
  @Builder.Default
  Amount interest = Amount.ZERO;
  @NonNull
  @Builder.Default
  Amount amount = Amount.ZERO;
  @NonNull
  @Builder.Default
  Amount principalPayment = Amount.ZERO;

  @Builder.Default
  List<RateResetEvent> subEvents = new ArrayList<>();

  public CashflowInterestEvent copy() {
    return builder()
        .startDate(startDate)
        .endDate(endDate)
        .paymentDate(paymentDate)
        .principal(principal)
        .interestRate(interestRate)
        .interest(interest)
        .amount(amount)
        .principalPayment(principalPayment)
        .subEvents(subEvents.stream().map(RateResetEvent::copy).collect(Collectors.toList()))
        .build();
  }

  public RateResetEvent getSubEventInRange(LocalDate date) {
    return subEvents.stream()
        .filter(ie-> DateRange.closedOpen(ie.getStartDate(),ie.getEndDate()).contains(date))
        .findFirst().orElse(null);
  }

  public RateResetEvent getSubEvent(LocalDate date) {
    return subEvents.stream()
        .filter(ie->ie.getStartDate().equals(date))
        .findFirst().orElse(null);
  }

  public boolean splitSubEvent(LocalDate splitDate) {
    RateResetEvent rateReset1 = getSubEventInRange(splitDate);
    if (rateReset1 != null && !rateReset1.getStartDate().isEqual(splitDate)) {
      RateResetEvent rateReset2 = RateResetEvent.builder()
          .startDate(splitDate)
          .endDate(rateReset1.getEndDate())
          .interestRate(rateReset1.getInterestRate())
          .isRateReset(false)
          .build();
      rateReset1.setEndDate(splitDate);
      int idx = subEvents.indexOf(rateReset1);
      subEvents.add(idx+1,rateReset2);
      return true;
    }
    return false;
  }

  public boolean consolidateSubEvent(LocalDate date) {
    if (date.isAfter(this.startDate) && subEvents.size()>1) {
      RateResetEvent rateReset2 = getSubEvent(date);
      int idx = subEvents.indexOf(rateReset2);
      RateResetEvent rateReset1 = subEvents.get(idx-1);
      rateReset1.setEndDate(rateReset2.getEndDate());
      subEvents.remove(idx);
      return true;
    }
    return false;
  }

  public RateResetEvent firstRateReset() {
    return subEvents.isEmpty() ? null : subEvents.get(0);
  }

  public static List<CashflowInterestEvent> copy(List<CashflowInterestEvent> src) {
    return src.stream().map(CashflowInterestEvent::copy).collect(Collectors.toList());
  }

  public boolean setPrincipal(Amount principal) {
    boolean result = this.principal.equals(principal);
    if (!result) {
      this.principal = principal;
    }
    return result;
  }

  @Override
  public LocalDate getEventDate() {
    return this.paymentDate;
  }
}

