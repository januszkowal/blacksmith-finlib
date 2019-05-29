package org.blacksmith.finlib.basic;

import java.time.LocalDate;
import java.time.Period;

public enum TimeUnit implements DateOperation {
  DAY{
    @Override public LocalDate plus(LocalDate date, int amount) {
      return date.plusDays(amount);
    }
    @Override public LocalDate minus(LocalDate date, int amount) {
      return date.minusDays(amount);
    }
  },
  WEEK{
    @Override public LocalDate plus(LocalDate date, int amount) {
      return date.plusWeeks(amount);
    }

    @Override public LocalDate minus(LocalDate date, int amount) {
      return date.minusWeeks(amount);
    }
  },
  MONTH{
    @Override public LocalDate plus(LocalDate date, int amount) {
      return date.plusMonths(amount);
    }

    @Override public LocalDate minus(LocalDate date, int amount) {
      return date.minusMonths(amount);
    }
  },
  QUARTER{
    @Override public LocalDate plus(LocalDate date, int amount) {
      return date.plusMonths(3*amount);
    }

    @Override public LocalDate minus(LocalDate date, int amount) {
      return date.minusMonths(3*amount);
    }
  },
  HALF_YEAR{
    @Override public LocalDate plus(LocalDate date, int amount) {
      return date.plusMonths(6*amount);
    }

    @Override public LocalDate minus(LocalDate date, int amount) {
      return date.minusMonths(6*amount);
    }
  },
  YEAR{
    @Override public LocalDate plus(LocalDate date, int amount) {
      return date.plusYears(amount);
    }

    @Override public LocalDate minus(LocalDate date, int amount) {
      return date.minusYears(amount);
    }
  };
}
