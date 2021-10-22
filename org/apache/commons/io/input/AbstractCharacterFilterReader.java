// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.input;

import java.io.IOException;
import java.io.Reader;
import java.io.FilterReader;

public abstract class AbstractCharacterFilterReader extends FilterReader
{
    protected AbstractCharacterFilterReader(final Reader reader) {
        super(reader);
    }
    
    @Override
    public int read() throws IOException {
        int ch;
        do {
            ch = this.in.read();
        } while (this.filter(ch));
        return ch;
    }
    
    protected abstract boolean filter(final int p0);
    
    @Override
    public int read(final char[] cbuf, final int off, final int len) throws IOException {
        final int read = super.read(cbuf, off, len);
        if (read == -1) {
            return -1;
        }
        int pos = off - 1;
        for (int readPos = off; readPos < off + read; ++readPos) {
            if (!this.filter(cbuf[readPos])) {
                if (++pos < readPos) {
                    cbuf[pos] = cbuf[readPos];
                }
            }
        }
        return pos - off + 1;
    }
}
