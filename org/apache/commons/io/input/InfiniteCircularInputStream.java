// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.input;

public class InfiniteCircularInputStream extends CircularInputStream
{
    public InfiniteCircularInputStream(final byte[] repeatContent) {
        super(repeatContent, -1L);
    }
}
