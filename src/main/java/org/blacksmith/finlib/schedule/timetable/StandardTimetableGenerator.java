package org.blacksmith.finlib.schedule.timetable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.schedule.ScheduleParameters;

public class StandardTimetableGenerator implements TimetableGenerator {

  @Override
  public List<TimetableInterestEntry> generate(ScheduleParameters scheduleParameters) {
    ArgChecker.notNull(scheduleParameters, "Schedule parameters must be not null");
    List<TimetableInterestEntry> schedule = new ArrayList<>();
    LocalDate refDate = scheduleParameters.getFirstCouponDate();
    LocalDate cashflowStartDate = scheduleParameters.getStartDate();
    LocalDate cashflowPmtDateUnadjusted;
    LocalDate cashflowPmtDateAdjusted;
    LocalDate cashflowEndDate;

    int i = refDate.compareTo(scheduleParameters.getStartDate()) > 0 ? 0 : 1;
    while (cashflowStartDate.isBefore(scheduleParameters.getMaturityDate())) {
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
      var cashflowBuilder = TimetableInterestEntry.builder()
          .startDate(cashflowStartDate)
          .endDate(cashflowEndDate)
          .paymentDateUnadjusted(cashflowPmtDateUnadjusted)
          .paymentDate(cashflowPmtDateAdjusted);
      if (scheduleParameters.getRateResetFrequency() != null
          && scheduleParameters.getRateResetFrequency() != scheduleParameters.getCouponFrequency()) {
        cashflowBuilder.subEvents(generateSubEvents(scheduleParameters, cashflowStartDate,
            cashflowEndDate));
      }
      schedule.add(cashflowBuilder.build());
      cashflowStartDate = cashflowEndDate;
      i++;
    }

    return Collections.unmodifiableList(schedule);
  }

  private List<TimetableInterestEntry.TimetableRateResetEntry> generateSubEvents(ScheduleParameters scheduleParameters, LocalDate startDate,
      LocalDate endDate) {
    List<TimetableInterestEntry.TimetableRateResetEntry> subEvents = new ArrayList<>();
    LocalDate subCashflowStartDate = startDate;
    while (subCashflowStartDate.isBefore(endDate)) {
      LocalDate subCashflowEndDateUnadjusted =
          DateUtils.min(endDate, scheduleParameters.getRateResetFrequency()
              .addToWithEomAdjust(subCashflowStartDate, scheduleParameters.isEndOfMonthConvention()));
      subEvents.add(TimetableInterestEntry.TimetableRateResetEntry.builder()
          .startDate(subCashflowStartDate)
          .endDate(subCashflowEndDateUnadjusted)
          .build());
      subCashflowStartDate = subCashflowEndDateUnadjusted;
    }
    return subEvents;
  }

  private LocalDate adjustEndDate(ScheduleParameters scheduleParameters, LocalDate pmtUnadjusted, LocalDate pmtAdjusted) {
    return scheduleParameters.isLinkCouponLengthWitPayment() ? pmtAdjusted : pmtUnadjusted;
  }
}
