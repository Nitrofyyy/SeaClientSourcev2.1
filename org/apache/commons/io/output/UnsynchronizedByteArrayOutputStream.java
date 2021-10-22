// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.output;

import org.apache.commons.io.input.UnsynchronizedByteArrayInputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class UnsynchronizedByteArrayOutputStream extends AbstractByteArrayOutputStream
{
    public UnsynchronizedByteArrayOutputStream() {
        this(1024);
    }
    
    public UnsynchronizedByteArrayOutputStream(final int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Negative initial size: " + size);
        }
        this.needNewBuffer(size);
    }
    
    @Override
    public void write(final byte[] b, final int off, final int len) {
        if (off < 0 || off > b.length || len < 0 || off + len > b.length || off + len < 0) {
            throw new IndexOutOfBoundsException(String.format("offset=%,d, length=%,d", off, len));
        }
        if (len == 0) {
            return;
        }
        this.writeImpl(b, off, len);
    }
    
    @Override
    public void write(final int b) {
        this.writeImpl(b);
    }
    
    @Override
    public int write(final InputStream in) throws IOException {
        return this.writeImpl(in);
    }
    
    @Override
    public int size() {
        return this.count;
    }
    
    @Override
    public void reset() {
        this.resetImpl();
    }
    
    @Override
    public void writeTo(final OutputStream out) throws IOException {
        this.writeToImpl(out);
    }
    
    public static InputStream toBufferedInputStream(final InputStream input) throws IOException {
        return toBufferedInputStream(input, 1024);
    }
    
    public static InputStream toBufferedInputStream(final InputStream input, final int size) throws IOException {
        try (final UnsynchronizedByteArrayOutputStream output = new UnsynchronizedByteArrayOutputStream(size)) {
            output.write(input);
            return output.toInputStream();
        }
    }
    
    @Override
    public InputStream toInputStream() {
        return this.toInputStream(UnsynchronizedByteArrayInputStream::new);
    }
    
    @Override
    public byte[] toByteArray() {
        return this.toByteArrayImpl();
    }
}
