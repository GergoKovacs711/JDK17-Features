package com.zeiss.gergo.kovacs.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

public class RandomiserTest {
    @Test
    void testGetRandomElementFromArray() {
        final var randomiser = new Randomiser();
        final String[] testArray = {"first", "second", "third", "fourth", "fifth", "sixth"};

        IntStream.rangeClosed(0, 10000)
                .forEach(iteration -> randomiser.getElement(testArray));
    }

    @Test
    void testGetRandomElementFromList() {
        final var randomiser = new Randomiser();
        final var testArray = List.of("first", "second", "third", "fourth", "fifth", "sixth");

        IntStream.rangeClosed(0, 10000)
                 .forEach(iteration -> randomiser.getElement(testArray));
    }

    @Test
    void testGetRandomElementOrNullFromList() {
        final var randomiser = new Randomiser();
        final var testArray = List.of("first", "second", "third", "fourth", "fifth", "sixth");

        IntStream.rangeClosed(0, 10000)
                 .forEach(iteration -> randomiser.getElementOrNull(testArray));
    }
}
