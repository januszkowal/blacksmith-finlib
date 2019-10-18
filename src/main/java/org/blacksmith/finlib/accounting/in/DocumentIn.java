package org.blacksmith.finlib.accounting.in;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import lombok.Getter;

@Getter
public class DocumentIn {
  private final LocalDate processingDate;
  private final LocalDate valueDate;
  private final List<EntryIn> decrees = new ArrayList<>();

  public DocumentIn(LocalDate processingDate, LocalDate valueDate) {
    this.processingDate = processingDate;
    this.valueDate = valueDate;
  }

  public static DocumentIn of (LocalDate processingDate, LocalDate valueDate) {
    return new DocumentIn(processingDate, valueDate);
  }

  public void addDecree(EntryIn decree) {
    this.decrees.add(decree);
  }

  public List<EntryIn> getDecrees() {
    return Collections.unmodifiableList(this.decrees);
  }
}
