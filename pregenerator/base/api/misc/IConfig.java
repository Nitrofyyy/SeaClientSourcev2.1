// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.base.api.misc;

public interface IConfig
{
    boolean getBoolean(final String p0, final String p1, final boolean p2);
    
    boolean getBoolean(final String p0, final String p1, final boolean p2, final String p3);
    
    void setBoolean(final String p0, final String p1, final boolean p2);
    
    int getInt(final String p0, final String p1, final int p2);
    
    int getInt(final String p0, final String p1, final int p2, final String p3);
    
    int getInt(final String p0, final String p1, final int p2, final int p3, final int p4, final String p5);
    
    void setInt(final String p0, final String p1, final int p2);
    
    String getString(final String p0, final String p1, final String p2);
    
    String getString(final String p0, final String p1, final String p2, final String p3);
    
    void setString(final String p0, final String p1, final String p2);
    
    void prepaireMassChange();
    
    void saveConfig();
}
