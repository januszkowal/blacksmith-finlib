package org.blacksmith.finlib.accounting;

import org.blacksmith.finlib.accounting.in.DocumentIn;

public interface Ledger {
  Long book(DocumentIn document);
}
