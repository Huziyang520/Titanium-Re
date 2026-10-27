// Titanium Lite Array Util

package com.hrznstudio.titanium.util;

import java.util.function.Consumer;

public class ArrayUtil {
    @SafeVarargs
    public static <T> void forEach(Consumer<T> consumer, T... values) {
        for (T t : values)
            consumer.accept(t);
    }
}
