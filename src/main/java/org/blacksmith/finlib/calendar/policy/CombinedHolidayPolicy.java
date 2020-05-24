package org.blacksmith.finlib.calendar.policy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.blacksmith.commons.arg.Validate;
import org.blacksmith.finlib.calendar.HolidayPolicy;

/**
 * Holiday policy containing list of policies
 * Checking is done in insertion order, until first "non-business" day is found
 * */
public class CombinedHolidayPolicy implements HolidayPolicy {
  private static final String NULL_POLICIES_MESSAGE = "Null policies list not allowed";
  
  private List<HolidayPolicy> policies = new ArrayList<>();
  
  public CombinedHolidayPolicy(){}
  
  public CombinedHolidayPolicy(Collection<HolidayPolicy> policies){
    addPolicies(policies);
  }
  
  public CombinedHolidayPolicy(HolidayPolicy... policies){
    addPolicies(policies);
  }
  
  public static CombinedHolidayPolicy of (Collection<HolidayPolicy> policies){
    return new CombinedHolidayPolicy(policies);
  }
  
  public static CombinedHolidayPolicy of (HolidayPolicy... policies){
    return new CombinedHolidayPolicy(policies);
  }

  public void addPolicy(HolidayPolicy policy) {
    Validate.notEmpty(policies, NULL_POLICIES_MESSAGE);
    this.policies.add(policy);
  }

  public void addPolicies(HolidayPolicy...policies) {
    Validate.notEmpty(policies, NULL_POLICIES_MESSAGE);
    this.policies.addAll(List.of(policies));
  }
  
  public void addPolicies(Collection<HolidayPolicy> holidayPolicies) {
    Validate.notEmpty(holidayPolicies, "Null holiday policy not allowed");
    this.policies.addAll(holidayPolicies);
  }
  
  @Override
  public boolean isHoliday(LocalDate date) {
    return policies.stream()
        .map(hp->hp.isHoliday(date))
        .filter(ih->ih)
        .findFirst().orElse(false);
  }

  public static CombinedHolidayPolicyBuilder builder() {
    return new CombinedHolidayPolicyBuilder();
  }

  public static class CombinedHolidayPolicyBuilder {
    private List<HolidayPolicy> policies = new ArrayList<>();
    public CombinedHolidayPolicyBuilder policy(HolidayPolicy policy) {
      this.policies.add(policy);
      return this;
    }
    public CombinedHolidayPolicyBuilder policies(HolidayPolicy...policies) {
      this.policies.addAll(List.of(policies));
      return this;
    }
    public CombinedHolidayPolicyBuilder policies(Collection<HolidayPolicy> policies) {
      this.policies.addAll(policies);
      return this;
    }
    public CombinedHolidayPolicy build() {
      return new CombinedHolidayPolicy(policies);
    }
  }
}
