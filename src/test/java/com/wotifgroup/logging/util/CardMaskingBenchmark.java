/*
 * #%L
 * Wotif Group Logging Utilities
 * %%
 * Copyright (C) 2014 The Wotif Group
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.wotifgroup.logging.util;

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
