package org.blacksmith.finlib.math.analysis;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest {
  public static void main (String[] args) {
    double[][] matrix_1 = {
        {1, 2, -1, -4},
        {2, 3, -1, -11},
        {-2, 0, -3, 22}
    };

    Matrix x = new Matrix(matrix_1);
    System.out.println("before\n" + x.toString() + "\n");
    x.RREF();
    System.out.println("after\n" + x.toString() + "\n");

    double matrix_2 [][] = {
        {2, 0, -1, 0, 0},
        {1, 0, 0, -1, 0},
        {3, 0, 0, -2, -1},
        {0, 1, 0, 0, -2},
        {0, 1, -1, 0, 0}
    };

    Matrix y = new Matrix(matrix_2);
    System.out.println("before\n" + y.toString() + "\n");
    y.RREF();
    System.out.println("after\n" + y.toString() + "\n");

    double matrix_3 [][] = {
        {1, 2, 3, 4, 3, 1},
        {2, 4, 6, 2, 6, 2},
        {3, 6, 18, 9, 9, -6},
        {4, 8, 12, 10, 12, 4},
        {5, 10, 24, 11, 15, -4}
    };

    Matrix z = new Matrix(matrix_3);
    System.out.println("before\n" + z.toString() + "\n");
    z.RREF();
    System.out.println("after\n" + z.toString() + "\n");

    double matrix_4 [][] = {
        {0, 1},
        {1, 2},
        {0,5}
    };

    Matrix a = new Matrix(matrix_4);
    System.out.println("before\n" + a.toString() + "\n");
    a.RREF();
    System.out.println("after\n" + a.toString() + "\n");
  }
}
