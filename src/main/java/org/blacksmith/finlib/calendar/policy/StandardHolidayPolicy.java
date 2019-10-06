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
  
  private static final String NULL_PROVIDERS_MESSAGE = "Null providers list not allowed";
  
  private Set<HolidayProvider> holidayProviders = new LinkedHashSet<>();
  
  public StandardHolidayPolicy(Collection<HolidayProvider> providers) {
    Validate.checkNotEmpty(providers, NULL_PROVIDERS_MESSAGE);
    this.holidayProviders.addAll(providers);
  }
  
  public StandardHolidayPolicy(HolidayProvider...providers) {
    Validate.checkNotEmpty(providers, NULL_PROVIDERS_MESSAGE);
    this.holidayProviders.addAll(Arrays.stream(providers).collect(Collectors.toList()));
  }
  
  public static StandardHolidayPolicy of(Collection<HolidayProvider> providers) {
    return new StandardHolidayPolicy(providers);
  }
  
  public static StandardHolidayPolicy of(HolidayProvider...providers) {
    return new StandardHolidayPolicy(providers);
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
