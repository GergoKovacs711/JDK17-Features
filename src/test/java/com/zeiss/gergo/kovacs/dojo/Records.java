package com.zeiss.gergo.kovacs.dojo;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;

public class Records implements BasicFunctionalities {
    record Pojo(String name, int number) {
        // you get a default constructor, getters, hashCode, equals, toString for "free"
    }

    @Test
    void basics() {
        final var basicPojo = new Pojo("Some name", 123);

        final String nameFromField = basicPojo.name;
        final int numberFromField = basicPojo.number;

        final String nameFromMethod = basicPojo.name();
        final int numberFromMethod = basicPojo.number();

        final String toString = basicPojo.toString();
        final int hashCode = basicPojo.hashCode();

        final var pojoA = new Pojo("pojo", 10);
        final var pojoB = new Pojo("pojo", 10);
        final var pojoC = new Pojo("different pojo", 0);

        logger.print("Hash codes for ")
              .pairs(
                      pojoA.hashCode(), "pojoA",
                      pojoB.hashCode(), "pojoB",
                      pojoC.hashCode(), "pojoC"
              )
              .line()


              .print("Equals on")
              .pairs(
                      basicPojo.equals(basicPojo), "same record",
                      pojoA.equals(pojoB), "different record, same field values",
                      pojoA.equals(pojoC), "different record, different field values"
              ).line()


              .print("toString()s")
              .pairs(
                      pojoA.toString(), "pojoA",
                      pojoB.toString(), "pojoB",
                      pojoC.toString(), "pojoC"
              );
    }

    record CustomisedPojo(List<String> words, int sumOfLetters) {
        final static BinaryOperator<Integer> SUM_OF_LETTERS = (length, sum) -> sum += length;

        public CustomisedPojo {
            Objects.requireNonNull(words);
        }

        public CustomisedPojo(List<String> words) {
//                Objects.requireNonNull(words); // Nope. First line must be the primary constructor
            this(words, words.stream()
                             .map(String::length)
                             .reduce(0, SUM_OF_LETTERS)
            );
        }

        @Override
        public int sumOfLetters() {
            // this still works...
            logger.print("shhhh!");
            return sumOfLetters + 1;
        }
    }

    @Test
    void customisedPojo() {
        final var animals = List.of("dog", "cat", "giraffe", "snake");
        final var pojo = new CustomisedPojo(animals, 100);
        final var pojoWithOptionalConstructor = new CustomisedPojo(animals);

        logger.pairs(
                pojo.sumOfLetters, "using accessor",
                pojo.sumOfLetters(), "using getter",
                pojoWithOptionalConstructor.sumOfLetters, "using accessor",
                pojoWithOptionalConstructor.sumOfLetters(), "using getter"
        );

        // NPE from the custom constructor
//            final var npe = new CustomisedPojo(null);
//            final var npe = new CustomisedPojo(null, 21);
    }
}
