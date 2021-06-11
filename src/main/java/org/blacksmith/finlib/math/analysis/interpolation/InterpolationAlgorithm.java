package org.blacksmith.finlib.math.analysis.interpolation;

public enum InterpolationAlgorithm {
  AKIMA_SPLINE_APACHE_COMMONS("Akima Spline - Apache Commons"),
  AKIMA_SPLINE_BLACKSMITH("Akima Spline - Blacksmith"),
  LINEAR_APACHE_COMMONS("Linear - Apache Commons"),
  LINEAR_BLACKSMITH("Linear - Blacksmith");

  private final String description;

  InterpolationAlgorithm(String description) {
    this.description = description;
  }

  public String getDescription() {
    return this.description;
  }

  public InterpolationAlgorithm fromName(String name) {
    return Enum.valueOf(InterpolationAlgorithm.class, name);
  }
}
