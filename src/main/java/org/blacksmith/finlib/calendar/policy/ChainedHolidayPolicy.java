package org.blacksmith.finlib.calendar.policy;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.blacksmith.commons.arg.Validate;
import org.blacksmith.finlib.calendar.HolidayPolicy;

public class ChainedHolidayPolicy implements HolidayPolicy {
  
  private static final String NULL_PROVIDERS_MESSAGE = "Null providers list not allowed";
  private final HolidayPolicy next;
  
  private Set<HolidayPolicy> policies = new LinkedHashSet<>();
  
  public ChainedHolidayPolicy(Collection<HolidayPolicy> providers, HolidayPolicy next) {
    Validate.notEmpty(providers, NULL_PROVIDERS_MESSAGE);
    this.next = next;
    this.policies.addAll(providers);
  }
  
  public ChainedHolidayPolicy(HolidayPolicy...providers) {
    Validate.notEmpty(providers, NULL_PROVIDERS_MESSAGE);
    this.next = null;
    this.policies.addAll(Arrays.stream(providers).collect(Collectors.toList()));
  }
  
  @Override
  public boolean isHoliday(LocalDate date) {
    boolean thisPolicyResult = policies.stream()
        .map(hp->hp.isHoliday(date))
        .filter(ih->ih)
        .findFirst().orElse(false);
    return thisPolicyResult || Optional.ofNullable(next).map(p->p.isHoliday(date)).orElse(false);
  }
  
  public static ChainedHolidayPolicyBuilder builder() {
    return new ChainedHolidayPolicyBuilder();
  }
  
  public static class ChainedHolidayPolicyBuilder {
    private HolidayPolicy next;
    private Collection<HolidayPolicy> holidayProviders;
    public ChainedHolidayPolicyBuilder next(HolidayPolicy next) {
      this.next = next;
      return this;
    }
    public ChainedHolidayPolicyBuilder policies(HolidayPolicy...policies) {
      this.holidayProviders = Arrays.stream(policies).collect(Collectors.toList());
      return this;
    }
    public ChainedHolidayPolicyBuilder policies(Collection<HolidayPolicy> providers) {
      this.holidayProviders = providers;
      return this;
    }
    public ChainedHolidayPolicy build() {
      ChainedHolidayPolicy result = new ChainedHolidayPolicy(holidayProviders,next);
      return result;
    }
  }
}
