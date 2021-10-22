// 
// Decompiled by Procyon v0.5.36
// 

package sea.cosmetic.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import sea.cosmetic.CosmeticModelBase;
import net.minecraft.client.model.ModelRenderer;
import java.awt.Color;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import sea.cosmetic.CosmeticBoolean;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import sea.cosmetic.CosmeticBase;

public class CosmeticHalo extends CosmeticBase
{
    private final ModelHalo modelHalo;
    private static final ResourceLocation HALOBLUE;
    
    static {
        HALOBLUE = new ResourceLocation("textures/seaclient/halo/blue.jpeg");
    }
    
    public CosmeticHalo(final RenderPlayer renderPlayer) {
        super(renderPlayer);
        this.modelHalo = new ModelHalo(renderPlayer);
    }
    
    @Override
    public void render(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float headYaw, final float headPitch, final float scale) {
        if (CosmeticBoolean.shouldRenderTopHat(player) && CosmeticBoolean.isHaloblue()) {
            GlStateManager.pushMatrix();
            this.playerRendner.bindTexture(CosmeticHalo.HALOBLUE);
            final float[] color = CosmeticBoolean.getHaloColor(player);
            GL11.glColor3f(color[0], color[1], color[2]);
            this.modelHalo.render(player, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scale);
            GL11.glColor3f(1.0f, 1.0f, 1.0f);
            GL11.glPopMatrix();
        }
    }
    
    private class ModelHalo extends CosmeticModelBase
    {
        private ModelRenderer halo;
        private boolean hat;
        
        public ModelHalo(final RenderPlayer player) {
            super(player);
            this.hat = CosmeticBoolean.isHaloblue();
            (this.halo = new ModelRenderer(this.playerModel).setTextureSize(14, 2)).addBox(-3.0f, -12.5f, -4.0f, 6, 1, 1, 0.15f);
            this.halo.isHidden = true;
        }
        
        @Override
        public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float headYaw, final float headPitch, final float scale) {
            GlStateManager.pushMatrix();
            final float f = (float)Math.cos(ageInTicks / 10.0) / 20.0f;
            GlStateManager.rotate(headYaw + ageInTicks / 2.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(0.0f, f - (this.isHat() ? 0.2f : 0.0f), 0.0f);
            Minecraft.getMinecraft().getTextureManager().bindTexture(CosmeticHalo.HALOBLUE);
            GlStateManager.disableLighting();
            final ModelRenderer modelrenderer = CosmeticBase.this.bindTextureAndColor(Color.WHITE, CosmeticHalo.HALOBLUE, this.halo, null);
            modelrenderer.isHidden = false;
            for (int i = 0; i < 4; ++i) {
                modelrenderer.render(scale);
                GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
            }
            modelrenderer.isHidden = true;
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
        
        public boolean isHat() {
            return this.hat;
        }
        
        private Color getColor() {
            return Color.WHITE;
        }
    }
}
