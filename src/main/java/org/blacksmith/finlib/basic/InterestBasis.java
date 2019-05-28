package org.blacksmith.finlib.basic;

import java.time.LocalDate;

public interface InterestBasis {
  double getFactor(LocalDate startDate, LocalDate endDate);
}
