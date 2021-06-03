package org.blacksmith.finlib.calendar.policy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.blacksmith.finlib.calendar.HolidayPolicy;

/**
 * Holiday policy containing list of policies
 * Checking is done in insertion order, until first "non-business" day is found
 */
public class CombinedHolidayPolicy implements HolidayPolicy {
  private static final String NULL_POLICIES_MESSAGE = "Null policies list not allowed";

  private final List<HolidayPolicy> policies;

  public CombinedHolidayPolicy(Collection<HolidayPolicy> policies) {
    this.policies = new ArrayList<>(policies);
  }

  public CombinedHolidayPolicy(HolidayPolicy... policies) {
    this.policies = List.of(policies);
  }

  public static CombinedHolidayPolicy of(Collection<HolidayPolicy> policies) {
    return new CombinedHolidayPolicy(policies);
  }

  public static CombinedHolidayPolicy of(HolidayPolicy... policies) {
    return new CombinedHolidayPolicy(policies);
  }

  public static CombinedHolidayPolicyBuilder builder() {
    return new CombinedHolidayPolicyBuilder();
  }

  @Override
  public boolean isHoliday(LocalDate date) {
    return policies.stream()
        .anyMatch(hp -> hp.isHoliday(date));
  }

  public static class CombinedHolidayPolicyBuilder {
    private List<HolidayPolicy> policies = new ArrayList<>();

    public CombinedHolidayPolicyBuilder policy(HolidayPolicy policy) {
      this.policies.add(policy);
      return this;
    }

    public CombinedHolidayPolicyBuilder policies(HolidayPolicy... policies) {
      this.policies = List.of(policies);
      return this;
    }

    public CombinedHolidayPolicyBuilder policies(Collection<HolidayPolicy> policies) {
      this.policies = new ArrayList<>(policies);
      return this;
    }

    public CombinedHolidayPolicy build() {
      return new CombinedHolidayPolicy(policies);
    }
  }
}
