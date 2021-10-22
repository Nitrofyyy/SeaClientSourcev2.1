// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.output;

import java.io.Writer;

public class CloseShieldWriter extends ProxyWriter
{
    public CloseShieldWriter(final Writer out) {
        super(out);
    }
    
    @Override
    public void close() {
        this.out = ClosedWriter.CLOSED_WRITER;
    }
}
