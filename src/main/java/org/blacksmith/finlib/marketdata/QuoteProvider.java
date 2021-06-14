package org.blacksmith.finlib.marketdata;

import java.time.LocalDate;

public interface QuoteProvider {
  double getQuote(LocalDate valuationDate, QuoteId quoteId);
}
