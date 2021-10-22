// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io;

import java.util.Collections;
import java.util.List;
import java.io.IOException;

public class IOExceptionList extends IOException
{
    private static final long serialVersionUID = 1L;
    private final List<? extends Throwable> causeList;
    
    public IOExceptionList(final List<? extends Throwable> causeList) {
        super(String.format("%,d exceptions: %s", (causeList == null) ? 0 : causeList.size(), causeList), (causeList == null) ? null : ((Throwable)causeList.get(0)));
        this.causeList = ((causeList == null) ? Collections.emptyList() : causeList);
    }
    
    public <T extends Throwable> List<T> getCauseList() {
        return (List<T>)this.causeList;
    }
    
    public <T extends Throwable> T getCause(final int index) {
        return (T)this.causeList.get(index);
    }
    
    public <T extends Throwable> T getCause(final int index, final Class<T> clazz) {
        return (T)this.causeList.get(index);
    }
    
    public <T extends Throwable> List<T> getCauseList(final Class<T> clazz) {
        return (List<T>)this.causeList;
    }
}
