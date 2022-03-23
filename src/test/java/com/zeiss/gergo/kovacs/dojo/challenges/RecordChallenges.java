package com.zeiss.gergo.kovacs.dojo.challenges;

import com.zeiss.gergo.kovacs.dojo.BasicFunctionalities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RecordChallenges implements BasicFunctionalities {

    /**
     * Create a record, that has a validation in its constructor and throws IllegalArgumentException when
     * we try to initialise one of its fields with null.
     */
    @Test
    void challenge1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            final var record = ;
            throw new IllegalArgumentException("Remove this throw");
        });
    }
}
