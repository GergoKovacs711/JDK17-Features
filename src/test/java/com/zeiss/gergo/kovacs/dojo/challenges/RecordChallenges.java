package com.zeiss.gergo.kovacs.dojo.challenges;

import com.zeiss.gergo.kovacs.dojo.BasicFunctionalities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Optional;

public class RecordChallenges implements BasicFunctionalities {

    /**
     * Create a record, that has a validation in its constructor and throws IllegalArgumentException when
     * we try to initialise one of its fields with null.
     */
    @Test
    void challenge1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            final var record = new TestRecord(null);
        });
    }
}

record Bla(Integer n){
    public Bla {
        try {
            Objects.requireNonNull(n);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException();
        }
    }
}

record CustomRecord(Integer abc) {
    public CustomRecord {
        if (abc == null) {
            throw new IllegalArgumentException("must not be null");
        }
    }
}

record myCustomRecord(String somethingElse) {
    public myCustomRecord {
        Objects.requireNonNullElse(somethingElse, new IllegalArgumentException());
    }
}

record TestRecord(String input) {
    TestRecord {
        Optional.ofNullable(input).orElseThrow(() -> new IllegalArgumentException("input was null"));
    }
}
