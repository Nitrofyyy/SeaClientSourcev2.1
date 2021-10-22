// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.preview.world.data;

import pregenerator.impl.client.preview.world.IHeightMap;
import pregenerator.impl.client.preview.texture.MoveableTexture;
import pregenerator.impl.client.preview.data.ITask;

public interface IChunkData extends ITask
{
    int getX();
    
    int getZ();
    
    int getHeight(final int p0, final int p1);
    
    int getBiome(final int p0, final int p1);
    
    boolean isSlimeChunk();
    
    void addToTexture(final MoveableTexture p0, final int p1, final int p2);
    
    void updateHeightMap(final IHeightMap p0);
    
    void storeInHeightMap(final IHeightMap p0);
}
