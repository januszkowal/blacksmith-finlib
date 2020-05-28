package org.blacksmith.finlib.dayconvention;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StandardBusinessDayConventionTest {
  @Test
  public void testSearchByName() {
    assertEquals(StandardBusinessDayConvention.MODIFIED_FOLLOWING,StandardBusinessDayConvention.fromName("MODIFIED_FOLLOWING"));
    assertNull(StandardBusinessDayConvention.fromName("XXX"));
    assertEquals(StandardBusinessDayConvention.MODIFIED_FOLLOWING,StandardBusinessDayConvention.fromShortName("ModifiedFollowing"));
    assertNull(StandardBusinessDayConvention.fromShortName("xxx"));
  }

}