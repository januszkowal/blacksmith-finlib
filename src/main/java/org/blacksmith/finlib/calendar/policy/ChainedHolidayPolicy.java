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

  private HolidayPolicy next;
  
  private Set<HolidayProvider> holidayProviders = new LinkedHashSet<>();
  
  public ChainedHolidayPolicy(Collection<HolidayProvider> providers) {
    this.holidayProviders.addAll(providers);
  }
  
  public ChainedHolidayPolicy(HolidayProvider...providers) {
    Validate.checkNotNull(providers, "Null providers list not allowed");
    this.holidayProviders.addAll(Arrays.stream(providers).collect(Collectors.toList()));
  }
  
  public void setNext(HolidayPolicy next) {
    this.next = next;
  }

  @Override
  public boolean isHoliday(LocalDate date) {
    boolean thisPolicyResult = holidayProviders.stream()
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
    private Collection<HolidayProvider> holidayProviders;
    public ChainedHolidayPolicyBuilder next(HolidayPolicy next) {
      this.next = next;
      return this;
    }
    public ChainedHolidayPolicyBuilder providers(HolidayProvider...providers) {
      this.holidayProviders = Arrays.stream(providers).collect(Collectors.toList());
      return this;
    }
    public ChainedHolidayPolicyBuilder providers(Collection<HolidayProvider> providers) {
      this.holidayProviders = providers;
      return this;
    }
    public ChainedHolidayPolicy build() {
      ChainedHolidayPolicy result = new ChainedHolidayPolicy(holidayProviders);
      result.setNext(next);
      return result;
    }
  }
}
