// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class MarkShieldInputStream extends ProxyInputStream
{
    public MarkShieldInputStream(final InputStream in) {
        super(in);
    }
    
    @Override
    public void mark(final int readlimit) {
    }
    
    @Override
    public boolean markSupported() {
        return false;
    }
    
    @Override
    public void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }
}
