// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.preview.texture;

import pregenerator.impl.client.preview.world.data.CompressedChunkData;
import pregenerator.impl.client.preview.world.data.IChunkData;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import java.io.IOException;
import net.minecraft.client.resources.IResourceManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.BufferUtils;
import java.nio.ByteBuffer;
import net.minecraft.client.renderer.texture.AbstractTexture;

public class CompressedTexture extends AbstractTexture implements IRenderTexture
{
    ByteBuffer buffer;
    int width;
    int height;
    int ticker;
    boolean changed;
    
    public CompressedTexture(final int width, final int height) {
        this.ticker = 0;
        this.changed = false;
        this.buffer = BufferUtils.createByteBuffer(width * height * 4);
        this.width = width;
        this.height = height;
        final int id = this.func_110552_b();
        GlStateManager.func_179144_i(id);
        GL11.glTexParameterf(3553, 10241, 9728.0f);
        GL11.glTexParameterf(3553, 10240, 9728.0f);
        GL11.glTexImage2D(3553, 0, 6408, width, height, 0, 6408, 5121, this.buffer);
        GlStateManager.func_179144_i(0);
        this.clearTexture();
    }
    
    @Override
    public void removeTexture() {
        this.func_147631_c();
    }
    
    @Override
    public void clearTexture() {
    }
    
    public void func_110551_a(final IResourceManager resourceManager) throws IOException {
    }
    
    @Override
    public void translate(final float x, final float y, final float scale) {
        final RenderShader shader = RenderShader.SHADER;
        shader.startShader();
        shader.loadMat("transform", RenderShader.createMatrix(x, y, 2000.0f, scale));
        final ScaledResolution res = new ScaledResolution(Minecraft.func_71410_x());
        shader.loadMat("proViewMatrix", RenderShader.orthoLH(0.0f, (float)res.func_78327_c(), (float)res.func_78324_d(), 0.0f, 1000.0f, 3000.0f, false));
    }
    
    @Override
    public void onRender(final boolean slime, final MoveableTexture.IRenderFunction function, final MoveableTexture texture) {
        GlStateManager.func_179144_i(this.func_110552_b());
        RenderShader.SHADER.loadInt("dataMap", this.func_110552_b());
    }
    
    @Override
    public void addData(final IChunkData data, final int size, final int view) {
        final CompressedChunkData chunk = (CompressedChunkData)data;
        int value = 1 << view;
        if (chunk.slimeChunk) {
            value += 8;
        }
        this.buffer.limit(this.width * this.height * 4);
        final int[] toAdd = chunk.getData(view > 0);
        final int xPos = data.getX() * 16 + size;
        final int zPos = data.getZ() * 16 + size;
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                final int index = z * 16 + x;
                int bufferIndex = (zPos + z) * this.width + (xPos + x);
                bufferIndex *= 4;
                this.buffer.put(bufferIndex, (byte)(toAdd[index] & 0xFF));
                this.buffer.put(bufferIndex + 1, (byte)0);
                this.buffer.put(bufferIndex + 2, (byte)0);
                this.buffer.put(bufferIndex + 3, (byte)0);
            }
        }
        this.changed = true;
        this.buffer.flip();
    }
    
    @Override
    public void onRenderFinished(final Minecraft mc) {
        RenderShader.SHADER.stopShader();
        mc.field_71460_t.func_78478_c();
        ++this.ticker;
        if (this.ticker >= 20) {
            this.ticker = 0;
            if (this.changed) {
                this.changed = false;
                this.buffer.flip();
                this.buffer.limit(this.width * this.height * 4);
                GlStateManager.func_179144_i(this.func_110552_b());
                GL11.glTexImage2D(3553, 0, 6408, this.width, this.height, 0, 6408, 5121, this.buffer);
            }
        }
    }
}
