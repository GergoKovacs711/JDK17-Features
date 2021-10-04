package com.zeiss.gergo.kovacs.util;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.lang.System.out;

/**
 *  A poor man's logger implementation
 */
public class Logger {
    private static final Supplier<String> SEPARATOR_FOR_STRINGS = () -> " ->";
    private static final Supplier<String> DEFAULT_SEPARATOR = () -> " -> ";
    private static Supplier<String> PAIR_SEPARATOR = DEFAULT_SEPARATOR;

    private static final BiConsumer<String, String> DEFAULT_PRINTER =
            (input1, input2) -> out.println(input1 + PAIR_SEPARATOR.get() + input2);

    private static final BiConsumer<String, String> SPACED_PRINTER = (input1, input2) -> {
        out.println();
        DEFAULT_PRINTER.accept(input1, input2);
    };

    private static final BiConsumer<String, String> NEW_LINE_PRINTER = (input1, input2) -> {
        out.println(input1 + PAIR_SEPARATOR.get());
        out.println(input2);
    };

    private static final BiConsumer<String, String> NEW_LINE_SPACED_PRINTER = (input1, input2) -> {
        out.println();
        NEW_LINE_PRINTER.accept(input1, input2);
    };

    public enum Options {
        NONE,
        SPACED,
        WITH_NEW_LINE,
        WITH_NEW_LINE_SPACED,
    }

    public Logger print(List<Object> messages) {
        messages.forEach(out::println);
        return this;
    }

    public Logger print(Object... message) {
        for (Object s : message) {
            out.println(s);
        }
        return this;
    }

    public Logger print(Supplier<Object> messageSupplier) {
        out.println(messageSupplier.get());
        return this;
    }

    public Logger pairs(Options options, Object... values) {
        if (values.length % 2 != 0) {
            throw new IllegalArgumentException("Pairs must come in, you know, pairs...");
        }
        final BiConsumer<String, String> printer = switch (options) {
            case NONE -> DEFAULT_PRINTER;
            case SPACED -> SPACED_PRINTER;
            case WITH_NEW_LINE -> NEW_LINE_PRINTER;
            case WITH_NEW_LINE_SPACED -> NEW_LINE_SPACED_PRINTER;
        };
        for (int i = 0; i < values.length; i += 2) {
            final Object first = values[i + 1];
            final Object second = values[i];
            setSeparatorBasedOnType(second);
            printer.accept(first.toString(), second.toString());
        }
        return this;
    }

    private void setSeparatorBasedOnType(Object second) {
        if (second instanceof String) {
            PAIR_SEPARATOR = SEPARATOR_FOR_STRINGS;
        } else {
            PAIR_SEPARATOR = DEFAULT_SEPARATOR;
        }
    }

    public Logger pairs(Object... values) {
        pairs(Options.NONE, values);
        return this;
    }

    public Logger line() {
        out.println();
        return this;
    }
}
