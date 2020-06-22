package org.blacksmith.finlib.schedule;

import java.time.LocalDate;
import org.blacksmith.finlib.interestbasis.ScheduleInfo;
import org.blacksmith.finlib.interestbasis.ScheduleParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.blacksmith.finlib.basic.Amount;

public class StandardScheduleGenerator implements ScheduleGenerator {
  
  private final Logger LOGGER = LoggerFactory.getLogger(StandardScheduleGenerator.class);
  
  private final ScheduleParameters scheduleParameters;
  
  private Schedule schedule;

  public StandardScheduleGenerator(ScheduleParameters scheduleParameters) {
    this.scheduleParameters = scheduleParameters;
  }
  
  public LocalDate getEndDate(LocalDate pmtUnadjusted, LocalDate pmtAdjusted) {
    return scheduleParameters.isLinkCouponLengthWitPayment() ? pmtAdjusted : pmtUnadjusted;
  }

  private ScheduleInfo createScheduleInfo(ScheduleParameters scheduleParameters, LocalDate couponStartDate, LocalDate couponEndDate) {
    return ScheduleInfo.builder()
        .isEndOfMonthConvention(scheduleParameters.isEndOfMonthConvention())
        .couponFrequency(scheduleParameters.getCouponFrequency())
        .startDate(scheduleParameters.getStartDate())
        .endDate(scheduleParameters.getMaturityDate())
        .couponStartDate(couponStartDate)
        .couponEndDate(couponEndDate)
        .build();
  }
  
  private void addInterestCashflow(LocalDate startDate, LocalDate endDate,LocalDate pmtDate) {
    ScheduleInfo scheduleInfo = createScheduleInfo(scheduleParameters,startDate,endDate);
    double rate=7;
    double interest =
        scheduleParameters.getBasis().yearFraction(startDate, scheduleInfo.getCouponEndDate(), scheduleInfo)*
        rate*
        scheduleInfo.getNotional(startDate).doubleValue();
    Cashflow cashflow = Cashflow.builder()
        .startDate(startDate)
        .endDate(endDate)
        .paymentDate(pmtDate)
        .notional(scheduleParameters.getNotional())
        .amount(Amount.of(interest))
        .build();
    schedule.getCashflow().add(cashflow);    
  }

  @Override
  public Schedule generate() {
    this.schedule = new Schedule();
    //
    LocalDate refDate = scheduleParameters.getFirstCouponDate();
    LocalDate cashflowStartDate = scheduleParameters.getStartDate();
    LocalDate cashflowPmtDateUnadjusted;
    LocalDate cashflowPmtDateAdjusted;
    LocalDate cashflowEndDate;
    
    int i = refDate.compareTo(scheduleParameters.getStartDate()) > 0 ? -1 : 0;
    while(cashflowStartDate.isBefore(scheduleParameters.getMaturityDate())) {
      i++;
      cashflowPmtDateUnadjusted = scheduleParameters.getCouponFrequency().addTo(refDate,i);
      cashflowPmtDateAdjusted = scheduleParameters.getBusinessDayConvention().adjust(cashflowPmtDateUnadjusted,scheduleParameters.getBusinessDayCalendar());
      if (cashflowPmtDateAdjusted.isAfter(scheduleParameters.getMaturityDate())) {
        cashflowPmtDateAdjusted = scheduleParameters.getMaturityDate();
        cashflowEndDate = cashflowPmtDateAdjusted;
      }
      else {
        cashflowEndDate = getEndDate(cashflowPmtDateUnadjusted,cashflowPmtDateAdjusted);
      }
      LOGGER.info("Item schedulePmtDateUnadjusted={} cashflowPmtDateAdjusted={}",cashflowPmtDateUnadjusted, cashflowPmtDateAdjusted);
      addInterestCashflow(cashflowStartDate,cashflowEndDate,cashflowPmtDateAdjusted);
      cashflowStartDate = cashflowEndDate;
    } 

    return schedule;
  }
}
