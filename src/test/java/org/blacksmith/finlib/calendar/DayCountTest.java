package org.blacksmith.finlib.calendar;

import org.blacksmith.finlib.interestbasis.InterestBasis;
import org.blacksmith.finlib.interestbasis.ScheduleParameters;
import org.blacksmith.finlib.interestbasis.StandardInterestBasis;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


import static org.blacksmith.commons.datetime.DateUtils.daysBetween;
import static org.blacksmith.commons.datetime.DateUtils.nextOrSameLeapDay;

public class DayCountTest {

  private void printFactor(InterestBasis basis, LocalDate startDate, LocalDate endDate, LocalDate calcDate) {
    System.out.println(basis.toString() + ":"+basis.yearFraction(startDate,endDate,calcDate));
  }
  private InterestBasis testr1() {
    return  new InterestBasis() {
      @Override public double yearFraction(LocalDate startDate, LocalDate endDate, LocalDate calcDate, ScheduleParameters scheduleInfo) {
        LocalDate end = endDate;
        LocalDate start = endDate.minusYears(1);
        long yearsx = 0;
        while (!start.isBefore(startDate)) {
          yearsx++;
          end = start;
          start = endDate.minusYears(yearsx + 1);
        }
        // calculate the remaining fraction, including start, excluding end
        long actualDays = daysBetween(startDate, end);
        LocalDate nextLeap = nextOrSameLeapDay(startDate);
        return yearsx + (actualDays / (nextLeap.isBefore(end) ? 366d : 365d));
      }

      public int days(LocalDate firstDate, LocalDate secondDate) {
        return 0;
      }
    };
  }
  @Test
  void test1() {
    LocalDate startDate = LocalDate.now();
    //LocalDate startDate = LocalDate.of(2020,2,1);
    LocalDate endDate = startDate.plusDays(400);
    List<StandardInterestBasis> basiss = Arrays.asList(StandardInterestBasis.ONE_ONE,
        StandardInterestBasis.ACT_360,StandardInterestBasis.ACT_364,StandardInterestBasis.ACT_365, StandardInterestBasis.ACT_365_25,
        StandardInterestBasis.ACT_ACT_ISDA, StandardInterestBasis.ACT_365_ACT,
        StandardInterestBasis.NL_360, StandardInterestBasis.NL_365,
        StandardInterestBasis.D30_360_ISDA,
        StandardInterestBasis.D30_360_PSA,
        StandardInterestBasis.D30_E_360, StandardInterestBasis.D30_E_365,
        StandardInterestBasis.D30_EPLUS_360, StandardInterestBasis.D30_U_360_EOM,
        StandardInterestBasis.ACT_ACT_YEAR,StandardInterestBasis.ACT_ACT_AFB).stream().collect(Collectors.toList());
    basiss.forEach(b->printFactor(b,startDate,endDate,endDate));
    printFactor(testr1(),startDate,endDate, endDate);
    System.out.println(Arrays.stream(StandardInterestBasis.values()).filter(b->!basiss.contains(b)).collect(Collectors.toList()));

  }
}
