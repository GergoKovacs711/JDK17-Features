package com.zeiss.gergo.kovacs.util;

import java.util.stream.IntStream;

public class SnippetRunner {
    public void run(int times, Runnable snippet) {
        IntStream.rangeClosed(0, times)
                 .forEach(i -> snippet.run());
    }
}

