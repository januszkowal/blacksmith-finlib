package org.blacksmith.finlib.math.analysis.interpolation;

import java.util.List;

import lombok.Value;

@Value(staticConstructor = "of")
public class PolynomialFunctionMetadata {
  double xLeft;
  List<Double> coefficients;
}
