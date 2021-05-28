package org.blacksmith.finlib.curve.dbstruct;

import lombok.Value;

@Value(staticConstructor = "of")
public class KnotDefinition {
  CurveSourceTableType tableType;
  String tableName;
  String period;
}
