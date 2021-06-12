package org.blacksmith.finlib.marketdata;

import org.blacksmith.commons.arg.ArgChecker;

import lombok.Value;

@Value
public class QuoteId {
  private final StandardId standardId;
  private final String fieldName;

  public QuoteId(StandardId standardId, String fieldName) {
    ArgChecker.notNull(standardId, "standardId must be not null");
    ArgChecker.notEmpty(fieldName, "fieldName must be not empty");
    this.standardId = standardId;
    this.fieldName = fieldName;
  }

  public static QuoteId of(StandardId standardId, String fieldName) {
    return new QuoteId(standardId, fieldName);
  }
}
