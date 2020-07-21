package org.blacksmith.finlib.schedule.helper;

import java.util.List;
import java.util.function.Function;

import org.blacksmith.commons.arg.ArgChecker;

public class CascadeFunction<E> implements Function<E,E> {
  private final Function<E,E> function;

  public CascadeFunction(List<Function<E,E>> functions) {
    ArgChecker.notEmpty(functions,"Functions must be not empty");
    var fff = functions.get(0);
    for (int i=1; i<functions.size();i++) {
      fff = fff.andThen(functions.get(i));
    }
    this.function = fff;
  }

  @Override
  public E apply(E value) {
//    for (Function<E,E> updater: updaters) {
//      value = updater.apply(value);
//    }
//    return value;
    return function.apply(value);
  }
}
