package org.blacksmith.finlib.calendar;

import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.blacksmith.finlib.datetime.Frequency;
import org.blacksmith.finlib.interestbasis.DayCountConvention;
import org.blacksmith.finlib.interestbasis.ScheduleInfo;
import org.blacksmith.finlib.interestbasis.StandardDayCountConvention;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


import static org.blacksmith.commons.datetime.DateUtils.daysBetween;
import static org.blacksmith.commons.datetime.DateUtils.nextOrSameLeapDay;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class DayCountTest extends DayCountBaseTest {

  public void assertDayCountMethod(StandardDayCountConvention method, String s1, String s2, int expectedDays, double expectedFactor) {
    LocalDate d1 = LocalDate.parse(s1, DateTimeFormatter.BASIC_ISO_DATE);
    LocalDate d2 = LocalDate.parse(s2, DateTimeFormatter.BASIC_ISO_DATE);
    Assertions.assertEquals(expectedDays,method.days(d1,d2,null));
    Assertions.assertEquals(expectedFactor,method.yearFraction(d1,d2,null));
  }
}
