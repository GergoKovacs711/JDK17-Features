package com.zeiss.gergo.kovacs.dojo;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UpdatedSwitch implements BasicFunctionalities {

    enum Case {
        SIMPLE,
        COMPLEX,
        UNKNOWN,
        NONE
    }

    private final Supplier<Case> randomCase = () -> randomiser.getElement(Case.values());
    private final List<Case> cases = List.of(Case.values());

    @Test
    void LTSFeatures() {
        switch (randomCase.get()) {
            case SIMPLE -> logger.print("simple");
            case COMPLEX -> logger.print("complicated");
            case UNKNOWN -> logger.print("don't even aks");
            case NONE -> logger.print("none of your business");
            default -> logger.print("default");
        }

        switch (randomCase.get()) {
            case SIMPLE -> logger.print("simple");
            case COMPLEX -> logger.print("complicated");
            case UNKNOWN -> logger.print("don't even aks");

            // no errors if a case is missing BUT warning can be issues by the compiler
            // case NONE -> logger.print("none of your business");

            // no errors if default is missing (maximum a warning)
            // default -> logger.print("default");
        }

        switch (randomCase.get()) {
            case SIMPLE, COMPLEX, UNKNOWN -> logger.print("simple");
            // case UNKNOWN -> logger.print("don't even aks"); // ERROR -> duplicate label
            case default -> logger.print("default");
        }

        final var someValue = generator.nextInt(100);
        switch (someValue) {
            case 1, 2, 3, 4, 5 -> logger.print("smaller than 5");
            case 11 -> logger.print("between 5 and 10");
            case 10 -> logger.print("exactly ten");
            default -> logger.print("wow, that's very big");
        }

        final var stringInput = randomiser.getElement(List.of("AC/DC", "ABC", "XYZ"));
        switch (stringInput) {
            case "ABC", "XYZ" -> logger.print("ABC or XYZ");
            default -> logger.print("not null");
        }

        // cases must be constants
        final var reference = "this is a constant";
//            var reference = "you might lose me!";
        switch (reference) {
            case reference -> logger.print("OK");
            default -> logger.print("it could have changed :(");
        }

        final Case chosenEnum = randomiser.getElement(cases);
        final boolean enumIsKnown = switch (chosenEnum) {
            case SIMPLE, COMPLEX, NONE -> true;
            case UNKNOWN -> false;
        };

        logger.pairs(
                enumIsKnown, "Is enum %s known?".formatted(chosenEnum)
        );

        final var yieldedResult = switch (chosenEnum) {
            case SIMPLE, COMPLEX, NONE -> {
                logger.print("Enum is know");
                yield true;
            }
            case UNKNOWN -> false;
        };
        logger.print(yieldedResult);

        assertThrows(NullPointerException.class, () -> {
            final String nullableReference = null;
            switch (nullableReference) {
                case "" -> logger.print("It's empty");
                default -> logger.print("Something else");
            }
        });
    }

    @Test
    void previewFeatures() {
        final Optional<Boolean> enumIsKnown = switch (randomiser.getElementOrNull(cases)) {
            case SIMPLE, COMPLEX, NONE -> Optional.of(true);
            case UNKNOWN -> Optional.of(false);
            case null -> {
                logger.print("The input was null!");
                yield Optional.empty();
            }
            default -> Optional.empty();
        };

        logger.pairs(
                enumIsKnown.orElse(false), "enum to int conversion is possible"
        );

        // TODO: show the Randomiser's implementation
        final var potentiallyNullInput = randomiser.getElementOrNull(List.of("AC/DC", "ABC", "XYZ"));
        switch (potentiallyNullInput) {
            case "AC/DC" -> logger.print("ABC");
            case null -> logger.print("null");
            default -> logger.print("not null");
//            case null, default -> logger.print("Null or whatever");
        }
    }
}
