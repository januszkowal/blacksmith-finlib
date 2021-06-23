package org.blacksmith.finlib.math.struct;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class Rref {
  public void rref(double[][] mat) {
    var lead = 0;
    for (int r = 0; r < mat.length; r++) {
      if (mat[0].length <= lead) {
        return;
      }
      int ii = r;
      while (mat[ii][lead] == 0) {
        ii++;
        if (mat.length == ii) {
          ii = r;
          lead++;
          if (mat[0].length == lead) {
            return;
          }
        }
      }

      var tmp = mat[ii];
      mat[ii] = mat[r];
      mat[r] = tmp;

      var val = mat[r][lead];
      for (var j = 0; j < mat[0].length; j++) {
        mat[r][j] = mat[r][j] / val;
      }

      for (int i = 0; i < mat.length; i++) {
        if (i == r)
          continue;
        val = mat[i][lead];
        for (var j = 0; j < mat[0].length; j++) {
          mat[i][j] = mat[i][j] - val * mat[r][j];
        }
      }
      lead++;
    }
    return;
  }

  public String toString(double[][] array) {
    return Arrays.deepToString(array);
//    DoubleStream x = Arrays.stream(array).flatMapToDouble(Arrays::stream);//.collect(Collectors.joining());
    //    Arrays.asList(array).stream().flatMapToDouble(d -> Arrays.stream(d).flatMap().flatMap(d->d))
//    return Stream.of(array).flatMapToDouble(Stream::of)
//        .map(row -> Stream.of(row).map(r -> r.toString()).collect(Collectors.joining(",", "[", "]")))
//        .collect(Collectors.joining(",", "Matrix[", "]"));
  }
}
