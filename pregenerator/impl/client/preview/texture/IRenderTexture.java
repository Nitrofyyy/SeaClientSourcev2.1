// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.preview.texture;

import net.minecraft.client.Minecraft;
import pregenerator.impl.client.preview.world.data.IChunkData;

public interface IRenderTexture
{
    void clearTexture();
    
    void translate(final float p0, final float p1, final float p2);
    
    void onRender(final boolean p0, final MoveableTexture.IRenderFunction p1, final MoveableTexture p2);
    
    void addData(final IChunkData p0, final int p1, final int p2);
    
    void onRenderFinished(final Minecraft p0);
    
    void removeTexture();
}
