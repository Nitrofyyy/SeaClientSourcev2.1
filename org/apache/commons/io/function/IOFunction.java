// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.function;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.Function;
import java.util.Objects;
import java.io.IOException;

@FunctionalInterface
public interface IOFunction<T, R>
{
    R apply(final T p0) throws IOException;
    
    default <V> IOFunction<V, R> compose(final IOFunction<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return v -> this.apply(before.apply((Object)v));
    }
    
    default <V> IOFunction<V, R> compose(final Function<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return v -> this.apply(before.apply((Object)v));
    }
    
    default IOSupplier<R> compose(final IOSupplier<? extends T> before) {
        Objects.requireNonNull(before);
        return (IOSupplier<R>)(() -> this.apply(before.get()));
    }
    
    default IOSupplier<R> compose(final Supplier<? extends T> before) {
        Objects.requireNonNull(before);
        return (IOSupplier<R>)(() -> this.apply(before.get()));
    }
    
    default <V> IOFunction<T, V> andThen(final IOFunction<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (IOFunction<T, V>)(t -> after.apply(this.apply(t)));
    }
    
    default <V> IOFunction<T, V> andThen(final Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (IOFunction<T, V>)(t -> after.apply((Object)this.apply(t)));
    }
    
    default IOConsumer<T> andThen(final IOConsumer<? super R> after) {
        Objects.requireNonNull(after);
        return t -> after.accept((Object)this.apply(t));
    }
    
    default IOConsumer<T> andThen(final Consumer<? super R> after) {
        Objects.requireNonNull(after);
        return t -> after.accept((Object)this.apply(t));
    }
    
    default <T> IOFunction<T, T> identity() {
        return t -> t;
    }
}
