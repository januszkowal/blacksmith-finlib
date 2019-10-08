package org.blacksmith.finlib.schedule;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.blacksmith.finlib.interestbasis.ScheduleParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduleGenerator {
  private final Logger LOGGER = LoggerFactory.getLogger(ScheduleGenerator.class);
  private final ScheduleParameters scheduleParameters;

  public ScheduleGenerator(ScheduleParameters scheduleParameters) {
    this.scheduleParameters = scheduleParameters;
  }
  public LocalDate getEndDate(LocalDate pmtUnadjusted, LocalDate pmtAdjusted) {
    return scheduleParameters.isLinkCouponLengthWitPayment() ? pmtAdjusted : pmtUnadjusted;
  }
  public void generate() {
    LocalDate refDate = scheduleParameters.getReferenceDate();
    LocalDate refDateX = refDate.minusDays(1);
    LocalDate schPmtDateUnadjusted = null;
    LocalDate schPmtDateAdjusted = null;
    LocalDate schStartDate = scheduleParameters.getStartDate();
    LocalDate schEndDate = null;
    
    Set<LocalDate> nonMovablePmtDates = Stream.of(refDate,scheduleParameters.getEndDate()).collect(Collectors.toSet());
    int i=0;
    do {
      i++;
      schPmtDateUnadjusted = scheduleParameters.getCouponFrequency().plus(refDate,i);
      //scheduleParameters.getBasis().yearFraction()
      if (schPmtDateUnadjusted.isAfter(refDateX)) {
        if (nonMovablePmtDates.contains(schPmtDateUnadjusted)) {
          schPmtDateAdjusted = schPmtDateUnadjusted;
          schEndDate = schPmtDateUnadjusted;
        }
        else {
          schPmtDateAdjusted = scheduleParameters.getBusinessDayConvention().adjust(schPmtDateUnadjusted,scheduleParameters.getBusinessDayCalendar());
          schEndDate = getEndDate(schPmtDateUnadjusted,schPmtDateAdjusted);
        }
        LOGGER.info("Item pmtDateUnadjusted={} pmtDateAdjusted={}",schPmtDateUnadjusted, schPmtDateAdjusted);
      }
    } while (schPmtDateUnadjusted.isBefore(scheduleParameters.getEndDate()));
  }
}
