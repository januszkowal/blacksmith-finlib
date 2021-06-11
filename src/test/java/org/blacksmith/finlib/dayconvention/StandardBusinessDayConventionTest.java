package org.blacksmith.finlib.dayconvention;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StandardBusinessDayConventionTest {
  @Test
  public void testSearchByName() {
    assertEquals(StandardBusinessDayConvention.MODIFIED_FOLLOWING, StandardBusinessDayConvention.fromName("MODIFIED_FOLLOWING"));
//    assertNull(StandardBusinessDayConvention.fromName("XXX"));
    assertEquals(StandardBusinessDayConvention.MODIFIED_FOLLOWING, StandardBusinessDayConvention.fromShortName("ModifiedFollowing"));
  }
}
