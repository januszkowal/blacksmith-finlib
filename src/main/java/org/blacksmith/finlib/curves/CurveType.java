package org.blacksmith.finlib.curves;

public enum CurveType {
  AKIMA_SPLINE_APACHE_COMMONS("Akima Spline - Apache Commons"),
  AKIMA_SPLINE_BLACKSMITH("Akima Spline - Blacksmith"),
  LINEAR_APACHE_COMMONS("Linear - Apache Commons"),
  LINEAR_BLACKSMITH("Linear - Blacksmith");

  private final String description;

  CurveType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return this.description;
  }
}
