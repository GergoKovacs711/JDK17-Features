package com.zeiss.gergo.kovacs.dojo.challenges;

import com.zeiss.gergo.kovacs.dojo.BasicFunctionalities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class StreamChallenges implements BasicFunctionalities {

    enum Grade {
        EXCELLENT(5),
        GOOD(4),
        SATISFACTORY(3),
        PASSING(2),
        FAILURE(1);

        private final int value;

        Grade(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    enum Subject {
        MATH,
        GRAMMAR,
        FOREIGN_LANGUAGE,
        HISTORY,
        PHYSICAL_EXERCISE
    }

    enum Sex {
        FEMALE,
        MALE,
        OTHER
    }

    record Student(String firstname, String lastName, Sex sex) {}

    record GradeEntry(Student student, Grade grade, Subject subject, int schoolYear) {}

    record NeedSupportOrCompliment(Collection<GradeEntry> support, Collection<GradeEntry> compliment) {}

    /**
     * Write a solution that helps you decide which students need support with a certain subject and which need
     * praise because of their excellent performance.
     * <p>
     * -> NeedSupportOrCompliment.support should contain all grades that are Grade.PASSING or Grade.FAILURE
     * -> NeedSupportOrCompliment.compliment should contain all grades that are Grade.EXCELLENT
     * <p>
     * The solution should be done with the Collectors.teeing() method
     */
    private NeedSupportOrCompliment solution1(final List<GradeEntry> gradeEntries) {
        return gradeEntries.stream()
                           .collect(
                                   teeing(
                                           filtering(entry -> entry.grade.getValue() < 3, toList()),
                                           filtering(entry -> entry.grade.getValue() == 5, toList()),
                                           NeedSupportOrCompliment::new
                                   )
                           );
    }

    @Test
    void challenge1() {
        final NeedSupportOrCompliment result = solution1(testData);

        Assertions.assertEquals(14, result.compliment.size());
        Assertions.assertEquals(8, result.support.size());

        final List<GradeEntry> expectedCompliments = Stream.of(0, 12, 14, 19, 21, 29, 32, 37, 48, 51, 55, 59, 68, 73)
                                                           .map(testData::get)
                                                           .toList();
        final List<GradeEntry> expectedSupports = Stream.of(15, 39, 40, 42, 57, 60, 63, 72)
                                                        .map(testData::get)
                                                        .toList();
        Assertions.assertEquals(expectedCompliments, result.compliment);
        Assertions.assertEquals(expectedSupports, result.support);
    }

    private NeedSupportOrCompliment solution11(final List<GradeEntry> gradeEntries) {
        return gradeEntries.stream().collect(teeing(
                filtering(ge -> ge.grade.equals(Grade.PASSING) || ge.grade.equals(Grade.FAILURE), toList()),
                filtering(ge -> ge.grade.equals(Grade.EXCELLENT), toList()),
                (support, compliment) -> new NeedSupportOrCompliment(support, compliment)));
    }

    private NeedSupportOrCompliment solution12(final List<GradeEntry> gradeEntries) {
        return gradeEntries.stream()
                           .collect(
                                   teeing(
                                           filtering(grade -> grade.grade.equals(Grade.PASSING) || grade.grade.equals(Grade.FAILURE), toList()),
                                           filtering(grade -> grade.grade.equals(Grade.EXCELLENT), toList()),
                                           NeedSupportOrCompliment::new
                                   )
                           );
    }

    record GroupByGradesAndCount(Map<Integer, List<GradeEntry>> byGrades, long sum) {}

    /**
     * Write a solution that groups grade entries together based on Grade.
     * <p>
     * - GroupByGradesAndCount.byGrades should contain a map in which theGradeEntrys are grouped by grades
     * - GroupByGradesAndCount.sum should be the sum of all grades that are either Grade.EXCELLENT or Grade.FAILURE
     * <p>
     * The solution should be done with the Collectors.teeing() method
     */
    private GroupByGradesAndCount solution2(final List<GradeEntry> gradeEntries) {
        return gradeEntries.stream()
                           .collect(
                                   teeing(
                                           groupingBy(it -> it.grade.getValue()),
                                           filtering(it -> it.grade.getValue() == 1 || it.grade.getValue() == 5, counting()),
                                           GroupByGradesAndCount::new
                                   )
                           );
    }

    @Test
    void challenge2() {
        final GroupByGradesAndCount result = solution2(testData);

        Assertions.assertEquals(19, result.sum);
        Assertions.assertEquals(5, result.byGrades.size());

        final List<GradeEntry> failures = result.byGrades.get(1);
        final List<GradeEntry> passings = result.byGrades.get(2);
        final List<GradeEntry> satisfactories = result.byGrades.get(3);
        final List<GradeEntry> goods = result.byGrades.get(4);
        final List<GradeEntry> excellents = result.byGrades.get(5);

        Assertions.assertEquals(5, failures.size());
        Assertions.assertEquals(3, passings.size());
        Assertions.assertEquals(17, satisfactories.size());
        Assertions.assertEquals(41, goods.size());
        Assertions.assertEquals(14, excellents.size());
        Assertions.assertEquals(80, failures.size() + passings.size() + satisfactories.size() + goods.size() + excellents.size());

        final List<GradeEntry> expectedFailures = Stream.of(40, 57, 60, 63, 72)
                                                        .map(testData::get)
                                                        .toList();
        final List<GradeEntry> expectedPassings = Stream.of(15, 39, 42)
                                                        .map(testData::get)
                                                        .toList();
        final List<GradeEntry> expectedSatisfactories = Stream.of(4, 8, 10, 24, 31, 33, 36, 41, 47, 49, 52, 62, 66, 69, 70, 77, 79)
                                                              .map(testData::get)
                                                              .toList();
        final List<GradeEntry> expectedGoods = Stream.of(1, 2, 3, 5, 6, 7, 9, 11, 13, 16, 17, 18, 20, 22, 23, 25, 26, 27, 28, 30, 34, 35, 38, 43, 44, 45, 46, 50, 53, 54, 56, 58, 61, 64, 65, 67, 71, 74, 75, 76, 78)
                                                     .map(testData::get)
                                                     .toList();
        final List<GradeEntry> expectedExcellents = Stream.of(0, 12, 14, 19, 21, 29, 32, 37, 48, 51, 55, 59, 68, 73)
                                                          .map(testData::get)
                                                          .toList();
        Assertions.assertEquals(expectedFailures, failures);
        Assertions.assertEquals(expectedPassings, passings);
        Assertions.assertEquals(expectedSatisfactories, satisfactories);
        Assertions.assertEquals(expectedGoods, goods);
        Assertions.assertEquals(expectedExcellents, excellents);
    }

    record StudentWithAverage(Student student, double average) {}

    /**
     * Write a solution that finds the best students based on certain criteria.
     * <p>
     * -> The best student of each school year should automatically be among the best students.
     * -> Every student with an average of 4.0 or higher is considered to be one of the best students.
     * <p>
     * Hints:
     * -> All students have a unique firstname + lastname combination within the test data
     * -> The list can only contain students once. Even if a student is best in its school year and have a higher
     * average than 4.0 should only be included in the resulting list once.
     * -> Java have no distinctBy field method for filtering, you can use the provided distinctByKey method, if you need it
     * <p>
     * Example usage:
     * students.stream()
     * .filter(distinctByKey(Student::firstname))
     * .toList();
     * <p>
     * <p>
     * The solution should be done with the Collectors.teeing() method
     */
    private List<StudentWithAverage> solution3(final List<GradeEntry> gradeEntries) {
        return gradeEntries.stream()
                           .collect(
                                   teeing(
                                           groupingBy(entry -> entry.schoolYear, groupingBy(entry -> entry.student.firstname + entry.student.lastName)),
                                           groupingBy(entry -> entry.student.firstname + entry.student.lastName),
                                           (studentsBySchoolYear, groupedByStudents) -> {
                                               final List<StudentWithAverage> bestStudentsForASchoolYear =
                                                       studentsBySchoolYear.values()
                                                                           .stream()
                                                                           .map(year -> year.values()
                                                                                            .stream()
                                                                                            .map(studentGrades -> new StudentWithAverage(
                                                                                                            studentGrades.get(0).student,
                                                                                                            studentGrades.stream()
                                                                                                                         .mapToInt(entry -> entry.grade.getValue())
                                                                                                                         .average()
                                                                                                                         .orElse(0)
                                                                                                    )
                                                                                            ).max(Comparator.comparing(StudentWithAverage::average))
                                                                                            .orElse(null)
                                                                           ).filter(Objects::nonNull)
                                                                           .toList();
                                               final List<StudentWithAverage> studentsAbove40 =
                                                       groupedByStudents.values()
                                                                        .stream()
                                                                        .map(studentGrades -> new StudentWithAverage(
                                                                                        studentGrades.get(0).student,
                                                                                        studentGrades.stream()
                                                                                                     .mapToInt(entry -> entry.grade.getValue())
                                                                                                     .average()
                                                                                                     .orElse(0)
                                                                                )
                                                                        ).sorted(Comparator.comparing(StudentWithAverage::average))
                                                                        .filter(student -> student.average >= 4.0)
                                                                        .toList();
                                               return Stream.of(studentsAbove40, bestStudentsForASchoolYear)
                                                            .flatMap(Collection::stream)
                                                            .filter(
                                                                    distinctByKey(it -> it.student.firstname + it.student.lastName)
                                                            )
                                                            .toList();
                                           }
                                   )
                           );
    }
    @Test
    void challenge3() {
        final var bestStudents = solution3(testData);

        final List<StudentWithAverage> expectedBestStudents = List.of(
                new StudentWithAverage(new Student("Kevin", "Clark", Sex.MALE), 4.0),
                new StudentWithAverage(new Student("Karen", "Lopez", Sex.FEMALE), 4.0),
                new StudentWithAverage(new Student("Daniel", "Walker", Sex.MALE), 4.0),
                new StudentWithAverage(new Student("Sandra", "Thomas", Sex.FEMALE), 4.2),
                new StudentWithAverage(new Student("Joseph", "Harris", Sex.MALE), 4.2),
                new StudentWithAverage(new Student("Michael", "Sanchez", Sex.MALE), 3.8)
        );

        Assertions.assertEquals(6, bestStudents.size());
        Assertions.assertEquals(expectedBestStudents, bestStudents);
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private List<GradeEntry> generateRandomEntries() {
        final var grades = Grade.values();
        final var subjects = Subject.values();
        final var sexes = (Sex.values());
        final var schoolYears = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        return IntStream.rangeClosed(0, 100)
                        .mapToObj(it -> {
                            final var sex = randomiser.getElement(sexes);
                            final var firstName = switch (sex) {
                                case FEMALE -> randomiser.getElement(femaleNames);
                                case MALE -> randomiser.getElement(maleNames);
                                case OTHER -> generator.nextBoolean()
                                        ? randomiser.getElement(femaleNames)
                                        : randomiser.getElement(maleNames);
                            };
                            return new GradeEntry(
                                    new Student(firstName, randomiser.getElement(lastNames), sex),
                                    randomiser.getElement(grades),
                                    randomiser.getElement(subjects),
                                    randomiser.getElement(schoolYears)
                            );
                        }).toList();
    }

    final List<GradeEntry> testData = List.of(
            new GradeEntry(new Student("Karen", "Lopez", Sex.FEMALE), Grade.EXCELLENT, Subject.GRAMMAR, 1),
            new GradeEntry(new Student("Karen", "Lopez", Sex.FEMALE), Grade.GOOD, Subject.FOREIGN_LANGUAGE, 1),
            new GradeEntry(new Student("Karen", "Lopez", Sex.FEMALE), Grade.GOOD, Subject.HISTORY, 1),
            new GradeEntry(new Student("Karen", "Lopez", Sex.FEMALE), Grade.GOOD, Subject.MATH, 1),
            new GradeEntry(new Student("Karen", "Lopez", Sex.FEMALE), Grade.SATISFACTORY, Subject.PHYSICAL_EXERCISE, 1),

            new GradeEntry(new Student("James", "Allen", Sex.MALE), Grade.GOOD, Subject.GRAMMAR, 1),
            new GradeEntry(new Student("James", "Allen", Sex.MALE), Grade.GOOD, Subject.FOREIGN_LANGUAGE, 1),
            new GradeEntry(new Student("James", "Allen", Sex.MALE), Grade.GOOD, Subject.HISTORY, 1),
            new GradeEntry(new Student("James", "Allen", Sex.MALE), Grade.SATISFACTORY, Subject.MATH, 1),
            new GradeEntry(new Student("James", "Allen", Sex.MALE), Grade.GOOD, Subject.PHYSICAL_EXERCISE, 1),

            new GradeEntry(new Student("Joseph", "Harris", Sex.MALE), Grade.SATISFACTORY, Subject.GRAMMAR, 1),
            new GradeEntry(new Student("Joseph", "Harris", Sex.MALE), Grade.GOOD, Subject.FOREIGN_LANGUAGE, 1),
            new GradeEntry(new Student("Joseph", "Harris", Sex.MALE), Grade.EXCELLENT, Subject.HISTORY, 1),
            new GradeEntry(new Student("Joseph", "Harris", Sex.MALE), Grade.GOOD, Subject.MATH, 1),
            new GradeEntry(new Student("Joseph", "Harris", Sex.MALE), Grade.EXCELLENT, Subject.PHYSICAL_EXERCISE, 1),

            new GradeEntry(new Student("Patricia", "Martin", Sex.FEMALE), Grade.PASSING, Subject.GRAMMAR, 1),
            new GradeEntry(new Student("Patricia", "Martin", Sex.FEMALE), Grade.GOOD, Subject.FOREIGN_LANGUAGE, 1),
            new GradeEntry(new Student("Patricia", "Martin", Sex.FEMALE), Grade.GOOD, Subject.HISTORY, 1),
            new GradeEntry(new Student("Patricia", "Martin", Sex.FEMALE), Grade.GOOD, Subject.MATH, 1),
            new GradeEntry(new Student("Patricia", "Martin", Sex.FEMALE), Grade.EXCELLENT, Subject.PHYSICAL_EXERCISE, 1),

            new GradeEntry(new Student("Kevin", "Clark", Sex.MALE), Grade.GOOD, Subject.GRAMMAR, 2),
            new GradeEntry(new Student("Kevin", "Clark", Sex.MALE), Grade.EXCELLENT, Subject.FOREIGN_LANGUAGE, 2),
            new GradeEntry(new Student("Kevin", "Clark", Sex.MALE), Grade.GOOD, Subject.HISTORY, 2),
            new GradeEntry(new Student("Kevin", "Clark", Sex.MALE), Grade.GOOD, Subject.MATH, 2),
            new GradeEntry(new Student("Kevin", "Clark", Sex.MALE), Grade.SATISFACTORY, Subject.PHYSICAL_EXERCISE, 2),

            new GradeEntry(new Student("Sandra", "Thomas", Sex.FEMALE), Grade.GOOD, Subject.GRAMMAR, 2),
            new GradeEntry(new Student("Sandra", "Thomas", Sex.FEMALE), Grade.GOOD, Subject.FOREIGN_LANGUAGE, 2),
            new GradeEntry(new Student("Sandra", "Thomas", Sex.FEMALE), Grade.GOOD, Subject.HISTORY, 2),
            new GradeEntry(new Student("Sandra", "Thomas", Sex.FEMALE), Grade.GOOD, Subject.MATH, 2),
            new GradeEntry(new Student("Sandra", "Thomas", Sex.FEMALE), Grade.EXCELLENT, Subject.PHYSICAL_EXERCISE, 2),

            new GradeEntry(new Student("Kenneth", "Clark", Sex.MALE), Grade.GOOD, Subject.GRAMMAR, 2),
            new GradeEntry(new Student("Kenneth", "Clark", Sex.MALE), Grade.SATISFACTORY, Subject.FOREIGN_LANGUAGE, 2),
            new GradeEntry(new Student("Kenneth", "Clark", Sex.MALE), Grade.EXCELLENT, Subject.HISTORY, 2),
            new GradeEntry(new Student("Kenneth", "Clark", Sex.MALE), Grade.SATISFACTORY, Subject.MATH, 2),
            new GradeEntry(new Student("Kenneth", "Clark", Sex.MALE), Grade.GOOD, Subject.PHYSICAL_EXERCISE, 2),

            new GradeEntry(new Student("Linda", "Wilson", Sex.FEMALE), Grade.GOOD, Subject.GRAMMAR, 2),
            new GradeEntry(new Student("Linda", "Wilson", Sex.FEMALE), Grade.SATISFACTORY, Subject.FOREIGN_LANGUAGE, 2),
            new GradeEntry(new Student("Linda", "Wilson", Sex.FEMALE), Grade.EXCELLENT, Subject.HISTORY, 2),
            new GradeEntry(new Student("Linda", "Wilson", Sex.FEMALE), Grade.GOOD, Subject.MATH, 2),
            new GradeEntry(new Student("Linda", "Wilson", Sex.FEMALE), Grade.PASSING, Subject.PHYSICAL_EXERCISE, 2),

            new GradeEntry(new Student("Sarah", "Williams", Sex.FEMALE), Grade.FAILURE, Subject.GRAMMAR, 3),
            new GradeEntry(new Student("Sarah", "Williams", Sex.FEMALE), Grade.SATISFACTORY, Subject.FOREIGN_LANGUAGE, 3),
            new GradeEntry(new Student("Sarah", "Williams", Sex.FEMALE), Grade.PASSING, Subject.HISTORY, 3),
            new GradeEntry(new Student("Sarah", "Williams", Sex.FEMALE), Grade.GOOD, Subject.MATH, 3),
            new GradeEntry(new Student("Sarah", "Williams", Sex.FEMALE), Grade.GOOD, Subject.PHYSICAL_EXERCISE, 3),

            new GradeEntry(new Student("Margaret", "Martinez", Sex.FEMALE), Grade.GOOD, Subject.GRAMMAR, 3),
            new GradeEntry(new Student("Margaret", "Martinez", Sex.FEMALE), Grade.GOOD, Subject.FOREIGN_LANGUAGE, 3),
            new GradeEntry(new Student("Margaret", "Martinez", Sex.FEMALE), Grade.SATISFACTORY, Subject.HISTORY, 3),
            new GradeEntry(new Student("Margaret", "Martinez", Sex.FEMALE), Grade.EXCELLENT, Subject.MATH, 3),
            new GradeEntry(new Student("Margaret", "Martinez", Sex.FEMALE), Grade.SATISFACTORY, Subject.PHYSICAL_EXERCISE, 3),

            new GradeEntry(new Student("Daniel", "Walker", Sex.MALE), Grade.GOOD, Subject.GRAMMAR, 3),
            new GradeEntry(new Student("Daniel", "Walker", Sex.MALE), Grade.EXCELLENT, Subject.FOREIGN_LANGUAGE, 3),
            new GradeEntry(new Student("Daniel", "Walker", Sex.MALE), Grade.SATISFACTORY, Subject.HISTORY, 3),
            new GradeEntry(new Student("Daniel", "Walker", Sex.MALE), Grade.GOOD, Subject.MATH, 3),
            new GradeEntry(new Student("Daniel", "Walker", Sex.MALE), Grade.GOOD, Subject.PHYSICAL_EXERCISE, 3),

            new GradeEntry(new Student("Christopher", "Williams", Sex.MALE), Grade.EXCELLENT, Subject.GRAMMAR, 3),
            new GradeEntry(new Student("Christopher", "Williams", Sex.MALE), Grade.GOOD, Subject.FOREIGN_LANGUAGE, 3),
            new GradeEntry(new Student("Christopher", "Williams", Sex.MALE), Grade.FAILURE, Subject.HISTORY, 3),
            new GradeEntry(new Student("Christopher", "Williams", Sex.MALE), Grade.GOOD, Subject.MATH, 3),
            new GradeEntry(new Student("Christopher", "Williams", Sex.MALE), Grade.EXCELLENT, Subject.PHYSICAL_EXERCISE, 3),

            new GradeEntry(new Student("Donna", "Ramirez", Sex.FEMALE), Grade.FAILURE, Subject.GRAMMAR, 4),
            new GradeEntry(new Student("Donna", "Ramirez", Sex.FEMALE), Grade.GOOD, Subject.FOREIGN_LANGUAGE, 4),
            new GradeEntry(new Student("Donna", "Ramirez", Sex.FEMALE), Grade.SATISFACTORY, Subject.HISTORY, 4),
            new GradeEntry(new Student("Donna", "Ramirez", Sex.FEMALE), Grade.FAILURE, Subject.MATH, 4),
            new GradeEntry(new Student("Donna", "Ramirez", Sex.FEMALE), Grade.GOOD, Subject.PHYSICAL_EXERCISE, 4),

            new GradeEntry(new Student("Michael", "Sanchez", Sex.MALE), Grade.GOOD, Subject.GRAMMAR, 4),
            new GradeEntry(new Student("Michael", "Sanchez", Sex.MALE), Grade.SATISFACTORY, Subject.FOREIGN_LANGUAGE, 4),
            new GradeEntry(new Student("Michael", "Sanchez", Sex.MALE), Grade.GOOD, Subject.HISTORY, 4),
            new GradeEntry(new Student("Michael", "Sanchez", Sex.MALE), Grade.EXCELLENT, Subject.MATH, 4),
            new GradeEntry(new Student("Michael", "Sanchez", Sex.MALE), Grade.SATISFACTORY, Subject.PHYSICAL_EXERCISE, 4),

            new GradeEntry(new Student("Mary", "Young", Sex.FEMALE), Grade.SATISFACTORY, Subject.GRAMMAR, 4),
            new GradeEntry(new Student("Mary", "Young", Sex.FEMALE), Grade.GOOD, Subject.FOREIGN_LANGUAGE, 4),
            new GradeEntry(new Student("Mary", "Young", Sex.FEMALE), Grade.FAILURE, Subject.HISTORY, 4),
            new GradeEntry(new Student("Mary", "Young", Sex.FEMALE), Grade.EXCELLENT, Subject.MATH, 4),
            new GradeEntry(new Student("Mary", "Young", Sex.FEMALE), Grade.GOOD, Subject.PHYSICAL_EXERCISE, 4),

            new GradeEntry(new Student("Kevin", "Brown", Sex.OTHER), Grade.GOOD, Subject.GRAMMAR, 4),
            new GradeEntry(new Student("Kevin", "Brown", Sex.OTHER), Grade.GOOD, Subject.FOREIGN_LANGUAGE, 4),
            new GradeEntry(new Student("Kevin", "Brown", Sex.OTHER), Grade.SATISFACTORY, Subject.HISTORY, 4),
            new GradeEntry(new Student("Kevin", "Brown", Sex.OTHER), Grade.GOOD, Subject.MATH, 4),
            new GradeEntry(new Student("Kevin", "Brown", Sex.OTHER), Grade.SATISFACTORY, Subject.PHYSICAL_EXERCISE, 4)
    );

    private static final List<String> femaleNames = List.of("Mary",
            "Patricia",
            "Jennifer",
            "Linda",
            "Elizabeth",
            "Barbara",
            "Susan",
            "Jessica",
            "Sarah",
            "Karen",
            "Nancy",
            "Lisa",
            "Betty",
            "Margaret",
            "Sandra",
            "Ashley",
            "Kimberly",
            "Emily",
            "Donna",
            "Michelle",
            "Dorothy",
            "Carol");

    private static final List<String> maleNames = List.of("James",
            "Robert",
            "John",
            "Michael",
            "William",
            "David",
            "Richard",
            "Joseph",
            "Thomas",
            "Charles",
            "Christopher",
            "Daniel",
            "Matthew",
            "Anthony",
            "Mark",
            "Donald",
            "Steven",
            "Paul",
            "Andrew",
            "Joshua",
            "Kenneth",
            "Kevin");
    private static final List<String> lastNames = List.of("Johnson",
            "Williams",
            "Brown",
            "Jones",
            "Garcia",
            "Miller",
            "Davis",
            "Rodriguez",
            "Martinez",
            "Hernandez",
            "Lopez",
            "Gonzalez",
            "Wilson",
            "Anderson",
            "Thomas",
            "Taylor",
            "Moore",
            "Jackson",
            "Martin",
            "Lee",
            "Perez",
            "Thompson",
            "White",
            "Harris",
            "Sanchez",
            "Clark",
            "Ramirez",
            "Lewis",
            "Robinson",
            "Walker",
            "Young",
            "Allen");
}
