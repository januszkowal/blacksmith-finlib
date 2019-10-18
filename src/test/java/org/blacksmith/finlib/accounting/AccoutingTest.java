package org.blacksmith.finlib.accounting;

import java.time.LocalDate;
import org.blacksmith.finlib.accounting.in.DocumentIn;
import org.blacksmith.finlib.accounting.register.RegisterFactory;
import org.blacksmith.finlib.basic.Currency;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AccoutingTest {
  @Test
  public void testDocument() throws JsonProcessingException {

    Register preg = RegisterFactory.ofPosCcyWithId(123L, 23L, "POSITION_BUY", Currency.EUR, Currency.PLN);
    String result = new ObjectMapper().writeValueAsString(preg);
    
    //DocumentIn doc  = DocumentIn.of(LocalDate.now(),LocalDate.of(2019,1,3));
    System.out.println(result);
  }
}
