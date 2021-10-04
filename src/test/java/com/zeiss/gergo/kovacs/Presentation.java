package com.zeiss.gergo.kovacs;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Flow;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Presentation {
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
        System.out.println("in com.zeiss.gergo.kovacs.Presentation");
        /*
    LTS Releases:
        * 8 LTS  -> interface static and default methods, method references (static, method, constructor), Optional
        * 9      -> Quick GET Request, Process API improvements, AutoClosable, Diamond Operator with anonymous classes,
        interface with private methods, JShell Command Line Tool, Reactive Streams with java.util.concurrent.Flow, Immutable Set, Optional to Stream
        * 10     -> local variable type inference (var), Unmodifiable Collections changes, Optional*.orElseThrow()
        * 11 LTS -> String methods (isBlank, lines, strip, stripLeading, stripTrailing, repeat), File methods: readString writeString, Collection:toArray, Predicate:not
        * 12     -> String::transform, String::indent, File::mismatch, Collectors::teeing, CompactNumberFormat
        * 13     -> Text Blocks (preview)
        * 14     -> Switch Expressions,
        * 15     -> Helpful NullPointerExceptions, Text Blocks
        * 16     -> Records, Stream.toList(), Pattern Matching for instanceof, Sealed Classes, jpackage
        * 17 LTS -> Sealed Classes, switch pattern matching (preview)
 */
            // ---------------------
            //         8
            // ---------------------


//             interface static and default methods
//            interface SomeApi {
//                String regularOperation(final SomeApi otherInstance);
//
//                static String someStaticMethod() {
//                    // must have a definition here
//                    return "someStaticBehavior";
//                }
//
//                private String somePrivateMethod() { // TODO: only available from java 9, move it to that section!
//                    // also needs a definition here
//                    return "somePrivateBehavior";
//                }
//
//                default String aBasicImplementationThatCanBeOverridden() {
//                    // and this one as well
//                    return "basicImplementationThatCanBeOverridden";
//                }
//            }
//            class ClassImplementingSomeApi implements SomeApi {
//                @Override
//                public String regularOperation(final SomeApi otherInstance) {
//                    // only this needs to be overridden by default
//                    return this + otherInstance.toString();
//                }
//            }
//
//            // method references (static, method, constructor), Optional
//            var someApiObject = new ClassImplementingSomeApi();
//
//            Optional<Object> npeWillBeThrown = Optional.of(null);
//            Optional<Object> thisOneIsOK = Optional.ofNullable(null);
//            Optional<ClassImplementingSomeApi> someApiObjectOptional = Optional.ofNullable(null);
//
//            someApiObjectOptional.map(it -> it.getClass())
//                    .ifPresentOrElse(it -> System.out.println(it), () -> {
//                        // do something
//                    });
//
//            // same thing, just with references
//            someApiObjectOptional.map(ClassImplementingSomeApi::getClass)
//                    .ifPresentOrElse(System.out::println, () -> {
//                        // do something
//                    });
//
//            var firstApiObject = new ClassImplementingSomeApi();
//            var secondApiObject = new ClassImplementingSomeApi();
//
//            Optional<String> mergedResult =
//                    Optional.of(firstApiObject)
//                            .map(secondApiObject::regularOperation);
//
//            if (mergedResult.isPresent()) {
//                String stringRepresentation = mergedResult.get();
//                // do something with the result
//            } else {
//                // do something else
//            }
//
//            String resultOrDefault =
//                    Optional.of(firstApiObject)
//                            .map(secondApiObject::regularOperation)
//                            .orElse("Some default value");
//
//            String resultOrException =
//                    Optional.of(firstApiObject)
//                            .map(secondApiObject::regularOperation)
//                            .orElseThrow(() -> new RuntimeException("Oops, something went wrong..."));
//
//            String resultOrProducer =
//                    Optional.of(firstApiObject)
//                            .map(secondApiObject::regularOperation)
//                            .orElseGet(firstApiObject::toString);
//
//            // SomeApi references
//            final var result1 =
//                    Optional.of(someApiObject)
//                            .map(SomeApi::aBasicImplementationThatCanBeOverridden) // works like a charm
//                            .orElseGet(SomeApi::someStaticMethod); // also works
//            final var result2 = someApiObject.somePrivateMethod();// error, somePrivateMethod() has private access in SomeApi
//            final var result3 = SomeApi.someStaticMethod(); // error, method is private to the interface
//
//            // ---------------------
//            //        9
//            // ---------------------
//            // 9 -> Quick GET Request, Process API improvements, AutoClosable, Diamond Operator with anonymous classes,
//            // interface with private methods, JShell Command Line Tool, Reactive Streams with java.util.concurrent.Flow, Immutable Set, Optional to Stream
//
//            // Quick GET Request
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(new URI("https://localhost:8080/resources/someId12345"))
//                    .GET()
//                    .build();
//
//            HttpResponse<String> response = HttpClient.newHttpClient()
//                    .send(request, HttpResponse.BodyHandler.asString());     // valid for java 10
//            // .send(request, HttpResponse.BodyHandlers.ofString()); // changed to this after java 11
//
//            // Process API improvements
//            ProcessHandle self = ProcessHandle.current();
//            long PID = self.getPid(); // changed to self.pid() somewhere down the line
//            ProcessHandle.Info procInfo = self.info();
//
//            Optional<String[]> arguments = procInfo.arguments();
//            Optional<String> cmd =  procInfo.commandLine();
//            Optional<Instant> startTime = procInfo.startInstant();
//            Optional<Duration> cpuUsage = procInfo.totalCpuDuration();
//
//            // AutoClosable
//            class CustomAutoCloseable implements AutoCloseable {
//                @Override public void close() throws Exception {}
//            }
//            CustomAutoCloseable closeable = new CustomAutoCloseable();
//            try (var reference = new CustomAutoCloseable() ) {
//                // do some stuff with finalCloseable
//            } catch (Exception ex) { }
//            // no need for finally
//
//
//            // Diamond Operator with anonymous classes
//            // TODO: doesn't seem to work under java 17
//            FooClass<Integer> fc = new FooClass<>(1) { // anonymous inner class
//            };
//            FooClass<? extends Integer> fc0 = new FooClass<>(1) {
//                // anonymous inner class
//            };
//            FooClass<?> fc1 = new FooClass<>(1) { // anonymous inner class
//            };
//
//
//            // JShell Command Line Tool
//        /*
//                jdk-9\bin>jshell.exe
//                |  Welcome to JShell -- Version 9
//                |  For an introduction type: /help intro
//                jshell> "This is my long string. I want a part of it".substring(8,19);
//                $5 ==> "my long string"
//         */
//
//            // Immutable Set
//            Set<String> strKeySet = Set.of("key1", "key2", "key3");
//
//            // Optional to Stream
//            final List<Optional<String>> listOfOptionals = List.of(Optional.ofNullable(null), Optional.of("someValue"));
//            List<String> filteredList = listOfOptionals.stream()
//                    .flatMap(Optional::stream)
//                    .collect(Collectors.toList());
//
//            // Reactive Streams TODO: make a test for this
//            class EndSubscriber<T> implements Flow.Subscriber<T> {
//                private Flow.Subscription subscription;
//                public List<T> consumedElements = new LinkedList<>();
//                @Override
//                public void onSubscribe(Flow.Subscription subscription) {
//                    this.subscription = subscription;
//                    subscription.request(1);
//                }
//                @Override
//                public void onNext(T item) {
//                    System.out.println("Got : " + item);
//                    subscription.request(1);
//                }
//                @Override
//                public void onError(Throwable t) {
//                    t.printStackTrace();
//                }
//                @Override
//                public void onComplete() {
//                    System.out.println("Done");
//                }
//            }
//
//            // ---------------------
//            //         10
//            // ---------------------
//            // 10     -> local variable type inference (var), Unmodifiable Collections changes, Optional*.orElseThrow()
//
//            // local variable type inference (var)
//            // not a golden sword, use it with care -> read guidelines on possible use-cases
//            String explicitType = "java 9";
//            var implicitTypeInference = "java 10";
//
//            Map<Integer, String> wayTooVerboseVariableInitialisation = new HashMap<>();
//            var conciseMap = new HashMap<Integer, String>();
//
//            var n; // error: cannot use 'var' on variable without initializer
//            var emptyList = null; // error: variable initializer is 'null'
//            public var = "hello"; // error: 'var' is not allowed here
//            var p = (String s) -> s.length() > 10; // error: lambda expression needs an explicit target-type
//            var arr = { 1, 2, 3 }; // error: array initializer needs an explicit target-type
//
//            // Unmodifiable Collections changes
//            final var someIntList = List.of(10, 10, 10);
//            List<Integer> copyList = List.copyOf(someIntList);
//            copyList.add(4);
//
//            List<Integer> evenList = someIntList.stream()
//                    .filter(i -> i % 2 == 0)
//                    .collect(Collectors.toUnmodifiableList());
//            evenList.add(4);
//
//            // Optional*.orElseThrow()
//            Integer firstEven = someIntList.stream()
//                    .filter(i -> i % 2 == 0)
//                    .findFirst()
//                    .orElseThrow();
//            assert firstEven.equals(2);
//
//            // ---------------------
//            //         11
//            // ---------------------
//            // 11 LTS -> String methods (isBlank, lines, strip, stripLeading, stripTrailing, repeat), File methods: readString writeString, Collection:toArray, Predicate:not
//            // String methods (isBlank, lines, strip, stripLeading, stripTrailing, repeat)
//            String multilineString = "Some sentence \\n \\n with \\n multiple words";
//            List<String> lines = multilineString.lines()
//                    .filter(line -> !line.isBlank())
//                    .map(String::strip)
//                    .collect(Collectors.toList());
//            assertThat(lines).containsExactly("Some sentence", "with", "multiple words");
//
//            // File methods: readString writeString
//            final var tempDir = Path.of("C:\\temp");
//            Path filePath = Files.writeString(Files.createTempFile(tempDir, "demo", ".txt"), "Sample text");
//            String fileContent = Files.readString(filePath);
//            assertThat(fileContent).isEqualTo("Sample text");
//
//            // Collection:toArray
//            List sampleList = Arrays.asList("Java", "Kotlin");
//            String[] sampleArray = sampleList.toArray(String[]::new);
//            assertThat(sampleArray).containsExactly("Java", "Kotlin");
//
//            // Predicate:not
//            List<String> someSampleList = Arrays.asList("Java", "\n \n", "Kotlin", " ");
//            List withoutBlanks = sampleList.stream()
//                    .filter(Predicate.not(String::isBlank))
//                    .collect(Collectors.toList());
//            assertThat(withoutBlanks).containsExactly("Java", "Kotlin");
//
//            // Local-Variable Syntax for Lambda
//            List<String> otherSampleList = Arrays.asList("Java", "Kotlin");
//            String resultString = sampleList.stream()
//                    .map((@Nonnull var x) -> x.toUpperCase())
//                    .collect(Collectors.joining(", "));
//            assertThat(resultString).isEqualTo("JAVA, KOTLIN");
//
//            // 12     -> String::transform, String::indent, File::mismatch, Collectors::teeing, CompactNumberFormat
//            // String::transform,
//            String text = "Hello Baeldung!\nThis is Java 12 article.";
//
//            text = text.indent(4);
//            System.out.println(text);
//
//            text = text.indent(-10);
//            System.out.println(text);
//
//            // File::mismatch
//            Path filePath1 = Files.createTempFile("file1", ".txt");
//            Path filePath2 = Files.createTempFile("file2", ".txt");
//            Files.writeString(filePath1, "Java 12 Article");
//            Files.writeString(filePath2, "Java 12 Article");
//
//            long mismatch = Files.mismatch(filePath1, filePath2);
//
//            // Collectors::teeing
//            double mean = Stream.of(1, 2, 3, 4, 5)
//                    .collect(Collectors.teeing(Collectors.summingDouble(i -> i),
//                            Collectors.counting(), (sum, count) -> sum / count));
//
//            // CompactNumberFormat
//            NumberFormat likesShort =
//                    NumberFormat.getCompactNumberInstance(new Locale("en", "US"), NumberFormat.Style.SHORT);
//            likesShort.setMaximumFractionDigits(2);
//            assertEquals("2.59K", likesShort.format(2592));
//
//            NumberFormat likesLong =
//                    NumberFormat.getCompactNumberInstance(new Locale("en", "US"), NumberFormat.Style.LONG);
//            likesLong.setMaximumFractionDigits(2);
//            assertEquals("2.59 thousand", likesLong.format(2592));
//
//            // 13     -> Text Blocks (preview)
//            String TEXT_BLOCK_JSON = """
//                {
//                    "name" : "Tom Hanns",
//                    "website" : "https://tomhanks.com"
//                }
//                """;
//
//            Stream.of()
//                    .filter()
//
//            // 14     -> Switch Expressions
//            final String day = "MONDAY";
//            boolean isTodayHoliday;
//            switch (day) {
//                case "MONDAY":
//                case "TUESDAY":
//                case "WEDNESDAY":
//                case "THURSDAY":
//                case "FRIDAY":
//                    isTodayHoliday = false;
//                    break;
//                case "SATURDAY":
//                case "SUNDAY":
//                    isTodayHoliday = true;
//                    break;
//                default:
//                    throw new IllegalArgumentException("What's a " + day);
//            }
//
//            isTodayHoliday = switch (day) {
//                case "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY" -> false;
//                case "SATURDAY", "SUNDAY" -> true;
//                default -> throw new IllegalArgumentException("What's a " + day);
//            };
//            // 15     -> Helpful NullPointerExceptions
//            String emailAddress = employee.getPersonalDetails().getEmailAddress().toLowerCase();
//
//            // Exception in thread "main" java.lang.NullPointerException
//            // at com.baeldung.java14.npe.HelpfulNullPointerException.main(HelpfulNullPointerException.java:10)
//
//            // 16     -> Records, Stream.toList(), Pattern Matching for instanceof, Sealed Classes, jpackage
//            class Person {
//                private final String name;
//                private final int age;
//
//                public Person(String name, int age) {
//                    this.name = name;
//                    this.age = age;
//                }
//
//                public String getName() {
//                    return name;
//                }
//
//                public int getAge() {
//                    return age;
//                }
//            }
//
//            abstract sealed class People permits Employee, Manager { }
//
//            // 17 LTS -> Sealed Classes, switch pattern matching (preview)
    }
}
