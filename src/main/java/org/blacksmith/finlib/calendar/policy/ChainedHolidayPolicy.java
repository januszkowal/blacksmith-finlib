package org.blacksmith.finlib.calendar.policy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.basic.calendar.HolidayPolicy;

public class ChainedHolidayPolicy implements HolidayPolicy {

  private static final String NULL_PROVIDERS_MESSAGE = "Null providers list not allowed";
  private final HolidayPolicy next;

  private final List<HolidayPolicy> policies;

  public ChainedHolidayPolicy(Collection<HolidayPolicy> policies, HolidayPolicy next) {
    ArgChecker.notEmpty(policies, NULL_PROVIDERS_MESSAGE);
    this.next = next;
    this.policies = new ArrayList<>(policies);
  }

  public static ChainedHolidayPolicyBuilder builder() {
    return new ChainedHolidayPolicyBuilder();
  }

  @Override
  public boolean isHoliday(LocalDate date) {
    boolean thisPolicyResult = policies.stream()
        .map(hp -> hp.isHoliday(date))
        .filter(ih -> ih)
        .findFirst().orElse(false);
    return thisPolicyResult || Optional.ofNullable(next).map(p -> p.isHoliday(date)).orElse(false);
  }

  public static class ChainedHolidayPolicyBuilder {

    private Collection<HolidayPolicy> holidayProviders;
    private HolidayPolicy next;

    public ChainedHolidayPolicyBuilder policies(HolidayPolicy... policies) {
      this.holidayProviders = Arrays.stream(policies).collect(Collectors.toList());
      return this;
    }

    public ChainedHolidayPolicyBuilder policies(Collection<HolidayPolicy> providers) {
      this.holidayProviders = providers;
      return this;
    }

    public ChainedHolidayPolicyBuilder next(HolidayPolicy next) {
      this.next = next;
      return this;
    }

    public org.blacksmith.finlib.basic.calendar.policy.ChainedHolidayPolicy build() {
      return new org.blacksmith.finlib.basic.calendar.policy.ChainedHolidayPolicy(holidayProviders, next);
    }
  }
}
