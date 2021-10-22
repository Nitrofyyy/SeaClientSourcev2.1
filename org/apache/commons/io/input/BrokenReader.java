// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.input;

import java.io.IOException;
import java.io.Reader;

public class BrokenReader extends Reader
{
    private final IOException exception;
    
    public BrokenReader(final IOException exception) {
        this.exception = exception;
    }
    
    public BrokenReader() {
        this(new IOException("Broken reader"));
    }
    
    @Override
    public int read(final char[] cbuf, final int off, final int len) throws IOException {
        throw this.exception;
    }
    
    @Override
    public long skip(final long n) throws IOException {
        throw this.exception;
    }
    
    @Override
    public boolean ready() throws IOException {
        throw this.exception;
    }
    
    @Override
    public void mark(final int readAheadLimit) throws IOException {
        throw this.exception;
    }
    
    @Override
    public synchronized void reset() throws IOException {
        throw this.exception;
    }
    
    @Override
    public void close() throws IOException {
        throw this.exception;
    }
}
