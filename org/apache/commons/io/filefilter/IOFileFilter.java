// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.FilenameFilter;
import java.io.FileFilter;

public interface IOFileFilter extends FileFilter, FilenameFilter
{
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    
    boolean accept(final File p0);
    
    boolean accept(final File p0, final String p1);
}
