package com.zeiss.gergo.kovacs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Supplier;


/*
LTS Releases:
    * 8 LTS  -> interface static and default methods, method references (static, method, constructor), Optional
    * 9      -> Quick GET Request, Process API improvements, AutoClosable, Diamond Operator with anonymous classes,interface with private methods, JShell Command Line Tool, Reactive Streams with java.util.concurrent.Flow, Immutable Set, Optional to Stream
    * 10     -> local variable type inference (var), Unmodifiable Collections changes, Optional*.orElseThrow()

    * 11 LTS -> String methods (isBlank, lines, strip, stripLeading, stripTrailing, repeat), File methods: readString writeString, Collection:toArray, Predicate:not
    * 12     -> String::transform, String::indent, File::mismatch, Collectors::teeing, CompactNumberFormat
    * 13     -> Text Blocks (preview)
    * 14     -> Switch Expressions,
    * 15     -> Helpful NullPointerExceptions, Text Blocks
    * 16     -> Records, Stream.toList(), Pattern Matching for instanceof, Sealed Classes, jpackage
    * 17 LTS -> Sealed Classes, switch pattern matching (preview)

*/

public class FeatureGroups {
    @Test
    public void stringTransform() {
        final var string = " a majestic string instance   ";
        Integer length = string.transform(String::trim)
                .transform(String::length);
        LOGGER.info("length: " + length);

        string.transform(String::lines)
                .forEach(LOGGER::info);

        final Supplier<String> potentiallyNullString = () -> new Random().nextBoolean() ? "some basic string " : null;
        final var testString = potentiallyNullString.get();

        List<String> words = Optional.ofNullable(testString)
                .map(str -> str.transform(it -> it.split(" ")))
                .map(Arrays::asList)
                .orElse(List.of("default", "case"));
        LOGGER.info(words::toString);


//        testString.transform( it -> {
//            it.
//        });
//        testString.isBlank();
//        testString.lines();
//        testString.strip();
//
//        testString.stripLeading();
//        testString.stripLeading();
//
//        testString.stripIndent();
//        testString.indent();
//
//        testString.repeat();
//        testString.translateEscapes();
    }

    @Test
    public void stringStrip() {
        //        testString.stripLeading();
//        testString.stripLeading();

    }
    @Test
    public void group2() {
        System.out.println("in com.zeiss.gergo.kovacs.FeatureGroups");
    }

    @Test
    public void group3() {
        System.out.println("in com.zeiss.gergo.kovacs.FeatureGroups");
    }

    @Test
    public void group4() {
        System.out.println("in com.zeiss.gergo.kovacs.FeatureGroups");
    }

    @Test
    public void group5() {
        System.out.println("in com.zeiss.gergo.kovacs.FeatureGroups");
    }
}

