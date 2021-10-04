package com.zeiss.gergo.kovacs.dojo;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class SealedTypes implements BasicFunctionalities {
    // Enum or abstract class? How about an Enum on steroids?
    enum ResultAsEnum {
        NONE,
        ONE_MATCH,
        MULTIPLE_MATCHES,
    }

    // leaving it package private?
    abstract class ResultAsClass {}

    class NoneAsClass extends ResultAsClass {}

    class OneMatchAsClass extends ResultAsClass {
        // private String match; ...
    }

    class MultipleMatchAsClass extends ResultAsClass {
        // private List<String> matches; ...
    }

    // Why not just a List<String> which can be empty?
    sealed class Result permits None, OneFound, MultipleFound {}

    final class None extends Result {} // as a singleton maybe?

    final class OneFound extends Result {
        private final String result;

        OneFound(String match) {
            this.result = match;
        }

        public String getResult() {
            return result;
        }
    }

    final class MultipleFound extends Result {
        private final List<String> results;

        public MultipleFound(List<String> results) {
            this.results = results;
        }

        public List<String> getResults() {
            return results;
        }
    }

    //class TwoFound extends Result { }

    sealed interface Match permits NoMatch, OneMatch, MultipleMatches {}

    final class NoMatch implements Match {}

    record OneMatch(String match) implements Match {}

    record MultipleMatches(List<String> matches) implements Match {}

    record MatchSummary(Match match, int sumOfCharacters) {}

    @Test
    void sealedClasses() {
        final List<Result> results = List.of(
                new None(),
                new OneFound("found something"),
                new MultipleFound(List.of("found one", "but found another as well"))
        );

        final Result result = randomiser.getElement(results);
        int sum = 0;

        if (result instanceof None none) {
            sum = 0;
        } else if (result instanceof OneFound one) {
            sum = one.getResult().length();
        } else if (result instanceof MultipleFound multi) {
            sum = multi.getResults()
                       .stream()
                       .map(String::length)
                       .reduce(0, Integer::sum);
        }

        logger.pairs(
                sum, "The sum of the characters"
        );

        final List<Match> matches = List.of(
                new NoMatch(),
                new OneMatch("there was exactly one match"),
                new MultipleMatches(List.of("here is one match", "but I kinda managed to snatch another one ^^ "))
        );

        final var match = randomiser.getElement(matches);
        final MatchSummary summary = switch (match) {
            case NoMatch n -> new MatchSummary(n, 0);
            case OneMatch one -> new MatchSummary(one, one.match.length());
            case MultipleMatches multiple -> new MatchSummary(
                    multiple,
                    multiple.matches
                            .stream()
                            .map(String::length)
                            .reduce(0, Integer::sum)
            );
        };

        logger.pairs(
                summary, "match summary"
        );

        final Function<Match, MatchSummary> reducer = (Match input) -> switch (input) {
            case NoMatch n -> new MatchSummary(n, 0);
            case OneMatch one -> new MatchSummary(one, one.match.length());
            case MultipleMatches multiple -> new MatchSummary(
                    multiple,
                    multiple.matches
                            .stream()
                            .map(String::length)
                            .reduce(0, Integer::sum)
            );
        };

        IntStream.rangeClosed(0, 10)
                 .mapToObj(it -> randomiser.getElement(matches))
                 .map(reducer)
                 .forEach(matchSummary -> logger.pairs(
                         matchSummary, "match summary"
                 ));
    }
}
