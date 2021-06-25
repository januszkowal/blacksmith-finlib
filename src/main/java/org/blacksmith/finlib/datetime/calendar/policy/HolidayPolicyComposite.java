package org.blacksmith.finlib.datetime.calendar.policy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.datetime.calendar.HolidayPolicy;

import lombok.Builder;
import lombok.Singular;

@Builder
public class HolidayPolicyComposite implements HolidayPolicy {

  private static final String EMPTY_POLICIES_MESSAGE = "Empty policies list not allowed";
  private static final String NULL_POLICY_MESSAGE = "Null policy is not allowed";

  @Singular(value = "policy")
  private final List<HolidayPolicy> policies;

  public HolidayPolicyComposite(Collection<HolidayPolicy> policies) {
    ArgChecker.notEmpty(policies, EMPTY_POLICIES_MESSAGE);
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
    return new HolidayPolicyComposite(policy);
  }

  public static HolidayPolicyCompositeBuilder builder() {
    return new HolidayPolicyCompositeBuilder();
  }

  @Override
  public boolean isHoliday(LocalDate date) {
    return policies.stream().anyMatch(hp -> hp.isHoliday(date));
  }
}
