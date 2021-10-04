package com.zeiss.gergo.kovacs;

import java.util.function.Supplier;

// TODO: fix logging
public class LOGGER {
    public static void info(String message) {
        System.out.println(message);
    }
    public static void info(Supplier<String> messageSupplier) {
        System.out.println(messageSupplier.get());
    }
}
