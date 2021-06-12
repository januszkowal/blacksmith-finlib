package org.blacksmith.finlib.curve.node;

import org.blacksmith.finlib.basic.datetime.Tenor;
import org.blacksmith.finlib.marketdata.QuoteId;

public interface CurveNodeDefinition {
  //Descriptive value
  String getLabel();
  Tenor getTenor();
  QuoteId getQuoteId();
  double getSpread();
  //Label=OIS-1M
  //Symbology=OG-Ticker
  //Ticker=EUR-OIS-1M
  //Field Name=MarketValue
  //Type=OIS
  //Convention=EUR-FIXED-1Y-EONIA-OIS
  //Time=1M
}
