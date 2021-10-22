// 
// Decompiled by Procyon v0.5.36
// 

package sea.event;

public enum Priority
{
    FIRST("FIRST", 0), 
    SECOND("SECOND", 1), 
    THIRD("THIRD", 2), 
    FOURTH("FOURTH", 3), 
    FIFTH("FIFTH", 4), 
    HIGHEST("HIGHEST", 5), 
    HIGH("HIGH", 6), 
    NORMAL("NORMAL", 7), 
    LOW("LOW", 8), 
    LOWEST("LOWEST", 9);
    
    public static final byte[] VALUE_ARRAY;
    
    static {
        VALUE_ARRAY = new byte[] { 0, 1, 2, 3, 4 };
    }
    
    private Priority(final String name, final int ordinal) {
    }
}
