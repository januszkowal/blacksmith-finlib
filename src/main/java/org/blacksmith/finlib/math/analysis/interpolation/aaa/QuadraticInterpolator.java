package org.blacksmith.finlib.math.analysis.interpolation.aaa;

import org.blacksmith.finlib.math.analysis.interpolation.PolynomialFunction;
import org.blacksmith.finlib.math.analysis.interpolation.PolynomialInterpolator;
import org.blacksmith.finlib.math.analysis.interpolation.PolynomialSplineFunction;
import org.blacksmith.finlib.math.struct.DoubleOperations;
import org.blacksmith.finlib.math.struct.Matrix2D;
import org.blacksmith.finlib.math.struct.MatrixRref;

public class QuadraticInterpolator implements PolynomialInterpolator {
  @Override
  public PolynomialSplineFunction interpolate(double[] xValues, double[] yValues) {
    int numberOfUnknowns = 3 * (xValues.length - 1);
    var matrix = new Matrix2D<>(numberOfUnknowns, numberOfUnknowns + 1, Double.class, new DoubleOperations());
    // through points equations
    for (int i = 0, j = 0; j < xValues.length - 1; i += 2, j++) {
      double x0 = xValues[j];
      double y0 = yValues[j];
      double x1 = xValues[j + 1];
      double y1 = yValues[j + 1];
      matrix.setCell(i, j * 3, x0 * x0);
      matrix.setCell(i, j * 3 + 1, x0);
      matrix.setCell(i, j * 3 + 2, 1d);
      matrix.setCell(i, numberOfUnknowns, y0);
      matrix.setCell(i + 1, j * 3, x1 * x1);
      matrix.setCell(i + 1, j * 3 + 1, x1);
      matrix.setCell(i + 1, j * 3 + 2, 1d);
      matrix.setCell(i + 1, numberOfUnknowns, y1);
    }

    // derivative equations
    for (int i = (xValues.length - 1) * 2, j = 0; i < numberOfUnknowns - 1; i++, j++) {
      double x = xValues[j + 1];
      matrix.setCell(i, j * 3, 2 * x);
      matrix.setCell(i, j * 3 + 1, 1d);
      matrix.setCell(i, j * 3 + 3, -2 * x);
      matrix.setCell(i, j * 3 + 4, -1d);
    }
    // additional information / boundary condition (required)
    matrix.setCell(numberOfUnknowns - 1, 0, 1d);
    double[][] aaa = matrix.toDoubleArray();
    var matrixOper = new MatrixRref<>(matrix);
    matrixOper.reduce();
    PolynomialFunction[] polynomials = new PolynomialFunction[matrix.getRowCount() / 3];
    for (int i = 0; i < matrix.getRowCount(); i += 3) {
      polynomials[i / 3] = polynomial(matrix, i);
    }
    return new PolynomialSplineFunction(xValues, polynomials);
  }

  private PolynomialFunction polynomial(Matrix2D<Double> matrix, int index) {
    double c = matrix.getCell(index, matrix.getColCount() - 1).doubleValue();
    double b = matrix.getCell(index + 1, matrix.getColCount() - 1).doubleValue();
    double a = matrix.getCell(index + 2, matrix.getColCount() - 1).doubleValue();
    return new PolynomialFunction(a, b, c);
  }

  private PolynomialFunction polynomial(double[][] matrix, int index) {
    double c = matrix[index][matrix[0].length - 1];
    double b = matrix[index + 1][matrix[0].length - 1];
    double a = matrix[index + 2][matrix[0].length - 1];
    return new PolynomialFunction(a, b, c);
  }
}
