// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.output;

import java.io.Writer;
import java.util.Collection;

public class TeeWriter extends ProxyCollectionWriter
{
    public TeeWriter(final Collection<Writer> writers) {
        super(writers);
    }
    
    public TeeWriter(final Writer... writers) {
        super(writers);
    }
}
