package com.zeiss.gergo.kovacs.presentation;

import com.zeiss.gergo.kovacs.util.Logger;
import com.zeiss.gergo.kovacs.util.Randomiser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.zeiss.gergo.kovacs.presentation.FeatureGroups.MethodReferences.*;
import static com.zeiss.gergo.kovacs.util.Logger.Options;
import static java.util.function.Predicate.not;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FeatureGroups {
    private static final Logger LOGGER = new Logger();
    public static Randomiser randomised = new Randomiser();
    public static Random gen = new Random();

    @Nested
    class Basics {
        @Test
        void lambdas() {
            final Supplier<String> getString = () -> "supplying string";

            final Function<Integer, String> intToString = (input) -> input.toString();

            final Consumer<Integer> useInt = (input) -> LOGGER.print(input.toString());

            final BiFunction<Integer, Integer, Optional<Integer>> addInts = (first, second) -> {
                if (first != null && second != null)
                    return Optional.of(first + second);
                return Optional.empty();
            };

            final Predicate<Integer> isNotNull = (Integer input) -> input != null;

            useInt.accept(0);
            LOGGER.pairs(
                    getString.get(), "String supplier",
                    intToString.apply(10), "Integer to String function",
                    addInts.apply(5, 6).get(), "Integer bi-function",
                    isNotNull.test(null), "Integer is not null predicate"
            );
        }

        // A sequence of elements supporting sequential and parallel aggregate operations
        @Test
        void streams() {
            final String sequence = Stream.of(1, 2, 3, 4, 5, 6)
                                          .filter(it -> it > 2)
                                          .map(it -> Integer.toString(it).repeat(3))
                                          .collect(Collectors.joining("-"));
            LOGGER.print(sequence);
//            Collectors::toSet;
//            Collectors::toList;
//            Collectors::toMap;
//            Collectors::groupingBy;
//            Collectors::toList;
//            Collectors::teeing;
//            Collectors::summingInt;
        }
    }

    @Nested
    class MethodReferences {
        interface Behavior {
            String mergeTogether(final Behavior otherInstance);

            static String aStaticMethod() {
                // must have a definition here
                return "Standing";
            }

            private String aPrivateMethod() {
                // also needs a definition here
                return "Privacy";
            }

            default String aDefaultBehavior() {
                // and this one as well
                return "Living";
            }
        }

        static class RealBehavior implements Behavior {
            public String name;

            RealBehavior() {
                this.name = "null";
            }

            RealBehavior(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }

            public static RealBehavior mergeTwoInstances(final RealBehavior one, final RealBehavior other) {
                return one == null || other == null ?
                        new RealBehavior(Behavior.aStaticMethod()) :
                        new RealBehavior(one.name + other.name);
            }

            @Override
            public String mergeTogether(final Behavior otherInstance) {
                // only this needs to be overridden by default
                return this.name + " " + otherInstance.aDefaultBehavior();
            }
        }

        @Test
        void references() {
            final Supplier<RealBehavior> validConstructor = RealBehavior::new;              // OK
            final Supplier<Behavior> validConstructorAsInterface = RealBehavior::new;       // OK
//            final Supplier<Behavior> constructorOnInterface = Behavior::new;                // Error

            final RealBehavior someApiObject = new RealBehavior("implementing class");

            //@f:off
//            final Supplier<Behavior> objPrivate = someApiObject::somePrivateMethod;                                          // Error
//            final Supplier<Behavior> objStatic = someApiObject::aStaticMethod;                                               // Error
//            final Supplier<String> objDefault = someApiObject::aDefaultBehavior;                                             // OK
//            final Function<Behavior, String> objRegular = someApiObject::mergeTogether;                                      // OK
//            final BiFunction<RealBehavior, RealBehavior, Behavior> objStaticImpl = someApiObject::mergeTwoInstances;         // Error
//
//            final Supplier<String> implementerPrivate = RealBehavior::somePrivateMethod;                                     // Error
//            final Supplier<String> implementerStatic = RealBehavior::aStaticMethod;                                          // Error
//            final Supplier<String> implementerDefault = RealBehavior::aDefaultBehavior;                                      // Error
//            final Function<Behavior, String> implementerRegular = RealBehavior::mergeTogether;                               // Error
//            final BiFunction<RealBehavior, RealBehavior, Behavior> implementerStaticImpl = RealBehavior::mergeTwoInstances;  // OK
//
//            final Supplier<String> interfacePrivate = Behavior::somePrivateMethod;                                           // Error
//            final Supplier<String> interfaceStatic = Behavior::aStaticMethod;                                                // OK
//            final Supplier<String> interfaceDefault = Behavior::aDefaultBehavior;                                            // Error
//            final Function<Behavior, String> interfaceRegular = Behavior::mergeTogether;                                     // Error
//            final BiFunction<RealBehavior, RealBehavior, Behavior> interfaceStaticImpl = Behavior::mergeTwoInstances;        // Error
//
//            someApiObject.somePrivateMethod();                      // Error
//            someApiObject.aStaticMethod();                          // Error
//            someApiObject.aDefaultBehavior();                       // OK
//            someApiObject.mergeTogether(null);                      // OK
//            someApiObject.mergeTwoInstances(null, null);            // OK
//
//            RealBehavior.somePrivateMethod();                       // Error
//            RealBehavior.aStaticMethod();                           // Error
//            RealBehavior.aDefaultBehavior();                        // Error
//            RealBehavior.mergeTogether(null);                       // Error
//            RealBehavior.mergeTwoInstances(null, null);             // OK
//
//            Behavior.somePrivateMethod();                           // Error
//            Behavior.aStaticMethod();                               // OK
//            Behavior.aDefaultBehavior();                            // Error
//            Behavior.mergeTogether(null);                           // Error
//            Behavior.mergeTwoInstances(null, null);                 // Error
            //@f:on
        }
    }

    // A container object which may or may not contain a non-null value.
    @Nested
    class Optionals {
        @Test
        void optionalNullability() {
            final RealBehavior behavior = new RealBehavior("Moving");

//            Optional<Object> npeWillBeThrown = Optional.of(null);                                           // Error
            Optional<Object> thisOneIsOK = Optional.ofNullable(null);                                       // OK
            Optional<RealBehavior> realObj = Optional.ofNullable(gen.nextBoolean() ? behavior : null);      // OK
            Optional<RealBehavior> empty = Optional.empty();                                                // Comes in handy at times

            realObj.map(it -> it.getName())
                   .ifPresentOrElse(
                           it -> LOGGER.print("name was " + it),
                           () -> LOGGER.print("there was no name")
                   );
        }

        @Test
        void optionalMethods() {
            final RealBehavior firstApiObject = new RealBehavior("Running");
            final RealBehavior secondApiObject = new RealBehavior("Swimming");

            Optional<String> mergedResult =
                    Optional.of(firstApiObject)
                            .map(secondApiObject::mergeTogether);

            if (mergedResult.isPresent()) {
                LOGGER.pairs(mergedResult, "the merge of the two behaviors");
            } else {
                LOGGER.print("Merge was not successful");
            }

            mergedResult.isPresent();
            mergedResult.isEmpty();
            mergedResult.ifPresent(LOGGER::print);
            mergedResult.ifPresentOrElse(LOGGER::print, () -> LOGGER.print("running or else"));

            Optional.of(firstApiObject)
                    .map(secondApiObject::mergeTogether)
                    .orElse("I've got ya covered.");

            Optional.of(firstApiObject)
                    .map(secondApiObject::mergeTogether)
                    .orElseGet(firstApiObject::toString);

            Optional.of(firstApiObject)
                    .map(secondApiObject::mergeTogether)
//                    .orElseThrow();
                    .orElseThrow(() -> new RuntimeException("Oops, something went wrong..."));

            Optional.of(firstApiObject)
                    .map(RealBehavior::getName)
                    .or(() -> Optional.of(secondApiObject).map(RealBehavior::getName));
//                    .orElseThrow(); // as a last resort...

            Optional.of(firstApiObject)
                    .map(RealBehavior::getName)
                    .filter(String::isBlank)
                    .map(String::length)
                    .orElse(0);

            final List<Optional<Integer>> listOfOptionals = List.of(
                    Optional.empty(),
                    Optional.of(1),
                    Optional.of(2),
                    Optional.empty(),
                    Optional.of(3),
                    Optional.of(4),
                    Optional.of(5)
            );

            LOGGER.pairs(
                    listOfOptionals.stream()
                                   .flatMap(Optional::stream)
                                   .reduce(0, Integer::sum),
                    "sum of non-null elements"
            );

            // Optional.map() vs Optional.flatMap()
            final boolean shouldBeNull = gen.nextBoolean();
            LOGGER.pairs(
                    Optional.ofNullable(shouldBeNull ? "something useful" : null)
                            .map(String::length)
                            .orElse(0),
                    "with map",

                    Optional.ofNullable(shouldBeNull ? "something useful" : null)
                            .flatMap(it -> Optional.of(it.length()))
                            .orElse(0),
                    "with flatmap"
            );
        }

    }

    @Nested
    class MiscellaneousFeatures {

        /**
         * Style Guidelines
         * Principles
         * - Reading code is more important than writing code.
         * - Code should be clear from local reasoning.
         * - Code readability shouldn't depend on IDEs.
         * - Explicit types are a tradeoff.
         * Guidelines
         * - Choose variable names that provide useful information.
         * - Minimize the scope of local variables.
         * - Consider var when the initializer provides sufficient information to the reader.
         * - Use var to break up chained or nested expressions with local variables.
         * - Don't worry too much about "programming to the interface" with local variables.
         * - Take care when using var with diamond or generic methods.
         * - Take care when using var with literals.
         */
        @Test
        void variableTypeInference() {
            RealBehavior explicitType = new RealBehavior("Eating");
            var implicitTypeInference = "java 10";
            final var explicitFinalReference = "final";

//            explicitFinalReference = "some other final"; // Error

            Map<Integer, String> wayTooVerboseVariableInitialisation = new HashMap<Integer, String>();
            var conciseMap = new HashMap<Integer, String>();

            var typedList = new ArrayList<String>();
//            var lostType = new ArrayList<>();

//            var n; // error: cannot use 'var' on variable without initializer
//            var emptyList = null; // error: variable initializer is 'null'
//            var = "hello"; // error: 'var' is not allowed here
//            var p = (String s) -> s.length() > 10; // error: lambda expression needs an explicit target-type
//            var arr = { 1, 2, 3 }; // error: array initializer needs an explicit target-type

            // ORIGINAL
            byte flagsGOOD = 0;
            short maskGOOD = 0x7fff;
            long baseGOOD = 17;

            // DANGEROUS: all infer as int
            var flagsBAD = 0;
            var maskBAD = 0x7fff;
            var baseBAD = 17;
        }

//        class Var {
//            private var value; // Error
//
//            public Var(String value) {
//                this.value = value;
//            }
//        }

        @Test
        void streamToList() {
            final var randomNumbers =
                    Stream.of(1, 2, 3, 4)
                          .map(gen::nextInt)
//                          .collect(Collectors.toList()); // pre jdk 16
                          .toList();                   // jdk 16 onwards
            LOGGER.pairs(
                    randomNumbers, "list of numbers"
            );
        }

        @Test
        void immutableSet() {
            final var listOfNumbers = List.of(1, 1, 2, 2, 5);
//            final var listOfNumbers = List.of(1, 1, 2, 2, 5, null);           // Error
            final var setOfNumbers = Set.of(1, 2, 5);
//            final var setOfNumbers = Set.of(1, 1, 2, 2, 5, null);               // Error

            LOGGER.pairs(
                    listOfNumbers, "list",
                    setOfNumbers, "set"
            );
        }

        @Test
        void unmodifiableCollectionCopyOf() {
            final var list = List.of(1, 2, 3, 1, 2, 3);
            final var set = Set.of(1, 2, 3);
            final var map = Map.of(1, "one", 2, "two");

            final var copiedList = List.copyOf(list);
            final var copiedSet = Set.copyOf(set);
            final var copiedMap = Map.copyOf(map);

            LOGGER.pairs(
                    copiedList.stream().toList(), "unmodifiable list",
                    copiedSet.stream().collect(Collectors.toUnmodifiableSet()), "unmodifiable set",
                    copiedMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)), "unmodifiable map"
            );
        }

        //@f:off
        class GenericWrapper<T> {
            final private T value;

            public GenericWrapper(T input) {
                this.value = input;
            }
        }

        @Test
        void diamondOperatorWithAnonymousClasses() {
            final GenericWrapper<Integer> integer = new GenericWrapper<>(1) {
            };
            final GenericWrapper<String> string = new GenericWrapper<>("input") {
            };
            final GenericWrapper<?> noIdea = new GenericWrapper<>(1) {
            };
        }
        //@f:on

        @Test
        void predicateNot() {
            final Predicate<Integer> isBiggerThanTen = (number) -> number > 10;

            final var testNumbers = List.of(1, 123, 9, -12, 10, 4, 45, 11, 1000000);
            LOGGER.pairs(
                    testNumbers.stream()
                               .filter(isBiggerThanTen)
                               .collect(Collectors.toList())
                    , "values bigger than 10",

                    testNumbers.stream()
                               .filter(isBiggerThanTen.negate())
                               .collect(Collectors.toList())
                    , "values smaller than or equal to 10",

                    testNumbers.stream()
                               .filter(Predicate.not(it -> it < 10))
//                               .filter(not(isBiggerThanTen))
                               .collect(Collectors.toList())
                    , "value not smaller than 10"
            );

            final var testStrings = List.of("string", " ", " asdsa ", "");
            LOGGER.pairs(
                    testStrings.stream()
                               .filter(it -> !it.isBlank())
                               .collect(Collectors.toList()),
                    "strings that are not blank (not)",

                    testStrings.stream()
                               .filter(not(String::isBlank))
                               .collect(Collectors.toList()),
                    "strings that are not blank (negated expression)"
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

        record EnergyFlow(long megaWatt, Country from, Country to) {
        }

        record EnergyFlowForCountry(Country country, List<EnergyFlow> from, List<EnergyFlow> to) {
        }

        record EnergyFlowsAndSum(Country country, List<EnergyFlow> flows, long sum) {
        }

        final Function<EnergyFlow, String> fromToKey = (it) -> it.from.name() + it.to.name();
        final List<Country> countries = List.of(Country.values());
        final Supplier<List<EnergyFlow>> generateTestCountries = () ->
                new ArrayList<>(
                        IntStream.rangeClosed(0, 15)
                                 .mapToObj(it -> gen.nextLong(10000))
                                 .map(it -> new EnergyFlow(
                                         it, randomised.getElement(countries), randomised.getElement(countries))
                                 )
                                 .filter(it -> it.from != it.to)
                                 .collect(Collectors.toMap(fromToKey, Function.identity(), (o1, o2) -> o1))
                                 .values()
                );

        @Test
        void streamTeeing() {
            final List<EnergyFlow> testValues = generateTestCountries.get();
            LOGGER.pairs(
                    testValues, "initial list"
            ).line();

            final Country targetCountry = testValues.get(0).from;

            final var flowForCountry =
                    testValues.stream()
                              .collect(
                                      Collectors.teeing(
                                              Collectors.filtering(it -> it.from == targetCountry, Collectors.toList()),
                                              Collectors.filtering(it -> it.to == targetCountry, Collectors.toList()),
                                              (from, to) -> new EnergyFlowForCountry(targetCountry, from, to)
                                      )
                              );

            LOGGER.pairs(
                    flowForCountry.country, "Country",
                    flowForCountry.from, "from",
                    flowForCountry.to, "to"
            ).line();

            final var flowsAndSum =
                    testValues.stream()
                              .filter(it -> it.from == targetCountry || it.to == targetCountry)
                              .collect(
                                      Collectors.teeing(
                                              Collectors.toList(),
                                              Collectors.mapping((EnergyFlow flow) -> {
                                                          if (flow.to == targetCountry)
                                                              return flow.megaWatt;
                                                          return -1 * flow.megaWatt;
                                                      }, Collectors.reducing(0L, Long::sum)
                                              ),
                                              (targetFlows, targetSum) -> new EnergyFlowsAndSum(targetCountry, targetFlows, targetSum)
                                      )
                              );

            LOGGER.pairs(
                    flowsAndSum.country, "Country",
                    flowsAndSum.flows, "from",
                    flowsAndSum.sum, "to"
            );
        }
    }

    @Nested
    class ControlFlow {
        enum Cases {
            SIMPLE,
            KINDA_COMPLICATED,
            DONT_EVEN_ASK,
            NONE
        }

        private final Supplier<Cases> randomCase = () -> randomised.getElement(Cases.values());
        private final List<Cases> cases = List.of(Cases.values());
        private final Function<String, String> printer = (text) -> {
            LOGGER.print(text);
            return text;
        };

        /**
         * - Case values must be compile-time constants
         * - Can be an expression ( ; is needed at the end!)
         * - Supported types: char, byte, short, int, Character, Byte, Short, Integer, String, Enum (Sealed classes and interfaces are in preview!)
         * - yield as a switch-scoped-return
         * - no more NPE, kinda
         * - listed cases
         * - the old style is still supported of course
         */
        @Test
        void switchExpression1() {
            //@f:off
            switch (randomCase.get()) {
                case SIMPLE -> LOGGER.print("simple");
                case KINDA_COMPLICATED -> LOGGER.print("complicated");
                case DONT_EVEN_ASK -> LOGGER.print("don't even aks");
                case NONE -> LOGGER.print("none of your business");
            }

            final Optional<Boolean> enumsToInt = switch (randomised.getElementOrNull(cases)) {
                case SIMPLE -> Optional.of(true);
                case KINDA_COMPLICATED, DONT_EVEN_ASK -> Optional.of(false);
                case null -> Optional.empty();
                default -> Optional.empty();
            };
            LOGGER.pairs(
                    enumsToInt.orElseGet(() -> false), "enum to int conversion"
            );

            final var someValue = gen.nextInt(100);
            switch (someValue) {
                case 1, 2, 3, 4, 5 -> printer.apply("smaller than 5");
                case 11 -> printer.apply("between 5 and 10");
                case 10 -> printer.apply("exactly ten");
                default -> printer.apply("wow, that's very big");
            }
            //@f:on

            final var list = List.of("AC/DC", "ABC", "XYZ");
            final var potentiallyNullInput = randomised.getElementOrNull(list);
//            final String potentiallyNullInput = null;

            switch (potentiallyNullInput) {
//                case "ABC" -> LOGGER.print("ABC");
                case null -> LOGGER.print("null");
                default -> LOGGER.print("not null");
//                case null, default -> LOGGER.print("Null or whatever");
            }

            // cases must be constants
            final var reference = "this is a constant";
//            var reference = "you might lose me!";
            switch (reference) {
                case reference -> printer.apply("OK");
                default -> printer.apply("it could have changed :(");
            }
        }

        //@f:off
        @Test
        void switchStillHasNPEOrWhatQuestionMark() {
            final var list = List.of("A", "B", "C", "DEFG");
            final IntFunction<String> mightMapToNull = (input) -> gen.nextBoolean() ? null : randomised.getElement(list);
            final Consumer<String> npeSafeSwitch = (input) -> {
                switch (input) {
//                    case null                           -> LOGGER.print("NPE avoided");
                    case "A", "B" -> LOGGER.print("A or B? -> " + input);
                    case String s && s.length() > 1 -> LOGGER.print("input longer than 1 -> " + input);
//                    default                             -> LOGGER.print("Something I didn't see coming");
                    case null, default -> LOGGER.print("Something else -> " + input);
                }
            };
            //@f:on

            // test for NPE
            IntStream.rangeClosed(0, 100)
                     .mapToObj(mightMapToNull)
                     .forEach(npeSafeSwitch);
        }
    }

    @Nested
    class Records {
        record Pojo(String name, int number) {
            // you get constructor, getters, hashCode, equals, toString for "free"
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

            LOGGER.print("Hash codes for ")
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
//                Objects.requireNonNull(words); // Nope
                this(words, words.stream()
                                 .map(String::length)
                                 .reduce(0, SUM_OF_LETTERS)
                );
            }

            @Override
            public int sumOfLetters() {
                // this still works...
                LOGGER.print("shhhh!");
                return sumOfLetters + 1;
            }
        }

        @Test
        void customisedPojo() {
            final var animals = List.of("dog", "cat", "giraffe", "snake");
            final var pojo = new CustomisedPojo(animals, 100);
            final var pojoWithOptionalConstructor = new CustomisedPojo(animals);

            LOGGER.pairs(
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

    //@f:off
    @Nested
    class PatternMatching {
        interface Decoder {
            String getName();

            String decode(String encodedString);
        }

        class MockDecoder implements Decoder {
            @Override
            public String getName() {
                return "Mock Decoder";
            }

            @Override
            public String decode(final String encodedString) {
                LOGGER.print("You know, I'm something of a decoder myself (from MockDecoder -> decode)");
                return encodedString;
            }
        }

        class EscapeDecoder implements Decoder {
            @Override
            public String getName() {
                return "Escape Decoder";
            }

            @Override
            public String decode(String encodedString) {
                return encodedString.translateEscapes();
            }
        }
        //@f:on

        @Test
        void instanceOf() {
            final BiFunction<String, Decoder, String> decoderTester = (input, decoder) -> {
                if (decoder instanceof MockDecoder mockDecoder) {
                    return mockDecoder.decode(input);

                } else if (decoder instanceof EscapeDecoder escapeDecoder) {
                    return escapeDecoder.decode(input);
//                    return mockDecoder.decode(input); // NOPE
                }
                return "";
            };


            final List<Decoder> decoders = List.of(new MockDecoder(), new EscapeDecoder());
            final Decoder decoderUnderTest = randomised.getElement(decoders);
            LOGGER.pairs(decoderUnderTest.getClass().getName(), "Chosen decoder");

            final var escapesToTranslate = " Translate me! \\t I dare you, I double dare you \\n mother \"translater\"!!!!\\b\\b\\b";
            final var regular = "Some regular sentence here";
            final String emptyString = " ";
            final var testValues = List.of(escapesToTranslate, regular, emptyString);

            final var decodedStrings = testValues.stream()
                                                 .map(it -> decoderTester.apply(it, decoderUnderTest))
                                                 .toList();
            LOGGER.pairs(decodedStrings, "But are they really decoded?");
        }
    }

    @Nested
    class SealedTypes {
        // Enum or abstract class? How about an Enum on steroids?
        enum ResultAsEnum {
            NONE,
            ONE_MATCH,
            MULTIPLE_MATCHES,
        }

        // leaving it package private?
        //@f:off
        abstract class ResultAsClass {
        }

        class NoneAsClass extends ResultAsClass {
        }

        class OneMatchAsClass extends ResultAsClass {
            // private String match; ...
        }

        class MultipleMatchAsClass extends ResultAsClass {
            // private List<String> matches; ...
        }

        // Why not just a List<String> which can be empty?
        sealed class Result permits None, OneFound, MultipleFound {
        }

        final class None extends Result {
        } // as a singleton maybe?

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
//        class TwoFound extends Result { }

        sealed interface Match permits NoMatch, OneMatch, MultipleMatches {
        }

        final class NoMatch implements Match {
        }

        record OneMatch(String match) implements Match {
        }

        record MultipleMatches(List<String> matches) implements Match {
        }

        record MatchSummary(Match match, int sumOfCharacters) {
        }

        // @f:on
        @Test
        void sealedClasses() {
            final List<Result> results = List.of(
                    new None(),
                    new OneFound("found something"),
                    new MultipleFound(List.of("found one", "but found another"))
            );

            final Result result = randomised.getElement(results);
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

            final List<Match> matches = List.of(
                    new NoMatch(),
                    new OneMatch("there was exactly one match"),
                    new MultipleMatches(List.of("here is one match", "but I kinda managed to snatch another one ^^ "))
            );

            //@f:off
            final Match match = randomised.getElement(matches);
//            final Matches match = new MultipleMatches(List.of("here is one match", "but I kinda managed to snatch another one ^^ "));
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
            //@f:on

            LOGGER.pairs(
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
                     .mapToObj(it -> randomised.getElement(matches))
                     .map(reducer)
                     .forEach(matchSummary -> LOGGER.pairs(
                             matchSummary, "match summary"
                     ));
        }
    }

    @Nested
    class StringsGroup {
        @Test
        public void textBlock() {
            final String block = """
                    """;

//        final String mustHaveANewLine = """ first line
//                """;

            final String html = """
                    <html>      
                        <body>
                            <div>example text</div>
                        </body>
                    </html""";

            final String fragmentShaderOld = "version 330 core\n" +
                                             "                out vec4 FragColor;\n" +
                                             "                 \n" +
                                             "                in vec4 vertexColor; // the input variable from the vertex shader (same name and same type)\n" +
                                             "                \n" +
                                             "                void main()\n" +
                                             "                {\n" +
                                             "                    FragColor = vertexColor;\n" +
                                             "                }";

            final String fragmentShaderNew = """                
                    #version 330 core
                    out vec4 FragColor;
                     
                    in vec4 vertexColor; // the input variable from the vertex shader (same name and same type)
                                    
                    void main()
                    {
                        FragColor = vertexColor;
                    }""";

            final String explicitIndentation = """
                        easier to read
                    """;
            LOGGER.pairs(explicitIndentation.equals("    easier to read"), "explicit indent: are they the same?");

            LOGGER.print("""
                    “To be, or not to be, that is the question”
                        - William Shakespeare, "" //OK, but how about 3 double quotes?
                        \""" //OK
                    """
            );

            final String noCarriageReturnForYou = """
                    using windows\r
                    is a sin""";
            LOGGER.pairs(
                    noCarriageReturnForYou.equals("using windows\r\nis a sin"), "only \\n is added?"
            );
            LOGGER.pairs(
                    """
                            escaping long text \
                            has never been \
                            easier
                            """, "wrapped lines"
            );

            LOGGER.pairs(
                    """   
                            my personal space             
                            is ignored :( 
                            """, "ignored whitespaces",
                    """
                            my personal space                    \s
                            shall not be ignored anymore!              \s             
                            """, "whitespaces incorporated"
            );

            LOGGER.print("""
                    easy-peasy %s %s""".formatted("lemon", "squeezy")
            );
        }

        @SuppressWarnings("ResultOfMethodCallIgnored")
        @Test
        public void isBlank() {
            final String validString = "test string";
            final var emptyString = "";
            final String oneWhiteSpace = " ";
            final String quiteALotOfWhiteSpace = "                              ";
            final String nullString = null;

            LOGGER.pairs(
                    validString.isBlank(), "Is a valid string blank?",
                    emptyString.isBlank(), "It's empty, but is it blank as well?",
                    oneWhiteSpace.isBlank(), "Does a whitespace considered blank?",
                    quiteALotOfWhiteSpace.isBlank(), "How about a lot of blank space?",
                    Assertions.assertThrows(NullPointerException.class, () -> nullString.isBlank()), "Null-safe, maybe?"
            );
        }

        @Test
        void lines() {
            final String stringContainingLines = """
                    one line
                    two line
                    three line
                    four
                    who wants even more lines?
                    cus I can do more!""";
            LOGGER.print(stringContainingLines);
            stringContainingLines.lines()
                                 .collect(Collectors.toList())
                                 .forEach(it -> LOGGER.print(new StringBuilder(it).reverse().toString()));
        }

        @Test
        public void transform() {
            final String string = " a majestic string instance   ";
            final Integer length = string.transform(String::trim)
                                         .transform(String::length);
            LOGGER.pairs(length, "length");

            string.transform(String::lines)
                  .forEach(LOGGER::print);

            final Supplier<String> potentiallyNullString = () -> gen.nextBoolean() ? "some basic string " : null;
            final String testString = potentiallyNullString.get();

            final List<String> words = Optional.ofNullable(testString)
                                               .map(str -> str.transform(it -> it.split(" ")))
                                               .map(Arrays::asList)
                                               .orElse(List.of("default", "case"));
            LOGGER.pairs(words, "words split apart");
        }

        @Test
        public void strip() {
            final String paddedString = "  Let's have some padding, shall we?   ";
            final String blankString = "      ";
            final String oneWhiteSpace = " ";

            LOGGER.pairs(
                    paddedString.strip(), "padded",
                    blankString.strip(), "blank",
                    oneWhiteSpace.strip(), "one white space"
            );

            LOGGER.pairs(
                    paddedString.stripLeading(), "padded leading",
                    paddedString.stripTrailing(), "padded trailing",
                    blankString.stripLeading(), "blank leading",
                    blankString.stripTrailing(), "blank trailing",
                    oneWhiteSpace.stripLeading(), "one white space leading",
                    oneWhiteSpace.stripTrailing(), "one white space trailing"
            );
        }

        @Test
        void indent() {
            final String unindentedString = """
                    No indent for you!
                    Or you!
                        Or for you!""";

            final String asIndented = unindentedString.indent(4 * 2);
            LOGGER.print(
                    asIndented
            );
        }

        @Test
        void repeat() {
            final String repeatingI = "I look into your Is and I see I";
            LOGGER.print(
                    repeatingI.repeat(10)
            );
        }

        @Test
        void translateEscapes() {
            final String stringToTranslate = " Translate me! \\t I dare you, I double dare you \\n mother \"translater\"!!!!\\b\\b\\b";
            LOGGER.print(
                    stringToTranslate,
                    stringToTranslate.translateEscapes()
            );
        }
    }

    @Nested
    class FilesGroup {
        private final Random rnd = new Random();
        private final Path basePath = Path.of("src/test/resources/");
        private final Path oneTestFile = basePath.resolve("one_test_file.txt");
        private final Path anotherTestFile = basePath.resolve("another_test_file.txt");

        private final Function<Integer, String> randomNumber = (Integer length) ->
                IntStream.rangeClosed(0, length - 1)
                         .map(it -> rnd.nextInt(10))
                         .mapToObj(Integer::toString)
                         .collect(Collectors.joining());

        private final Supplier<String> randomFileName = () -> "test_file_" + randomNumber.apply(10);
        private final Supplier<Path> randomFilePath = () -> basePath.resolve(randomFileName.get());
        private final Consumer<Path> silentDeleteFile = (Path path) -> {
            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        @Test
        void filesMethods() {
            final var content = "Some sort of string content to save";

            final var firstFileName = randomFilePath.get();
            final var secondFileName = randomFilePath.get();
            try {
                Files.writeString(firstFileName, content + " 123", StandardOpenOption.CREATE_NEW);
                Files.writeString(secondFileName, content + " 456", StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);

                LOGGER.pairs(
                        Files.readString(firstFileName), "reading file 1",
                        Files.readString(secondFileName), "reading file 2"
                );

                LOGGER.pairs(
                        Options.SPACED, Files.readString(oneTestFile), "reading test file"
                ).line();

                final var noMismatch = Files.mismatch(oneTestFile, oneTestFile);
                final var mismatchAtTheEnd = Files.mismatch(oneTestFile, anotherTestFile);

                LOGGER.pairs(
                        noMismatch, "no mismatch",
                        mismatchAtTheEnd, "some mismatch at the end of the file"
                );
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                Stream.of(firstFileName, secondFileName)
                      .filter(Files::exists)
                      .forEach(silentDeleteFile);
            }
        }
    }
}


