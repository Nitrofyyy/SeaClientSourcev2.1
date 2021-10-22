// 
// Decompiled by Procyon v0.5.36
// 

package pl.asie.foamfix.shared;

public class FoamFixShared
{
    public static final FoamFixConfig config;
    public static boolean coremodEnabled;
    public static int ramSaved;
    
    public static boolean hasOptifine() {
        try {
            return Class.forName("optifine.OptiFineTweaker") != null;
        }
        catch (ClassNotFoundException e) {
            return false;
        }
    }
    
    static {
        config = new FoamFixConfig();
        FoamFixShared.coremodEnabled = false;
        FoamFixShared.ramSaved = 0;
    }
}
