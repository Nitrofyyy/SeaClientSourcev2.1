// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.input;

import java.io.IOException;
import org.apache.commons.io.IOUtils;
import java.io.Closeable;
import java.io.InputStream;

public class DemuxInputStream extends InputStream
{
    private final InheritableThreadLocal<InputStream> inputStream;
    
    public DemuxInputStream() {
        this.inputStream = new InheritableThreadLocal<InputStream>();
    }
    
    public InputStream bindStream(final InputStream input) {
        final InputStream oldValue = this.inputStream.get();
        this.inputStream.set(input);
        return oldValue;
    }
    
    @Override
    public void close() throws IOException {
        IOUtils.close(this.inputStream.get());
    }
    
    @Override
    public int read() throws IOException {
        final InputStream input = this.inputStream.get();
        if (null != input) {
            return input.read();
        }
        return -1;
    }
}
