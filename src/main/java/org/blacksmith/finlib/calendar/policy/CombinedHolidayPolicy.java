package org.blacksmith.finlib.calendar.policy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.blacksmith.commons.arg.Validate;
import org.blacksmith.finlib.calendar.HolidayPolicy;

public class CombinedHolidayPolicy implements HolidayPolicy {
  private List<HolidayPolicy> holidayPolicies = new ArrayList<>();
  public CombinedHolidayPolicy(){}
  public CombinedHolidayPolicy(List<HolidayPolicy> policies){
    Validate.checkNotNull(holidayPolicies, "Null holiday policy list not allowed");
    this.holidayPolicies.addAll(policies);
  }
  public void addPolicy(HolidayPolicy holidayPolicy) {
    Validate.checkNotNull(holidayPolicies, "Null holiday policy not allowed");
    holidayPolicies.add(holidayPolicy);
  }
  public void addPolicies(List<HolidayPolicy> holidayPolicy) {
    Validate.checkNotNull(holidayPolicies, "Null holiday policy not allowed");
    holidayPolicies.addAll(holidayPolicies);
  }
  @Override public boolean isHoliday(LocalDate date) {
    for (HolidayPolicy policy: holidayPolicies) {
      if (policy.isHoliday(date))
        return true;
    }
    return false;
  }
}
