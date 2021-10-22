// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.function;

import java.io.IOException;

@FunctionalInterface
public interface IOSupplier<T>
{
    T get() throws IOException;
}
