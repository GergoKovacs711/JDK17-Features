package com.zeiss.gergo.kovacs.dojo.challenges;

import com.zeiss.gergo.kovacs.dojo.BasicFunctionalities;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class StringChallenges implements BasicFunctionalities {

    /**
     * *       /\
     * *      /\/\
     * *     /\/\/\
     * *    /\/\/\/\
     */
    @Test
    void challengeDeckOfCards() {
        final var towerOfCards = """
                   /\\
                  /\\/\\
                 /\\/\\/\\
                /\\/\\/\\/\\""";
        logger.print(towerOfCards);
        assertNotEquals(towerOfCards.charAt(towerOfCards.length() - 1), '\n');
    }

    /**
     * *       //\
     * *      //\/\
     * *     //\/\/\
     * *    //\/\/\/\
     */
    @Test
    void challengeDeckOfCardsWithShadowsOnTheLeft() {
        final var towerOfCards = """
                   //\\
                  //\\/\\
                 //\\/\\/\\
                //\\/\\/\\/\\""";
        assertNotEquals(towerOfCards.charAt(towerOfCards.length() - 1), '\n');
    }

    /**
     * *       /\\
     * *      /\/\\
     * *     /\/\/\\
     * *    /\/\/\/\\
     */
    @Test
    void challengeDeckOfCardsWithShadowsOnTheRight() {
        final var towerOfCards = """
                   /\\\\
                  /\\/\\\\
                 /\\/\\/\\\\
                /\\/\\/\\/\\\\""";
        logger.print(towerOfCards);
        assertNotEquals(towerOfCards.charAt(towerOfCards.length() - 1), '\n');
    }

    /**
     * Triple quotes
     */
    @Test
    void challengeTripleQuotes() {
        final var tripleDoubleQuotes = """
                ""\"""";
        logger.print(tripleDoubleQuotes);
        assertNotEquals(tripleDoubleQuotes.charAt(tripleDoubleQuotes.length() - 1), '\n');
    }

    /**
     * Triple quotes and new lines
     */
    @Test
    void challengeTripleQuotesAndNewLines() {
        final var tripleDoubleQuotes = """              
                ""\"""";
        logger.print(tripleDoubleQuotes);
        assertNotEquals(tripleDoubleQuotes.charAt(tripleDoubleQuotes.length() - 1), '\n');
        assertNotEquals(tripleDoubleQuotes.charAt(0), '\n');
    }

    /**
     * Six spaces
     */
    @Test
    void challengeSixWhiteSpacesInARow() {
        final var sixWhitespacesInARow = """
                some text with six whitespaces at the end      \s\
                """;
        String expected = sixWhitespacesInARow.substring(sixWhitespacesInARow.length() - 6);
        assertEquals(expected, "      ");
    }

    /**
     * Hello...
     */
    @Test
    void challengeHelloDotDotDot() {
        final var helloDotDotDot = """
                Hello %s\
                """;
        assertEquals(helloDotDotDot.toLowerCase(Locale.ROOT).formatted("Thomas"), "hello Thomas");
    }

    String solution1(String str) {
        return str.transform(it -> it.chars()
                                     .mapToObj(String::valueOf)
                                     .collect(Collectors.joining("")));
    }

    /**
     * Complete the solution so that it returns the ASCII codes for the characters in the input string.
     * Each character in the string should be transformed separately.
     * <p>
     * The solution should use the String.transform() method.
     * <p>
     * Example:
     * "a" -> 97
     * "b" -> 98
     */
    @Test
    void transformChallenge1() {
        final var input = "abcdef";
        String charactersToInt = solution1(input);

        logger.print(charactersToInt);
        assertEquals("979899100101102", charactersToInt);
    }

    String solution2(String input, Function<String, String> mapper) {
        return input.transform(it -> it.chars()
                                       .mapToObj(c -> mapper.apply(String.valueOf((char) c)))
                                       .collect(Collectors.joining(""))
        );
    }

    /**
     * Complete solution so that it takes a string and a lambda as input and runs the mapper lambda on each and every character found in the input string.
     * <p>
     * The solution should use the String.transform() method.
     * Example:
     * ("abc", String::toUpperCase) -> "ABC"
     * ("abc", it -> it) -> "abc"
     */
    @Test
    void transformChallenge2() {
        final var input = "abcdef";
        final var noTransformation = solution2(input, it -> it);

        logger.print(noTransformation);
        assertEquals("abcdef", noTransformation);

        final var charactersToInt = solution2(input, it -> String.valueOf((int) (it.charAt(0))));

        logger.print(charactersToInt);
        assertEquals("979899100101102", charactersToInt);

        final var repeatingCharacters = solution2(input, str -> str.repeat(str.length()) + " ");
        logger.print(repeatingCharacters);
        assertEquals("a b c d e f ", repeatingCharacters);

        final var inputForEncounters = "abcdef,abcdef";
        Map<Object, Integer> map = new ConcurrentHashMap<>();
        final var repeatingBasesOnHowManyTimesEncountered = solution2(inputForEncounters, str -> {
            Integer timesEncountered = map.getOrDefault(str, 1);
            map.put(str, timesEncountered + 1);
            return str.repeat(timesEncountered) + " ";
        });

        logger.print(repeatingBasesOnHowManyTimesEncountered);
        assertEquals("a b c d e f , aa bb cc dd ee ff ", repeatingBasesOnHowManyTimesEncountered);
    }

    enum Style {
        INDENT,
        UPPERCASE,
        BOTH
    }

    String solution3(String input, Style style) {
        final Function<String, String> indent = style != Style.UPPERCASE ? (str) -> str.indent(4) : Function.identity();
        final Function<String, String> uppercase = style != Style.INDENT ? String::toUpperCase : Function.identity();
        return input.transform(indent).transform(uppercase);
    }

    /**
     * Complete solution so that it takes a string and a Style as arguments and modifies the string based on what Style was given.
     * <p>
     * For Style.INDENT the indentation should be 4 spaces.
     * For Style.UPPERCASE the characters should be capitalised.
     * For Style.BOTH both transformations should be used.
     * <p>
     * The solution should use the String.transform() method.
     * Example:
     * ("abc", Style.INDENT) -> "    abc\n"
     * ("abc", Style.UPPERCASE) -> "ABC"
     * ("abc", Style.BOTH) -> "    ABC\n"
     */
    @Test
    void transformChallenge3() {
        final var input = "I want to style this text.";

        final var indented = solution3(input, Style.INDENT);
        final var uppercased = solution3(input, Style.UPPERCASE);
        final var both = solution3(input, Style.BOTH);

        logger.print(indented);
        logger.print(uppercased);
        logger.print(both);

        assertEquals("    I want to style this text.\n", indented);
        assertEquals("I WANT TO STYLE THIS TEXT.", uppercased);
        assertEquals("    I WANT TO STYLE THIS TEXT.\n", both);
    }

    enum Option {
        FIRST,
        SECOND,
        BOTH
    }

    BiFunction<String, Option, String> solution4(Function<String, String> first, Function<String, String> second) {
        final var safeFirst = Optional.ofNullable(first).orElse(Function.identity());
        final var safeSecond = Optional.ofNullable(second).orElse(Function.identity());
        return (input, mode) -> switch (mode) {
            case FIRST -> input.transform(safeFirst);
            case SECOND -> input.transform(safeSecond);
            case BOTH -> input.transform(safeFirst).transform(safeSecond);
        };
    }

    /**
     * Complete solution so that it takes a string and two lambda - first and second - which can be used to transform
     * the input string and return a BiFunction that can transform the input string based on the Option provided.
     * <p>
     * first and second can be null. If any of them is null, no modification should be done when they are used.
     *
     * <p>
     * For Option.FIRST only the first lambda should be used in the transformation.
     * For Option.SECOND only the second lambda should be used in the transformation.
     * For Option.BOTH both transformations should be used.
     * <p>
     * The solution should use the String.transform() method.
     * <p>
     * Example:
     * See test cases below
     */
    @Test
    void transformChallenge4() {
        final Function<String, String> indent = str -> str.indent(4);
        final Function<String, String> uppercase = String::toUpperCase;

        final var combinedFunctions = solution4(indent, uppercase);

        final var input = "I want to style this text.";
        var indented = combinedFunctions.apply(input, Option.FIRST);
        var uppercased = combinedFunctions.apply(input, Option.SECOND);
        var both = combinedFunctions.apply(input, Option.BOTH);

        assertEquals("    I want to style this text.\n", indented);
        assertEquals("I WANT TO STYLE THIS TEXT.", uppercased);
        assertEquals("    I WANT TO STYLE THIS TEXT.\n", both);

        final var missingFirst = solution4(null, uppercase);
        final var missingSecond = solution4(indent, null);
        final var missingBoth = solution4(null, null);

        indented = missingFirst.apply(input, Option.FIRST);
        uppercased = missingFirst.apply(input, Option.SECOND);
        both = missingFirst.apply(input, Option.BOTH);

        assertEquals("I want to style this text.", indented);
        assertEquals("I WANT TO STYLE THIS TEXT.", uppercased);
        assertEquals("I WANT TO STYLE THIS TEXT.", both);

        indented = missingSecond.apply(input, Option.FIRST);
        uppercased = missingSecond.apply(input, Option.SECOND);
        both = missingSecond.apply(input, Option.BOTH);

        assertEquals("    I want to style this text.\n", indented);
        assertEquals(input, uppercased);
        assertEquals("    I want to style this text.\n", both);

        indented = missingBoth.apply(input, Option.FIRST);
        uppercased = missingBoth.apply(input, Option.SECOND);
        both = missingBoth.apply(input, Option.BOTH);

        assertEquals(input, indented);
        assertEquals(input, uppercased);
        assertEquals(input, both);
    }
}
