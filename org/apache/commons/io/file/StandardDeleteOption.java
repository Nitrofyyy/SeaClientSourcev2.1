// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.file;

public enum StandardDeleteOption implements DeleteOption
{
    OVERRIDE_READ_ONLY;
    
    public static boolean overrideReadOnly(final DeleteOption[] options) {
        if (options == null || options.length == 0) {
            return false;
        }
        for (final DeleteOption deleteOption : options) {
            if (deleteOption == StandardDeleteOption.OVERRIDE_READ_ONLY) {
                return true;
            }
        }
        return false;
    }
}
