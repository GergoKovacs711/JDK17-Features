package com.zeiss.gergo.kovacs.util;

import java.util.List;
import java.util.Random;

public class Randomiser {
    public final Random gen = new Random();

    public <T> T getElement(final List<T> list) {
        return list.get(randomIndexIn(list));
    }

    public <T> T getElement(final T[] array) {
        return array[randomIndexIn(array)];
    }

    public <T> T getElementOrNull(final List<T> list) {
        return gen.nextBoolean() ? null : list.get(randomIndexIn(list));
    }

    private int randomIndexIn(final Object arrayOrList){
        final int size = switch (arrayOrList) {
            case Object[] array -> array.length;
            case List<?> list -> list.size();
            case null, default -> throw new IllegalArgumentException("Could not determine size!");
        };
       return gen.nextInt(size);
    }
}
