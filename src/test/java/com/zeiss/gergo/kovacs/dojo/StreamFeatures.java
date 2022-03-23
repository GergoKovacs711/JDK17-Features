package com.zeiss.gergo.kovacs.dojo;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Collections.emptyList;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.*;

public class StreamFeatures implements BasicFunctionalities {

    @Test
    void teeingSimple() {
        final List<Integer> items = IntStream.rangeClosed(0, 100)
                                             .mapToObj(it -> generator.randomRealNumber(1000))
                                             .toList();
        record MinMax(int min, int max) {}
        final var minMax = items.stream()
                                .collect(teeing(
                                        minBy(Comparator.comparing(it -> it)),
                                        maxBy(Comparator.comparing(it -> it)),
                                        (min, max) -> new MinMax(min.orElse(0), max.orElse(0))
                                ));

        logger.pairs(
                items, "Test list",
                minMax, "Min max values"
        );

        record CountWithGroupedValues(
                long sum,
                List<Integer> negativeValues,
                List<Integer> positiveValues,
                int numberOfZeros
        ) {}
        final Function<Integer, Integer> grouper = (number) -> number == 0 ? 0 : number > 0 ? 1 : -1;
        final var countWithGroups = items.stream()
                                         .collect(teeing(
                                                 reducing(0, Integer::sum),
                                                 groupingBy(grouper),
                                                 (sum, groups) -> new CountWithGroupedValues(
                                                         sum,
                                                         Optional.ofNullable(groups.get(-1)).orElse(emptyList()),
                                                         Optional.ofNullable(groups.get(1)).orElse(emptyList()),
                                                         Optional.ofNullable(groups.get(0)).map(List::size).orElse(0)
                                                 )
                                         ));

        logger.pairs(
                countWithGroups.toString(), "Count, positive and negative numbers"
        );
    }

    enum Country {
        GERMANY,
        POLAND,
        CZECH_REPUBLIC,
        SWITZERLAND,
        AUSTRIA,
        HUNGARY
    }

    final Function<EnergyFlow, String> fromToKey = (it) -> it.from.name() + it.to.name();
    final List<Country> countries = List.of(Country.values());
    final Supplier<List<EnergyFlow>> generateTestCountries = () ->
            new ArrayList<>(
                    IntStream.rangeClosed(0, 15)
                             .mapToObj(it -> generator.nextLong(10000))
                             .map(megaWatt -> new EnergyFlow(
                                             megaWatt,
                                             randomiser.getElement(countries),
                                             randomiser.getElement(countries)
                                     )
                             )
                             .filter(it -> it.from != it.to)
                             .collect(toMap(fromToKey, Function.identity(), (o1, o2) -> o1))
                             .values()
            );

    record EnergyFlow(long megaWatt, Country from, Country to) {}

    @Test
    void teeingComplex() {
        final List<EnergyFlow> testValues = generateTestCountries.get();
        logger.pairs(
                testValues, "initial list"
        ).line();

        final Country targetCountry = testValues.get(0).from;

        record EnergyFlowForCountry(Country country, List<EnergyFlow> from, List<EnergyFlow> to) {}
        final var flowForCountry =
                testValues.stream()
                          .collect(
                                  teeing(
                                          filtering(it -> it.from == targetCountry, toList()),
                                          filtering(it -> it.to == targetCountry, toList()),
                                          (from, to) -> new EnergyFlowForCountry(targetCountry, from, to)
                                  )
                          );

        logger.pairs(
                flowForCountry.country, "Country",
                flowForCountry.from, "from",
                flowForCountry.to, "to"
        ).line();

        record EnergyFlowsAndSum(Country country, List<EnergyFlow> flows, long sum) {}
        final var flowsAndSum =
                testValues.stream()
                          .filter(it -> it.from == targetCountry || it.to == targetCountry)
                          .collect(
                                  teeing(
                                          toList(),
                                          mapping(
                                                  (EnergyFlow flow) -> flow.to == targetCountry ? flow.megaWatt : -1 * flow.megaWatt,
                                                  reducing(0L, Long::sum)
                                          ),
                                          (targetFlows, targetSum) -> new EnergyFlowsAndSum(targetCountry, targetFlows, targetSum)
                                  )
                          );

        logger.pairs(
                flowsAndSum.country, "Country",
                flowsAndSum.flows, "flows",
                flowsAndSum.sum, "sum"
        );
    }

    @Test
    void predicateNot() {
        final Predicate<Integer> isBiggerThanTen = (number) -> number > 10;

        final var testNumbers = List.of(1, 123, 9, -12, 10, 4, 45, 11, 1000000);
        logger.pairs(
                testNumbers.stream()
                           .filter(isBiggerThanTen)
                           .collect(toList())
                , "values bigger than 10",

                testNumbers.stream()
                           .filter(isBiggerThanTen.negate())
                           .collect(toList())
                , "values smaller than or equal to 10",

                testNumbers.stream()
                           .filter(Predicate.not(it -> it < 10))
//                               .filter(not(isBiggerThanTen))
                           .collect(toList())
                , "value not smaller than 10"
        );

        final var testStrings = List.of("string", " ", " asdsa ", "");
        logger.pairs(
                testStrings.stream()
                           .filter(it -> !it.isBlank())
                           .collect(toList()),
                "strings that are not blank (not)",

                testStrings.stream()
                           .filter(not(String::isBlank))
                           .collect(toList()),
                "strings that are not blank (negated expression)"
        );
    }
}
