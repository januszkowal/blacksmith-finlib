package org.blacksmith.finlib;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.WarmupMode;

/*
* jmhAnnotationProcessor necessary if classes placed under /src/jmh/java...
* */
public class BenchmarkRunner {

  public static void main(String[] args) throws Exception {

    String includeBenchmarks = "";
    if (args.length > 1) {
      includeBenchmarks = args[1];
    }
    Options options = new OptionsBuilder()
        .include(includeBenchmarks)
        // some setup
        //.warmupMode(WarmupMode.BULK)
        //.warmupIterations(3)
        //.forks(2)
        .build();
    new Runner(options).run();
    // below would run all classes
    //org.openjdk.jmh.Main.main(args);
  }
}
