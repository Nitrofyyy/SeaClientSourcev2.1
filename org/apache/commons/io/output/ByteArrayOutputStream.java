// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.output;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteArrayOutputStream extends AbstractByteArrayOutputStream
{
    public ByteArrayOutputStream() {
        this(1024);
    }
    
    public ByteArrayOutputStream(final int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Negative initial size: " + size);
        }
        synchronized (this) {
            this.needNewBuffer(size);
        }
    }
    
    @Override
    public void write(final byte[] b, final int off, final int len) {
        if (off < 0 || off > b.length || len < 0 || off + len > b.length || off + len < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return;
        }
        synchronized (this) {
            this.writeImpl(b, off, len);
        }
    }
    
    @Override
    public synchronized void write(final int b) {
        this.writeImpl(b);
    }
    
    @Override
    public synchronized int write(final InputStream in) throws IOException {
        return this.writeImpl(in);
    }
    
    @Override
    public synchronized int size() {
        return this.count;
    }
    
    @Override
    public synchronized void reset() {
        this.resetImpl();
    }
    
    @Override
    public synchronized void writeTo(final OutputStream out) throws IOException {
        this.writeToImpl(out);
    }
    
    public static InputStream toBufferedInputStream(final InputStream input) throws IOException {
        return toBufferedInputStream(input, 1024);
    }
    
    public static InputStream toBufferedInputStream(final InputStream input, final int size) throws IOException {
        try (final ByteArrayOutputStream output = new ByteArrayOutputStream(size)) {
            output.write(input);
            return output.toInputStream();
        }
    }
    
    @Override
    public synchronized InputStream toInputStream() {
        return this.toInputStream(ByteArrayInputStream::new);
    }
    
    @Override
    public synchronized byte[] toByteArray() {
        return this.toByteArrayImpl();
    }
}
