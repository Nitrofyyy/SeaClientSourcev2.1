// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class CanExecuteFileFilter extends AbstractFileFilter implements Serializable
{
    private static final long serialVersionUID = 3179904805251622989L;
    public static final IOFileFilter CAN_EXECUTE;
    public static final IOFileFilter CANNOT_EXECUTE;
    
    protected CanExecuteFileFilter() {
    }
    
    @Override
    public boolean accept(final File file) {
        return file.canExecute();
    }
    
    static {
        CAN_EXECUTE = new CanExecuteFileFilter();
        CANNOT_EXECUTE = new NotFileFilter(CanExecuteFileFilter.CAN_EXECUTE);
    }
}
