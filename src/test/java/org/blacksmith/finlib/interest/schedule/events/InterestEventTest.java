package org.blacksmith.finlib.interest.schedule.events;

import java.time.LocalDate;

import org.blacksmith.finlib.basic.numbers.Amount;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InterestEventTest {
  @Test
  public void defaultValuesTest() {
    InterestEvent event = InterestEvent.builder()
        .startDate(LocalDate.now())
        .endDate(LocalDate.now())
        .paymentDate(LocalDate.now())
        .build();
    assertThat(event.principal).isEqualTo(Amount.ZERO);
  }

}
