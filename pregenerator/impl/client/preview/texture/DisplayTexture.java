// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.preview.texture;

import java.awt.Color;
import net.minecraft.util.MathHelper;
import pregenerator.impl.client.preview.world.data.ChunkData;
import pregenerator.impl.client.preview.world.data.IChunkData;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class DisplayTexture implements IRenderTexture
{
    public static final float[] defaultData;
    int width;
    int height;
    int xSize;
    int ySize;
    int[] textures;
    
    public DisplayTexture(final int width, final int height) {
        this.generateTexture(this.width = width, this.height = height);
        this.clearTexture();
    }
    
    @Override
    public void clearTexture() {
        final int frame = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(36009, frame);
        GL11.glViewport(0, 0, 4096, 4096);
        GL11.glDrawBuffer(36064);
        for (int i = 0; i < this.textures.length; ++i) {
            GL30.glFramebufferTexture2D(36160, 36064, 3553, this.textures[i], 0);
            GL11.glClear(16384);
            GL11.glClearColor(DisplayTexture.defaultData[0], DisplayTexture.defaultData[1], DisplayTexture.defaultData[2], 1.0f);
        }
        GL30.glDeleteFramebuffers(frame);
        GL30.glBindFramebuffer(36009, Minecraft.func_71410_x().func_147110_a().field_147616_f);
        final Minecraft mc = Minecraft.func_71410_x();
        GL11.glViewport(0, 0, mc.field_71443_c, mc.field_71440_d);
    }
    
    public void addPixels(final int x, final int y, final int[] data, final int width, final int height) {
        GlStateManager.func_179144_i(this.getTexture(x, y));
        TextureUtil.func_147955_a(new int[][] { data }, width, height, x % 4096, y % 4096, false, false);
    }
    
    @Override
    public void translate(final float x, final float y, final float scale) {
        GL11.glTranslatef(x, y, 0.0f);
        GL11.glScalef(scale, scale, 1.0f);
        GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    @Override
    public void onRender(final boolean slime, final MoveableTexture.IRenderFunction function, final MoveableTexture texture) {
        final float xPos = texture.getX();
        final float yPos = texture.getY();
        for (int x = 0; x < this.xSize; ++x) {
            for (int y = 0; y < this.ySize; ++y) {
                GlStateManager.func_179144_i(this.textures[this.ySize * x + y]);
                function.render(xPos + x * 4096.0f, yPos + y * 4096.0f, 4096, 4096);
            }
        }
    }
    
    @Override
    public void addData(final IChunkData data, final int size, final int view) {
        final ChunkData chunk = (ChunkData)data;
        final int x = data.getX() * 16 + size;
        final int z = data.getZ() * 16 + size;
        this.addPixels(x, z, chunk.getData(view), 16, 16);
    }
    
    @Override
    public void onRenderFinished(final Minecraft mc) {
    }
    
    @Override
    public void removeTexture() {
        for (int i = 0; i < this.textures.length; ++i) {
            if (this.textures[i] != -1) {
                TextureUtil.func_147942_a(this.textures[i]);
                this.textures[i] = -1;
            }
        }
    }
    
    public int getTexture(final int x, final int y) {
        return this.textures[MathHelper.func_76125_a(x, 0, this.width) / 4096 * this.ySize + MathHelper.func_76125_a(y, 0, this.height) / 4096];
    }
    
    public void generateTexture(final int width, final int height) {
        this.xSize = MathHelper.func_76143_f(width / 4096.0);
        this.ySize = MathHelper.func_76143_f(height / 4096.0);
        this.textures = new int[this.xSize * this.ySize];
        for (int x = 0; x < this.xSize; ++x) {
            for (int y = 0; y < this.ySize; ++y) {
                final int index = this.ySize * x + y;
                TextureUtil.func_110991_a(this.textures[index] = TextureUtil.func_110996_a(), 4096, 4096);
            }
        }
    }
    
    static {
        defaultData = new Color(139, 139, 139).getColorComponents(new float[3]);
    }
}
