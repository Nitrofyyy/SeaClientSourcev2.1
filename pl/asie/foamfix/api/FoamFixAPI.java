// 
// Decompiled by Procyon v0.5.36
// 

package pl.asie.foamfix.api;

public class FoamFixAPI
{
    public static IFoamFixHelper HELPER;
    
    static {
        FoamFixAPI.HELPER = new IFoamFixHelper.Default();
    }
}
