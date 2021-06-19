package org.blacksmith.finlib.math.analysis;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionConversionException;

public class Matrix {
  List<List<Fraction>> matrix;
  int numRows;
  int numCols;

  static class Coordinate {
    int row;
    int col;

    Coordinate(int r, int c) {
      row = r;
      col = c;
    }

    public String toString() {
      return "(" + row + ", " + col + ")";
    }
  }

  public Matrix(double [][] m) {
    numRows = m.length;
    numCols = m[0].length;

    matrix = new ArrayList<>(numRows);

    for (int i = 0; i < numRows; i++) {
      //
      var cols = new ArrayList<Fraction>(numCols);
      for (int j = 0; j < numCols; j++) {
        try {
          cols.add(new Fraction(m[i][j]));
        } catch (FractionConversionException e) {
          System.err.println("Fraction could not be converted from double by apache commons . . .");
        }
      }
      matrix.add(cols);
    }
  }

  public void interchange(Coordinate a, Coordinate b) {
    List<Fraction> temp = matrix.get(a.row);
    matrix.set(a.row, matrix.get(b.row));
    matrix.set(b.row, temp);

    int t = a.row;
    a.row = b.row;
    b.row = t;
  }

  public void scale(Coordinate x, Fraction d) {
    List<Fraction> row = matrix.get(x.row);
    for (int i = 0; i < numCols; i++) {
      row.set(i, row.get(i).multiply(d));
    }
  }

  public void multiplyAndAdd(Coordinate to, Coordinate from, Fraction scalar) {
    List<Fraction> row = matrix.get(to.row);
    List<Fraction> rowMultiplied = matrix.get(from.row);

    for (int i = 0; i < numCols; i++) {
      row.set(i, row.get(i).add((rowMultiplied.get(i).multiply(scalar))));
    }
  }

  public void RREF() {
    Coordinate pivot = new Coordinate(0,0);

    int submatrix = 0;
    for (int x = 0; x < numCols; x++) {
      pivot = new Coordinate(pivot.row, x);
      //Step 1
      //Begin with the leftmost nonzero column. This is a pivot column. The pivot position is at the top.
      for (int i = x; i < numCols; i++) {
        if (isColumnZeroes(pivot) == false) {
          break;
        } else {
          pivot.col = i;
        }
      }
      //Step 2
      //Select a nonzero entry in the pivot column with the highest absolute value as a pivot.
      pivot = findPivot(pivot);

      if (getCoordinate(pivot).doubleValue() == 0.0) {
        pivot.row++;
        continue;
      }

      //If necessary, interchange rows to move this entry into the pivot position.
      //move this row to the top of the submatrix
      if (pivot.row != submatrix) {
        interchange(new Coordinate(submatrix, pivot.col), pivot);
      }

      //Force pivot to be 1
      if (getCoordinate(pivot).doubleValue() != 1) {
					/*
					System.out.println(getCoordinate(pivot));
					System.out.println(pivot);
					System.out.println(matrix);
					*/
        Fraction scalar = getCoordinate(pivot).reciprocal();
        scale(pivot, scalar);
      }
      //Step 3
      //Use row replacement operations to create zeroes in all positions below the pivot.
      //belowPivot = belowPivot + (Pivot * -belowPivot)
      for (int i = pivot.row; i < numRows; i++) {
        if (i == pivot.row) {
          continue;
        }
        Coordinate belowPivot = new Coordinate(i, pivot.col);
        Fraction complement = (getCoordinate(belowPivot).negate().divide(getCoordinate(pivot)));
        multiplyAndAdd(belowPivot, pivot, complement);
      }
      //Step 5
      //Beginning with the rightmost pivot and working upward and to the left, create zeroes above each pivot.
      //If a pivot is not 1, make it 1 by a scaling operation.
      //Use row replacement operations to create zeroes in all positions above the pivot
      for (int i = pivot.row; i >= 0; i--) {
        if (i == pivot.row) {
          if (getCoordinate(pivot).doubleValue() != 1.0) {
            scale(pivot, getCoordinate(pivot).reciprocal());
          }
          continue;
        }
        if (i == pivot.row) {
          continue;
        }

        Coordinate abovePivot = new Coordinate(i, pivot.col);
        Fraction complement = (getCoordinate(abovePivot).negate().divide(getCoordinate(pivot)));
        multiplyAndAdd(abovePivot, pivot, complement);
      }
      //Step 4
      //Ignore the row containing the pivot position and cover all rows, if any, above it.
      //Apply steps 1-3 to the remaining submatrix. Repeat until there are no more nonzero entries.
      if ((pivot.row + 1) >= numRows || isRowZeroes(new Coordinate(pivot.row+1, pivot.col))) {
        break;
      }

      submatrix++;
      pivot.row++;
    }
  }

  public boolean isColumnZeroes(Coordinate a) {
    for (int i = 0; i < numRows; i++) {
      if (matrix.get(i).get(a.col).doubleValue() != 0.0) {
        return false;
      }
    }

    return true;
  }

  public boolean isRowZeroes(Coordinate a) {
    for (int i = 0; i < numCols; i++) {
      if (matrix.get(a.row).get(i).doubleValue() != 0.0) {
        return false;
      }
    }

    return true;
  }

  public Coordinate findPivot(Coordinate a) {
    int first_row = a.row;
    Coordinate pivot = new Coordinate(a.row, a.col);
    Coordinate current = new Coordinate(a.row, a.col);

    for (int i = a.row; i < (numRows - first_row); i++) {
      current.row = i;
      if (getCoordinate(current).doubleValue() == 1.0) {
        interchange(current, a);
      }
    }

    current.row = a.row;
    for (int i = current.row; i < (numRows - first_row); i++) {
      current.row = i;
      if (getCoordinate(current).doubleValue() != 0) {
        pivot.row = i;
        break;
      }
    }


    return pivot;
  }

  public Fraction getCoordinate(Coordinate a) {
    return matrix.get(a.row).get(a.col);
  }

  public String toString() {
    return matrix.toString().replace("], ", "]\n");
  }
}
