// 
// Decompiled by Procyon v0.5.36
// 

package sea;

public enum Color
{
    WHITE("WHITE", 0, "White", 553648127), 
    RED("RED", 1, "Red", 16711680), 
    GREEN("GREEN", 2, "Green", 63), 
    LIME("LIME", 3, "Lime", 65280), 
    PURPLE("PURPLE", 4, "Purple", 8388736), 
    AQUA("AQUA", 5, "Aqua", 65535), 
    PINK("PINK", 6, "Pink", 16711935), 
    RAINBOW("RAINBOW", 7, "Rainbow", -1);
    
    private String name;
    private int hex;
    
    private Color(final String name2, final int ordinal, final String name, final int hex) {
        this.name = name;
        this.hex = hex;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getHex() {
        return this.hex;
    }
}
