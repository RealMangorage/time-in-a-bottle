package org.mangorage.tiab.common.misc;

import java.util.function.Supplier;

public class LazySupplier<T> implements Supplier<T> {
    public static <T> Supplier<T> of(Supplier<T> supplier) {
        return new LazySupplier<>(supplier);
    }

    private final Supplier<T> supplier;
    private T instance;

    private LazySupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        return instance != null ? instance : (instance = supplier.get());
    }
}
