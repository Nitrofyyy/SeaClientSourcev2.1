// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.input;

import java.io.IOException;
import java.io.Reader;

public class ClosedReader extends Reader
{
    public static final ClosedReader CLOSED_READER;
    
    @Override
    public int read(final char[] cbuf, final int off, final int len) {
        return -1;
    }
    
    @Override
    public void close() throws IOException {
    }
    
    static {
        CLOSED_READER = new ClosedReader();
    }
}
