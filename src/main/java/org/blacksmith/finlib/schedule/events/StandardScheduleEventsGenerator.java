package org.blacksmith.finlib.schedule.events;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.interestbasis.ScheduleParameters;
import org.blacksmith.finlib.schedule.events.ScheduleEvent.SubEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardScheduleEventsGenerator implements ScheduleEventsGenerator {

  private final static Logger log = LoggerFactory.getLogger(StandardScheduleEventsGenerator.class);

  @Override
  public List<ScheduleEvent> generate(ScheduleParameters scheduleParameters) {
    List<ScheduleEvent> schedule = new ArrayList<>();
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
        cashflowEndDate = getEndDate(scheduleParameters, cashflowPmtDateUnadjusted, cashflowPmtDateAdjusted);
      }
      var cashflowBuilder = ScheduleEvent.builder()
          .startDate(cashflowStartDate)
          .endDate(cashflowEndDate)
          .paymentDateUnadjusted(cashflowPmtDateUnadjusted)
          .paymentDate(cashflowPmtDateAdjusted);
      if (scheduleParameters.getRateResetFrequency() != null
          && scheduleParameters.getRateResetFrequency() != scheduleParameters.getCouponFrequency()) {
        List<ScheduleEvent.SubEvent> subCashflows = generateSubEvents(scheduleParameters, cashflowStartDate,
            cashflowEndDate);
        cashflowBuilder.subEvents(subCashflows);
      }
      schedule.add(cashflowBuilder.build());
      cashflowStartDate = cashflowEndDate;
    }

    return Collections.unmodifiableList(schedule);
  }

  private List<SubEvent> generateSubEvents(ScheduleParameters scheduleParameters, LocalDate startDate,
      LocalDate endDate) {
    List<SubEvent> subEvents = new ArrayList<>();
    LocalDate subCashflowStartDate = startDate;
    while (subCashflowStartDate.isBefore(endDate)) {
      LocalDate subCashflowEndDateUnadjusted =
          DateUtils.min(endDate,
              scheduleParameters.getRateResetFrequency()
                  .addToWithEomAdjust(subCashflowStartDate, scheduleParameters.isEndOfMonthConvention()));
      subEvents.add(SubEvent.of(subCashflowStartDate, subCashflowEndDateUnadjusted));
      subCashflowStartDate = subCashflowEndDateUnadjusted;
    }
    return subEvents;
  }

  private LocalDate getEndDate(ScheduleParameters scheduleParameters, LocalDate pmtUnadjusted, LocalDate pmtAdjusted) {
    return scheduleParameters.isLinkCouponLengthWitPayment() ? pmtAdjusted : pmtUnadjusted;
  }
}
