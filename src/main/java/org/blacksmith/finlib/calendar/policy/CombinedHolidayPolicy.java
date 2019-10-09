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
  private static final String NULL_POLICIES_MESSAGE = "Null policies list not allowed";
  
  private List<HolidayPolicy> holidayPolicies = new ArrayList<>();
  
  public CombinedHolidayPolicy(){}
  
  public CombinedHolidayPolicy(Collection<HolidayPolicy> holidayPolicies){
    addPolicies(holidayPolicies);
  }
  
  public CombinedHolidayPolicy(HolidayPolicy... holidayPolicies){
    addPolicies(holidayPolicies);
  }
  
  public static CombinedHolidayPolicy of (Collection<HolidayPolicy> holidayPolicies){
    return new CombinedHolidayPolicy(holidayPolicies);
  }
  
  public static CombinedHolidayPolicy of(HolidayPolicy... holidayPolicies){
    return new CombinedHolidayPolicy(holidayPolicies);
  }

  public void addPolicies(HolidayPolicy...holidayPolicies) {
    Validate.notEmpty(holidayPolicies, NULL_POLICIES_MESSAGE);
    this.holidayPolicies.addAll(Arrays.stream(holidayPolicies).collect(Collectors.toList()));
  }
  
  public void addPolicies(Collection<HolidayPolicy> holidayPolicies) {
    Validate.notEmpty(holidayPolicies, "Null holiday policy not allowed");
    this.holidayPolicies.addAll(holidayPolicies);
  }
  
  @Override public boolean isHoliday(LocalDate date) {
    return holidayPolicies.stream()
        .map(hp->hp.isHoliday(date))
        .filter(ih->ih)
        .findFirst().orElse(false);
  }
}
