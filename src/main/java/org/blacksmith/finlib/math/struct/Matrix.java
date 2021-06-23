package org.blacksmith.finlib.math.struct;

import java.lang.reflect.Array;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.blacksmith.commons.arg.ArgChecker;

public class Matrix<T extends Number> {
  private final Class<T> clazz;
  private T[][] array;
  private int rowCount;
  private int colCount;

  private FieldOperations<T> operator;

  public Matrix(int rowCount, int colCount, Class<T> clazz, FieldOperations<T> operator) {
    this.clazz = clazz;
    this.operator = operator;
    this.rowCount = rowCount;
    this.colCount = colCount;
    this.array = (T[][]) Array.newInstance(clazz, rowCount, colCount);
    for (int r = 0; r < rowCount; r++) {
      for (int c = 0; c < colCount; c++) {
        array[r][c] = this.operator.factory(0.d);
      }
    }
  }

  public Matrix(double[][] m, Class<T> clazz, FieldOperations<T> operator) {
    ArgChecker.notNull(m);
    ArgChecker.isTrue(m.length > 0);
    ArgChecker.isTrue(m[0].length > 0);
    this.clazz = clazz;
    this.operator = operator;
    this.rowCount = m.length;
    this.colCount = m[0].length;
    for (int r = 0; r < rowCount; r++) {
      ArgChecker.isTrue(m[r].length == colCount);
    }
    this.array = (T[][]) Array.newInstance(clazz, rowCount, colCount);
    for (int r = 0; r < rowCount; r++) {
      for (int c = 0; c < colCount; c++) {
        array[r][c] = this.operator.factory(m[r][c]);
      }
    }
  }

  public void addRow(int row, T augend) {
    rowOperation(row, augend, operator.addition());
  }

  public void addRows(int row, VArray<T> augend) {
    rowOperation(row, augend, operator.addition());
  }

  public void addRows(int result, int row1, int row2) {
    rowOperation(result, row1, row2, operator.addition());
  }

  public void subtractRows(int row, VArray<T> subtrahend) {
    rowOperation(row, subtrahend, operator.subtraction());
  }

  public void subtractRow(int result, int row1, int row2) {
    rowOperation(result, row1, row2, operator.subtraction());
  }

  public void multiplyRows(int row, VArray<T> divisor) {
    rowOperation(row, divisor, operator.multiplication());
  }

  public void multiplyRow(int row, T multiplicand) {
    rowOperation(row, multiplicand, operator.multiplication());
  }

  public void multiplyRows(int result, int row1, int row2) {
    rowOperation(result, row1, row2, operator.multiplication());
  }

  public void divideRows(int row, VArray<T> divisor) {
    rowOperation(row, divisor, operator.division());
  }

  public void divideRow(int row, T divisor) {
    rowOperation(row, divisor, operator.division());
  }

  public void divideRows(int result, int row1, int row2) {
    rowOperation(result, row1, row2, operator.division());
  }

  public void swapRows(int i, int j) {
    if (i == j)
      return;
    var tmp = array[i];
    array[i] = array[j];
    array[j] = tmp;
  }

  public void setCell(int row, int col, T value) {
    array[row][col] = value;
  }

  public T getCell(int row, int col) {
    return array[row][col];
  }

  public boolean isCellZero(int row, int col) {
    return this.operator.isZero(getCell(row, col));
  }

  public boolean isRowZeroes(int row) {
    for (int j = 0; j < colCount; j++) {
      if (this.operator.isZero(getCell(row, j))) {
        return false;
      }
    }
    return true;
  }

  public int getRowCount() {
    return this.rowCount;
  }

  public int getColCount() {
    return this.colCount;
  }

  public T[][] toArray() {
    return this.array;
  }

  public double[][] toDoubleArray() {
    double[][] result = new double[rowCount][colCount];
    for (int i = 0; i < rowCount; i++) {
      for (int j = 0; j < colCount; j++) {
        result[i][j] = array[i][j].doubleValue();
      }
    }
    return result;
  }

  public VArray<T> getRow(int row) {
    return new VArray<T>(array[row], clazz, operator);
  }

  public void setRow(int row, VArray<T> data) {
    ArgChecker.isTrue(colCount == data.size());
    for (int i = 0; i < colCount; i++) {
      array[row][i] = data.get(i);
    }
  }

  public VArray<T> getCol(int col) {
    T[] colArray = (T[]) Array.newInstance(clazz, rowCount);
    for (int i = 0; i < rowCount; i++) {
      colArray[i] = array[i][col];
    }
    return new VArray<T>(colArray, clazz, operator);
  }

  public void setCol(int col, VArray<T> data) {
    ArgChecker.isTrue(rowCount == data.size());
    for (int i = 0; i < rowCount; i++) {
      array[i][col] = data.get(i);
    }
  }

  public FieldOperations<T> getOperator() {
    return operator;
  }

  @Override
  public String toString() {
    return Stream.of(array).map(row -> Stream.of(row).map(r -> r.toString()).collect(Collectors.joining(",", "[", "]")))
        .collect(Collectors.joining(",", "Matrix[", "]"));
  }

  public void rowOperation(int row, int row1, int row2, BiFunction<T, T, T> function) {
    for (int col = 0; col < colCount; col++) {
      array[row][col] = function.apply(array[row1][col], array[row2][col]);
    }
  }

  protected void rowOperation(int row, VArray<T> operand, BiFunction<T, T, T> function) {
    ArgChecker.isTrue(colCount == operand.size());
    for (int col = 0; col < colCount; col++) {
      array[row][col] = function.apply(array[row][col], operand.get(col));
    }
  }

  protected void rowOperation(int row, T operand, BiFunction<T, T, T> function) {
    for (int col = 0; col < colCount; col++) {
      array[row][col] = function.apply(array[row][col], operand);
    }
  }
}
