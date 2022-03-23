package com.zeiss.gergo.kovacs.dojo;

import com.zeiss.gergo.kovacs.util.Logger;
import com.zeiss.gergo.kovacs.util.Randomiser;
import com.zeiss.gergo.kovacs.util.SnippetRunner;

import java.util.Random;

public interface BasicFunctionalities {
    Randomiser randomiser = new Randomiser();
    Logger logger = new Logger();
    Generator generator = new Generator();
    SnippetRunner snippet = new SnippetRunner();

    final class Generator extends Random {
        int randomRealNumber(int bound) {
            return nextInt(bound) * (nextBoolean() ? 1 : -1);
        }
    }
}
