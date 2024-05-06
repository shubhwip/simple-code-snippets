package me.shubhamjain.codesamples.virtualthreads;

import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class SimpleVirtualThreadExecutor {

    public static void main(String[] args) {
        try(var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(1, 10000).forEach( i -> executor.submit(() -> {
                System.out.println(i);
                Thread.sleep(1000);
                return i;
            }));
        }
    }
}
