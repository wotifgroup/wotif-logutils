////////////////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2014, Wotif.com. All rights reserved.
//
// This is unpublished proprietary source code of Wotif.com.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
////////////////////////////////////////////////////////////////////////////////
package com.wotifgroup.logging.util;

import org.junit.Assert;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class CardMaskingBenchmark {

    @Param({"Value '4111111111111111' is not facet-valid", "Value '4111111111111112' is not facet-valid", "Value 'AAAAAAAAAAAAAAAA' is not facet-valid"})
    public String input;

    @org.openjdk.jmh.annotations.Benchmark
    public StringBuilder benchmark() throws Exception {
        StringBuilder sb = new StringBuilder(input);
        CardMaskingUtil.mask(sb);
        return sb;
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(CardMaskingBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

}
