package org.blacksmith.finlib.calendar.policy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.blacksmith.commons.arg.Validate;
import org.blacksmith.finlib.calendar.HolidayPolicy;

public class CombinedHolidayPolicy implements HolidayPolicy {
  private List<HolidayPolicy> holidayPolicies = new ArrayList<>();
  
  public CombinedHolidayPolicy(){}
  
  public CombinedHolidayPolicy(Collection<HolidayPolicy> policies){
    Validate.checkNotNull(holidayPolicies, "Null holiday policy list not allowed");
    this.holidayPolicies.addAll(policies);
  }
  
  public CombinedHolidayPolicy(HolidayPolicy... holidayPolicy){
    Validate.checkNotNull(holidayPolicies, "Null holiday policy list not allowed");
    this.holidayPolicies.addAll(Arrays.stream(holidayPolicy).collect(Collectors.toList()));
  }
  
  public void addPolicies(HolidayPolicy... holidayPolicy) {
    Validate.checkNotNull(holidayPolicies, "Null holiday policy not allowed");
    holidayPolicies.addAll(Arrays.stream(holidayPolicy).collect(Collectors.toList()));
  }
  
  public void addPolicies(Collection<HolidayPolicy> holidayPolicies) {
    Validate.checkNotNull(holidayPolicies, "Null holiday policy not allowed");
    this.holidayPolicies.addAll(holidayPolicies);
  }
  
  @Override public boolean isHoliday(LocalDate date) {
    return holidayPolicies.stream()
        .map(hp->hp.isHoliday(date))
        .filter(ih->ih)
        .findFirst().orElse(false);
  }
}
