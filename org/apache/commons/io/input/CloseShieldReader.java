// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.input;

import java.io.Reader;

public class CloseShieldReader extends ProxyReader
{
    public CloseShieldReader(final Reader in) {
        super(in);
    }
    
    @Override
    public void close() {
        this.in = ClosedReader.CLOSED_READER;
    }
}
