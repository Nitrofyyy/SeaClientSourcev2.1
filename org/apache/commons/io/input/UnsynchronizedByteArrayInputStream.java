// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.input;

import java.util.Objects;
import java.io.InputStream;

public class UnsynchronizedByteArrayInputStream extends InputStream
{
    public static final int END_OF_STREAM = -1;
    private final byte[] data;
    private final int eod;
    private int offset;
    private int markedOffset;
    
    public UnsynchronizedByteArrayInputStream(final byte[] data) {
        Objects.requireNonNull(data);
        this.data = data;
        this.offset = 0;
        this.eod = data.length;
        this.markedOffset = this.offset;
    }
    
    public UnsynchronizedByteArrayInputStream(final byte[] data, final int offset) {
        Objects.requireNonNull(data);
        if (offset < 0) {
            throw new IllegalArgumentException("offset cannot be negative");
        }
        this.data = data;
        this.offset = Math.min(offset, (data.length > 0) ? data.length : offset);
        this.eod = data.length;
        this.markedOffset = this.offset;
    }
    
    public UnsynchronizedByteArrayInputStream(final byte[] data, final int offset, final int length) {
        Objects.requireNonNull(data);
        if (offset < 0) {
            throw new IllegalArgumentException("offset cannot be negative");
        }
        if (length < 0) {
            throw new IllegalArgumentException("length cannot be negative");
        }
        this.data = data;
        this.offset = Math.min(offset, (data.length > 0) ? data.length : offset);
        this.eod = Math.min(this.offset + length, data.length);
        this.markedOffset = this.offset;
    }
    
    @Override
    public int available() {
        return (this.offset < this.eod) ? (this.eod - this.offset) : 0;
    }
    
    @Override
    public int read() {
        return (this.offset < this.eod) ? (this.data[this.offset++] & 0xFF) : -1;
    }
    
    @Override
    public int read(final byte[] b) {
        Objects.requireNonNull(b);
        return this.read(b, 0, b.length);
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) {
        Objects.requireNonNull(b);
        if (off < 0 || len < 0 || off + len > b.length) {
            throw new IndexOutOfBoundsException();
        }
        if (this.offset >= this.eod) {
            return -1;
        }
        int actualLen = this.eod - this.offset;
        if (len < actualLen) {
            actualLen = len;
        }
        if (actualLen <= 0) {
            return 0;
        }
        System.arraycopy(this.data, this.offset, b, off, actualLen);
        this.offset += actualLen;
        return actualLen;
    }
    
    @Override
    public long skip(final long n) {
        if (n < 0L) {
            throw new IllegalArgumentException("Skipping backward is not supported");
        }
        long actualSkip = this.eod - this.offset;
        if (n < actualSkip) {
            actualSkip = n;
        }
        this.offset += (int)actualSkip;
        return actualSkip;
    }
    
    @Override
    public boolean markSupported() {
        return true;
    }
    
    @Override
    public void mark(final int readlimit) {
        this.markedOffset = this.offset;
    }
    
    @Override
    public void reset() {
        this.offset = this.markedOffset;
    }
}
