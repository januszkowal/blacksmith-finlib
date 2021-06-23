package org.blacksmith.finlib.math.analysis.interpolation;

public enum InterpolationAlgorithm {
  AKIMA_SPLINE("Akima Spline"),
  DOUBLE_QUADRATIC("Double Quadratic"),
  QUADRATIC("Quadratic"),
  LINEAR("Linear"),
  AKIMA_SPLINE_APACHE_COMMONS("Akima Spline - Apache Commons"),
  LINEAR_APACHE_COMMONS("Linear - Apache Commons");

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
