package org.blacksmith.finlib.calendar;

import org.blacksmith.finlib.basic.StandardInterestBasis;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class DayCountTest {

  @Test
  void test1() {
    System.out.println("fac1:"+StandardInterestBasis.ACT_ACT_ISDA.getFactor(LocalDate.now(),LocalDate.now().plusDays(400)));
    System.out.println("fac2:"+StandardInterestBasis.ACT_ACT_ISDA_TEST.getFactor(LocalDate.now(),LocalDate.now().plusDays(400)));

  }
}
