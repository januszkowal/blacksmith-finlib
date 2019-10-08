package org.blacksmith.finlib.schedule;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.blacksmith.finlib.dayconvention.utils.Pair;
import org.blacksmith.finlib.interestbasis.ScheduleParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduleGenerator {
  private final Logger LOGGER = LoggerFactory.getLogger(ScheduleGenerator.class);
  private final ScheduleParameters scheduleParameters;

  public ScheduleGenerator(ScheduleParameters scheduleParameters) {
    this.scheduleParameters = scheduleParameters;
  }
  public void generate() {
    LocalDate refDate = scheduleParameters.getReferenceDate();
    LocalDate refDateX = refDate.minusDays(1);
    LocalDate schPmtDateCalc = null;
    LocalDate schStartDate = scheduleParameters.getStartDate();
    LocalDate schEndDate = null;
    Pair<LocalDate> endDates = null;
    Set<LocalDate> nonMovablePmtDates = Stream.of(refDate,scheduleParameters.getEndDate()).collect(Collectors.toSet());
    int i=0;
    LOGGER.info("fff {}",scheduleParameters);
    do {
      i++;
      schPmtDateCalc = scheduleParameters.getCouponFrequency().plus(refDate,i);
      //scheduleParameters.getBasis().yearFraction()
      if (schPmtDateCalc.isAfter(refDateX)) {
        if (nonMovablePmtDates.contains(schPmtDateCalc)) {
          endDates = Pair.of(schPmtDateCalc,schPmtDateCalc);
        }
        else {
          endDates = scheduleParameters.getBusinessDayConvention().adjustPair(schPmtDateCalc,scheduleParameters.getBusinessDayCalendar());
        }
        schEndDate = schPmtDateCalc;
        LOGGER.info("Item pmtDate={} pmtDateAdj={}",schPmtDateCalc, endDates);
        //
        schStartDate = endDates.getValue1();
      }
    } while (schPmtDateCalc.isBefore(scheduleParameters.getEndDate()));
  }
}
