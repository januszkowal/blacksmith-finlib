package org.blacksmith.finlib.calendar.policy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.calendar.HolidayPolicy;

public class HolidayPolicyComposite implements HolidayPolicy {

  private static final String EMPTY_PROVIDERS_MESSAGE = "Empty providers list not allowed";
  private static final String NULL_POLICY_MESSAGE = "Null policy is not allowed";

  private final List<HolidayPolicy> policies;

  public HolidayPolicyComposite(Collection<HolidayPolicy> policies) {
    ArgChecker.notEmpty(policies, EMPTY_PROVIDERS_MESSAGE);
    this.policies = List.copyOf(policies);
  }

  public HolidayPolicyComposite(HolidayPolicy policy) {
    ArgChecker.notNull(policy, NULL_POLICY_MESSAGE);
    this.policies = List.of(policy);
  }

  public static HolidayPolicyComposite of(Collection<HolidayPolicy> policies) {
    return new HolidayPolicyComposite(policies);
  }

  public static HolidayPolicyComposite of(HolidayPolicy... policies) {
    return new HolidayPolicyComposite(List.of(policies));
  }

  public static HolidayPolicyComposite ofSingle(HolidayPolicy policy) {
    ArgChecker.notNull(policy, NULL_POLICY_MESSAGE);
    return new HolidayPolicyComposite(policy);
  }

  public static HolidayPolicyCompositeBuilder builder() {
    return new HolidayPolicyCompositeBuilder();
  }

  @Override
  public boolean isHoliday(LocalDate date) {
    return policies.stream().anyMatch(hp -> hp.isHoliday(date));
  }

  public static class HolidayPolicyCompositeBuilder {

    private List<HolidayPolicy> policies = new ArrayList<>();

    public HolidayPolicyCompositeBuilder policies(HolidayPolicy... policies) {
      this.policies.addAll(List.of(policies));
      return this;
    }

    public HolidayPolicyCompositeBuilder policies(Collection<HolidayPolicy> policies) {
      this.policies.addAll(policies);
      return this;
    }

    public HolidayPolicyComposite build() {
      return new HolidayPolicyComposite(policies);
    }
  }
}
