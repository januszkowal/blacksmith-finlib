package org.blacksmith.finlib.schedule.events;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.interestbasis.ScheduleParameters;
import org.blacksmith.finlib.schedule.events.schedule.ScheduleEventsGenerator;
import org.blacksmith.finlib.schedule.events.schedule.ScheduleInterestEvent;
import org.blacksmith.finlib.schedule.events.schedule.ScheduleRateResetEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardScheduleEventsGenerator implements ScheduleEventsGenerator {

  private final static Logger log = LoggerFactory.getLogger(StandardScheduleEventsGenerator.class);

  @Override
  public List<ScheduleInterestEvent> generate(ScheduleParameters scheduleParameters) {
    List<ScheduleInterestEvent> schedule = new ArrayList<>();
    LocalDate refDate = scheduleParameters.getFirstCouponDate();
    LocalDate cashflowStartDate = scheduleParameters.getStartDate();
    LocalDate cashflowPmtDateUnadjusted;
    LocalDate cashflowPmtDateAdjusted;
    LocalDate cashflowEndDate;

    int i = refDate.compareTo(scheduleParameters.getStartDate()) > 0 ? -1 : 0;
    while (cashflowStartDate.isBefore(scheduleParameters.getMaturityDate())) {
      i++;
      cashflowPmtDateUnadjusted = scheduleParameters.getCouponFrequency()
          .addToWithEomAdjust(refDate, i, scheduleParameters.isEndOfMonthConvention());
      cashflowPmtDateAdjusted = scheduleParameters.getBusinessDayConvention()
          .adjust(cashflowPmtDateUnadjusted, scheduleParameters.getBusinessDayCalendar());
      if (cashflowPmtDateAdjusted.isAfter(scheduleParameters.getMaturityDate())) {
        cashflowPmtDateAdjusted = scheduleParameters.getMaturityDate();
        cashflowEndDate = cashflowPmtDateAdjusted;
      } else {
        cashflowEndDate = adjustEndDate(scheduleParameters, cashflowPmtDateUnadjusted, cashflowPmtDateAdjusted);
      }
      var cashflowBuilder = ScheduleInterestEvent.builder()
          .startDate(cashflowStartDate)
          .endDate(cashflowEndDate)
          .paymentDateUnadjusted(cashflowPmtDateUnadjusted)
          .paymentDate(cashflowPmtDateAdjusted);
      if (scheduleParameters.getRateResetFrequency() != null
          && scheduleParameters.getRateResetFrequency() != scheduleParameters.getCouponFrequency()) {
        List<ScheduleRateResetEvent> subEvents = generateSubEvents(scheduleParameters, cashflowStartDate,
            cashflowEndDate);
        cashflowBuilder.subEvents(subEvents);
      }
      schedule.add(cashflowBuilder.build());
      cashflowStartDate = cashflowEndDate;
    }

    return Collections.unmodifiableList(schedule);
  }

  private List<ScheduleRateResetEvent> generateSubEvents(ScheduleParameters scheduleParameters, LocalDate startDate,
      LocalDate endDate) {
    List<ScheduleRateResetEvent> subEvents = new ArrayList<>();
    LocalDate subCashflowStartDate = startDate;
    while (subCashflowStartDate.isBefore(endDate)) {
      LocalDate subCashflowEndDateUnadjusted =
          DateUtils.min(endDate,
              scheduleParameters.getRateResetFrequency()
                  .addToWithEomAdjust(subCashflowStartDate, scheduleParameters.isEndOfMonthConvention()));
      subEvents.add(ScheduleRateResetEvent.builder()
          .startDate(subCashflowStartDate)
          .endDate(subCashflowEndDateUnadjusted)
          .isRateReset(true)
          .build());
      subCashflowStartDate = subCashflowEndDateUnadjusted;
    }
    return subEvents;
  }

  private LocalDate adjustEndDate(ScheduleParameters scheduleParameters, LocalDate pmtUnadjusted, LocalDate pmtAdjusted) {
    return scheduleParameters.isLinkCouponLengthWitPayment() ? pmtAdjusted : pmtUnadjusted;
  }
}
