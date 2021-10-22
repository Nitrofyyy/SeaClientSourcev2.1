// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.input;

import java.io.Reader;

public class CharacterFilterReader extends AbstractCharacterFilterReader
{
    private final int skip;
    
    public CharacterFilterReader(final Reader reader, final int skip) {
        super(reader);
        this.skip = skip;
    }
    
    @Override
    protected boolean filter(final int ch) {
        return ch == this.skip;
    }
}
