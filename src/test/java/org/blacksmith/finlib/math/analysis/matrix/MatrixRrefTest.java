package org.blacksmith.finlib.math.analysis.matrix;

import java.util.List;

import org.blacksmith.finlib.math.analysis.ArrayTestUtils;
import org.blacksmith.finlib.math.analysis.PolynomialTestUtils;
import org.blacksmith.finlib.math.struct.DoubleOperations;
import org.blacksmith.finlib.math.struct.Matrix2D;
import org.blacksmith.finlib.math.struct.MatrixRref;
import org.blacksmith.test.VariableSource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

public class MatrixRrefTest {

  static double[][] matrix1b = {
      { 1, 2, -1, -4 },
      { 2, 3, -1, -11 },
      { -2, 0, -3, 22 }
  };
  static double[][] matrix1a = {
      { 1, 0, 0, -8 },
      { 0, 1, 0, 1 },
      { 0, 0, 1, -2 }
  };
  static double matrix2b[][] = {
      { 2, 0, -1, 0, 0 },
      { 1, 0, 0, -1, 0 },
      { 3, 0, 0, -2, -1 },
      { 0, 1, 0, 0, -2 },
      { 0, 1, -1, 0, 0 }
  };
  static double matrix2a[][] = {
      { 1, 0, 0, 0, 0 },
      { 0, 1, 0, 0, 0 },
      { 0, 0, 1, 0, 0 },
      { 0, 0, 0, 1, 0 },
      { 0, 0, 0, 0, 1 }
  };
  static double matrix3b[][] = {
      { 1, 2, 3, 4, 3, 1 },
      { 2, 4, 6, 2, 6, 2 },
      { 3, 6, 18, 9, 9, -6 },
      { 4, 8, 12, 10, 12, 4 },
      { 5, 10, 24, 11, 15, -4 }
  };
  static double matrix3a[][] = {
      { 1, 2, 0, 0, 3, 4 },
      { 0, 0, 1, 0, 0, -1 },
      { 0, 0, 0, 1, 0, 0 },
      { 0, 0, 0, 0, 0, 0 },
      { 0, 0, 0, 0, 0, 0 }
  };
  static double matrix4b[][] = {
      { 0, 1 },
      { 1, 2 },
      { 0, 5 }
  };
  static double matrix4a[][] = {
      { 1, 0 },
      { 0, 1 },
      { 0, 0 }
  };
  public static List<Arguments> arrays = List.of(Arguments.of(matrix1b, matrix1a), Arguments.of(matrix2b, matrix2a),
      Arguments.of(matrix3b, matrix3a), Arguments.of(matrix4b, matrix4a));

  @ParameterizedTest
  @VariableSource("arrays")
  public void rrefTest(double[][] in, double[][] out) {
    Matrix2D<Double> matrix = new Matrix2D<>(in, Double.class, new DoubleOperations());
    var matrixOper = new MatrixRref<>();
    matrixOper.reduce(matrix);
    double[][] result = matrix.toDoubleArray();
    ArrayTestUtils.assertArray(result, out, 1e-9);
  }
}
