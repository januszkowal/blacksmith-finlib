package org.blacksmith.finlib.math.struct;

import org.blacksmith.finlib.math.struct.Matrix;

// Reduced row echelon form
public class MatrixRref<T extends Number> {
  private final Matrix<T> matrix;

  public MatrixRref(Matrix<T> matrix) {
    this.matrix = matrix;
  }

  public void reduce() {
    var lead = 0;
    for (int r = 0; r < matrix.getRowCount(); r++) {
      if (lead >= matrix.getColCount()) {
        return;
      }
      int ii = r;
      while (matrix.isCellZero(ii, lead)) {
        ii++;
        if (matrix.getRowCount() == ii) {
          ii = r;
          lead++;
          if (lead == matrix.getColCount()) {
            return;
          }
        }
      }

      matrix.swapRows(ii, r);
      matrix.divideRow(r, matrix.getCell(r, lead));

      for (int i = 0; i < matrix.getRowCount(); i++) {
        if (i == r)
          continue;
        matrix.subtractRows(i, matrix.getRow(r).multiply(matrix.getCell(i, lead)));
      }
      lead++;
    }
  }
}
