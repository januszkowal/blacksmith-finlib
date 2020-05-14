package org.blacksmith.finlib.math.xirr;

import java.util.List;
import lombok.Value;
import org.blacksmith.finlib.schedule.Cashflow;

@Value
public class Schedule {
  String scheduleName;
  List<Cashflow> cashflows;
}
