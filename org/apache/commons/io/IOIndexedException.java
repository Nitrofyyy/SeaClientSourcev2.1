// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io;

import java.io.IOException;

public class IOIndexedException extends IOException
{
    private static final long serialVersionUID = 1L;
    private final int index;
    
    public IOIndexedException(final int index, final Throwable cause) {
        super(toMessage(index, cause), cause);
        this.index = index;
    }
    
    protected static String toMessage(final int index, final Throwable cause) {
        final String unspecified = "Null";
        final String name = (cause == null) ? "Null" : cause.getClass().getSimpleName();
        final String msg = (cause == null) ? "Null" : cause.getMessage();
        return String.format("%s #%,d: %s", name, index, msg);
    }
    
    public int getIndex() {
        return this.index;
    }
}
