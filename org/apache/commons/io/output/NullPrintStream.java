// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.output;

import java.io.OutputStream;
import java.io.PrintStream;

public class NullPrintStream extends PrintStream
{
    public static final NullPrintStream NULL_PRINT_STREAM;
    
    public NullPrintStream() {
        super(NullOutputStream.NULL_OUTPUT_STREAM);
    }
    
    static {
        NULL_PRINT_STREAM = new NullPrintStream();
    }
}
