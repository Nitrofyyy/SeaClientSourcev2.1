// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.input;

import java.util.Collections;
import java.io.Reader;
import java.util.Set;

public class CharacterSetFilterReader extends AbstractCharacterFilterReader
{
    private static final Set<Integer> EMPTY_SET;
    private final Set<Integer> skipSet;
    
    public CharacterSetFilterReader(final Reader reader, final Set<Integer> skip) {
        super(reader);
        this.skipSet = ((skip == null) ? CharacterSetFilterReader.EMPTY_SET : Collections.unmodifiableSet((Set<? extends Integer>)skip));
    }
    
    @Override
    protected boolean filter(final int ch) {
        return this.skipSet.contains(ch);
    }
    
    static {
        EMPTY_SET = Collections.emptySet();
    }
}
