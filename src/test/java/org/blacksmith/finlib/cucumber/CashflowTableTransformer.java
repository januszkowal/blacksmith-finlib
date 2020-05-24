package org.blacksmith.finlib.cucumber;

import io.cucumber.datatable.DataTable;
import io.cucumber.datatable.TableTransformer;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.blacksmith.finlib.math.xirr.Cashflow;

public class CashflowTableTransformer implements TableTransformer<List<Cashflow>> {

  @Override
  public List<Cashflow> transform(DataTable table) {
    return table.cells().stream().skip(1)
        .map(fields->Cashflow.of(LocalDate.parse(fields.get(0)),Double.parseDouble(fields.get(1))))
        .collect(Collectors.toList());
  }
}
