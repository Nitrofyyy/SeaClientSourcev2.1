// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.preview.texture;

import pregenerator.impl.client.preview.world.data.IChunkData;
import net.minecraft.client.Minecraft;
import pregenerator.impl.client.preview.world.WorldSeed;

public class MoveableTexture
{
    float xPosition;
    float yPosition;
    int width;
    int height;
    IRenderTexture texture;
    
    public MoveableTexture(final int size) {
        this.width = size;
        this.height = size;
        this.centerTexture();
        this.createTexture();
    }
    
    public void removeTexture() {
        if (this.texture != null) {
            this.texture.removeTexture();
            this.texture = null;
        }
    }
    
    public void resizeTexture(final int newSize) {
        this.removeTexture();
        this.width = newSize;
        this.height = newSize;
        this.createTexture();
    }
    
    public void createTexture() {
        this.texture = (WorldSeed.isUsingCompression() ? new CompressedTexture(this.width, this.height) : new DisplayTexture(this.width, this.height));
    }
    
    public void createTextureIfMissing() {
        if (this.texture == null) {
            this.createTexture();
        }
    }
    
    public void translate(final float x, final float y, final float scale) {
        this.texture.translate(x, y, scale);
    }
    
    public void render(final boolean slime, final IRenderFunction function) {
        this.texture.onRender(slime, function, this);
        this.texture.onRenderFinished(Minecraft.func_71410_x());
    }
    
    public void addChunkData(final IChunkData data, final int size, final int view) {
        if (this.texture == null) {
            return;
        }
        this.texture.addData(data, size, view);
    }
    
    public void clearTexture() {
        if (this.texture == null) {
            return;
        }
        this.texture.clearTexture();
    }
    
    public void centerTexture() {
        this.xPosition = (float)(-(this.width / 2));
        this.yPosition = (float)(-(this.height / 2));
    }
    
    public void moveTexture(final float xMove, final float yMove) {
        this.xPosition += xMove;
        this.yPosition += yMove;
    }
    
    public float getX() {
        return this.xPosition;
    }
    
    public float getY() {
        return this.yPosition;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public interface IRenderFunction
    {
        void render(final float p0, final float p1, final int p2, final int p3);
    }
}
