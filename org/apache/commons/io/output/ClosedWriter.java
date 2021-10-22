// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.output;

import java.io.IOException;
import java.io.Writer;

public class ClosedWriter extends Writer
{
    public static final ClosedWriter CLOSED_WRITER;
    
    @Override
    public void write(final char[] cbuf, final int off, final int len) throws IOException {
        throw new IOException("write(" + new String(cbuf) + ", " + off + ", " + len + ") failed: stream is closed");
    }
    
    @Override
    public void flush() throws IOException {
        throw new IOException("flush() failed: stream is closed");
    }
    
    @Override
    public void close() throws IOException {
    }
    
    static {
        CLOSED_WRITER = new ClosedWriter();
    }
}
