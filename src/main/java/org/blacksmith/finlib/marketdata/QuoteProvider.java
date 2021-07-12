package org.blacksmith.finlib.marketdata;

import java.time.LocalDate;

public interface QuoteProvider {
  double getQuote(QuoteId quoteId, LocalDate valuationDate);
}
