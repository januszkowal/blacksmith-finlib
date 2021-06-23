package org.blacksmith.finlib.math.analysis.matrix;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.blacksmith.finlib.math.struct.DoubleOperations;
import org.blacksmith.finlib.math.struct.Matrix;
import org.blacksmith.finlib.math.struct.VArray;
import org.junit.jupiter.api.Test;

class VArrayTest {
  @Test
  public void multiply() {
    Double[] array = new Double[]{1.1, 2.05, 3.5};
    VArray<Double> varray = new VArray<>(array, Double.class, new DoubleOperations());
    VArray<Double> result = varray.multiply(1.5);
    System.out.println(arrayToString(array));
    System.out.println(arrayToString(varray.toArray()));
    System.out.println(arrayToString(result.toArray()));
  }

  @Test
  public void multiply1() {
    double[][] array = new double[][]{{1d, 2d, 3d},{3d, 5d, 7d}};
    Matrix<Double> matrix = new Matrix<Double>(array, Double.class, new DoubleOperations());
    System.out.println("m0:" + matrix.toString());
    VArray<Double> varray = matrix.getRow(1).multiply(2d);
    System.out.println("a1:"+varray);
    matrix.subtractRows(0, varray);
    matrix.subtractRows(1, varray);
    //    VArray<Double> result = varray.multiply(1.5);
        System.out.println(matrix.toString());
    //    System.out.println(arrayToString(array));
//    System.out.println(arrayToString(result.toArray()));
  }

  private String arrayToString(Double[] array) {
    return Stream.of(array).map(d -> d.toString()).collect(Collectors.joining(","));
  }

  private String matrixToString(Matrix matrix) {
    return matrix.toString();
    //return Stream.of(array).map(d -> d.toString()).collect(Collectors.joining(","));
  }

  private void printMatrix(double[][] matrix, String prefix) {
    for (int i = 0; i < matrix.length; i++) {
      System.out.println(prefix + ":" + i + " " + Arrays.stream(matrix[i])
          .mapToObj(o -> String.valueOf(o)).collect(Collectors.joining(",")));
    }
  }

}
