// 
// Decompiled by Procyon v0.5.36
// 

package sea.cosmetic.impl;

import net.minecraft.util.ResourceLocation;
import sea.cosmetic.CosmeticModelBase;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import sea.cosmetic.CosmeticBoolean;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.model.ModelRenderer;
import sea.cosmetic.CosmeticBase;

public class CosmeticDragonWings2 extends CosmeticBase
{
    private static ModelRenderer wing;
    private static ModelRenderer wingTip;
    boolean flying;
    private final ModelDragonWings modelDragonWings;
    
    public CosmeticDragonWings2(final RenderPlayer renderPlayer) {
        super(renderPlayer);
        this.flying = false;
        (this.modelDragonWings = new ModelDragonWings(renderPlayer)).setTextureOffset("wingTip.bone", 112, 136);
        this.modelDragonWings.setTextureOffset("wing.skin", -56, 88);
        this.modelDragonWings.setTextureOffset("wing.bone", 112, 88);
        this.modelDragonWings.setTextureOffset("wingTip.skin", -56, 144);
        final int bw = this.modelDragonWings.textureWidth;
        final int bh = this.modelDragonWings.textureHeight;
        this.modelDragonWings.textureWidth = 256;
        this.modelDragonWings.textureHeight = 256;
        (CosmeticDragonWings2.wing = new ModelRenderer(this.modelDragonWings, "wing")).setRotationPoint(-12.0f, 5.0f, 2.0f);
        CosmeticDragonWings2.wing.addBox("bone", -56.0f, -4.0f, -4.0f, 56, 8, 8);
        CosmeticDragonWings2.wing.addBox("skin", -56.0f, 0.0f, 2.0f, 56, 0, 56);
        CosmeticDragonWings2.wing.isHidden = true;
        (CosmeticDragonWings2.wingTip = new ModelRenderer(this.modelDragonWings, "wingTip")).setRotationPoint(-56.0f, 0.0f, 0.0f);
        CosmeticDragonWings2.wingTip.isHidden = true;
        CosmeticDragonWings2.wingTip.addBox("bone", -56.0f, -2.0f, -2.0f, 56, 4, 4);
        CosmeticDragonWings2.wingTip.addBox("skin", -56.0f, 0.0f, 2.0f, 56, 0, 56);
        CosmeticDragonWings2.wing.addChild(CosmeticDragonWings2.wingTip);
        this.modelDragonWings.textureWidth = bw;
        this.modelDragonWings.textureWidth = bh;
    }
    
    @Override
    public void render(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float HeadYaw, final float headPitch, final float scale) {
        if (CosmeticBoolean.isDragonWings2() && player.getName().equals(Minecraft.getMinecraft().getSession().getUsername())) {
            GlStateManager.pushMatrix();
            this.modelDragonWings.render(player, limbSwing, limbSwingAmount, ageInTicks, HeadYaw, headPitch, scale);
            this.modelDragonWings.setRotationAngles(scale, limbSwing, limbSwingAmount, ageInTicks, HeadYaw, headPitch, player);
            GL11.glPopMatrix();
        }
    }
    
    public void setRotationAngles(final float f, final float f1, final float f2, final float f3, final float f4, final float f5, final Entity entity) {
    }
    
    private class ModelDragonWings extends CosmeticModelBase
    {
        public ModelDragonWings(final RenderPlayer player) {
            super(player);
        }
        
        @Override
        public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
            super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            if (entityIn.getName().equals(Minecraft.getMinecraft().getSession().getUsername())) {
                GlStateManager.pushMatrix();
                float f1 = 0.0f;
                if (Minecraft.getMinecraft().thePlayer.capabilities.isFlying) {
                    f1 = ageInTicks / 200.0f;
                }
                else {
                    f1 = ageInTicks / 80.0f;
                }
                Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/seaclient/wings/wings2.png"));
                float anSpeed = 100.0f;
                if (!entityIn.onGround || CosmeticDragonWings2.this.flying) {
                    anSpeed = 50.0f;
                    CosmeticDragonWings2.this.flying = true;
                }
                GlStateManager.scale(0.15, 0.15, 0.15);
                GlStateManager.translate(0.0, -0.3, 1.1);
                GlStateManager.rotate(50.0f, -50.0f, 0.0f, 0.0f);
                final int x = 0;
                final int index = 0;
                for (int i = 0; i < 2; ++i) {
                    final float f2 = f1 * 9.141593f * 2.0f;
                    CosmeticDragonWings2.wing.rotateAngleX = 0.125f - (float)Math.cos(f2) * 0.2f;
                    CosmeticDragonWings2.wing.rotateAngleY = 0.25f;
                    CosmeticDragonWings2.wing.rotateAngleZ = (float)(Math.sin(f2) + 1.225) * 0.3f;
                    CosmeticDragonWings2.wingTip.rotateAngleZ = -(float)(Math.sin(f2 + 2.0f) + 0.5) * 0.75f;
                    CosmeticDragonWings2.wing.isHidden = false;
                    CosmeticDragonWings2.wingTip.isHidden = false;
                    if (!entityIn.isInvisible()) {
                        GlStateManager.pushMatrix();
                        GlStateManager.disableLighting();
                        CosmeticDragonWings2.wing.render(scale);
                        GlStateManager.blendFunc(1, 1);
                        GlStateManager.enableLighting();
                        GlStateManager.popMatrix();
                    }
                    CosmeticDragonWings2.wing.isHidden = false;
                    CosmeticDragonWings2.wingTip.isHidden = false;
                    if (i == 0) {
                        GlStateManager.scale(-1.0f, 1.0f, 1.0f);
                    }
                }
                GlStateManager.popMatrix();
            }
        }
    }
}
