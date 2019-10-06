package org.blacksmith.finlib.calendar.policy;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.blacksmith.commons.arg.Validate;
import org.blacksmith.finlib.calendar.HolidayPolicy;

public class StandardHolidayPolicy implements HolidayPolicy {
  
  private Set<HolidayProvider> holidayProviders = new LinkedHashSet<>();
  
  public StandardHolidayPolicy(Collection<HolidayProvider> providers) {
    this.holidayProviders.addAll(providers);
  }
  
  public StandardHolidayPolicy(HolidayProvider...providers) {
    Validate.checkNotNull(providers, "Null providers list not allowed");
    this.holidayProviders.addAll(Arrays.stream(providers).collect(Collectors.toList()));
  }
  
  /*
   * Providers are used respectively in order they were inserted
   * */

  @Override
  public boolean isHoliday(LocalDate date) {    
    return holidayProviders.stream()
        .map(hp->hp.isHoliday(date))
        .filter(ih->ih)
        .findFirst()
        .orElse(false);
  }
  
  public static class StandardHolidayPolicyBuilder {
    private Collection<HolidayProvider> holidayProviders;
    public StandardHolidayPolicyBuilder providers(HolidayProvider...providers) {
      this.holidayProviders = Arrays.stream(providers).collect(Collectors.toList());
      return this;
    }
    public StandardHolidayPolicyBuilder providers(Collection<HolidayProvider> providers) {
      this.holidayProviders = providers;
      return this;
    }
    public StandardHolidayPolicy build() {
      return new StandardHolidayPolicy(holidayProviders);
    }
  }
}
