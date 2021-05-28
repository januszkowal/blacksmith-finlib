package org.blacksmith.finlib.curve.algorithm;

public enum AlgorithmType {
  AKIMA_SPLINE_APACHE_COMMONS("Akima Spline - Apache Commons"),
  AKIMA_SPLINE_BLACKSMITH("Akima Spline - Blacksmith"),
  LINEAR_APACHE_COMMONS("Linear - Apache Commons"),
  LINEAR_BLACKSMITH("Linear - Blacksmith");

  private final String description;

  AlgorithmType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return this.description;
  }
}
