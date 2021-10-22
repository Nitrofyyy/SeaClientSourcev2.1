// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.preview.world;

public interface IHeightMap
{
    void storeHeightData(final int p0, final int p1, final int[] p2);
    
    int getHeight(final int p0, final int p1);
    
    int getHeight(final int p0, final int p1, final int p2);
    
    int[] getHeightData(final int p0, final int p1);
    
    boolean hasHeightsStored(final int p0, final int p1);
}
