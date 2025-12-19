package org.mangorage.tiab.common.api;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

final class Getter {
    static final Supplier<Supplier<ICommonTimeInABottleAPI>> GETTER = () -> {
        try {
            return (Supplier<ICommonTimeInABottleAPI>) Class.forName("org.mangorage.tiab.common.TiabMod").getDeclaredMethod("getAPIHolder").invoke(null);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    };
}
