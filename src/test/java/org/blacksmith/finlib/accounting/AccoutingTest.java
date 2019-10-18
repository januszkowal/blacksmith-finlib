package org.blacksmith.finlib.accounting;

import java.time.LocalDate;
import org.blacksmith.finlib.accounting.in.DocumentIn;
import org.blacksmith.finlib.accounting.register.PortfolioCashRegister;
import org.blacksmith.finlib.basic.Currency;

public class AccoutingTest {
  public void testDocument() {

    AccountingRegister preg = PortfolioCashRegister.ofNew(2L, Currency.EUR);
    DocumentIn doc  = DocumentIn.of(LocalDate.now(),LocalDate.of(2019,1,3));
  }
}
