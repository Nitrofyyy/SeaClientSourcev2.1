// 
// Decompiled by Procyon v0.5.36
// 

package sea.cosmetic.impl;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class Skine implements LayerRenderer<AbstractClientPlayer>
{
    private final RenderPlayer armyRenderer;
    private ModelBase armyModel;
    
    public Skine(final RenderPlayer renderPlayer) {
        this.armyModel = new ModelPlayer(0.0f, true);
        this.armyRenderer = renderPlayer;
        this.armyModel = renderPlayer.getMainModel();
    }
    
    @Override
    public void doRenderLayer(final AbstractClientPlayer entitylivingbaseIn, final float p_1771412111, final float p_1771413111, final float partialTicks, final float p_1771415111, final float p_1771416111, final float p_1771417111, final float scale) {
        if (entitylivingbaseIn.getName().equals("SeaClient") && entitylivingbaseIn.getName().equals("SeaClient")) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/entity/BLueDD.png"));
            GlStateManager.enableNormalize();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            this.armyModel.setModelAttributes(this.armyRenderer.getMainModel());
            this.armyModel.render(entitylivingbaseIn, p_1771412111, p_1771413111, p_1771415111, p_1771416111, p_1771417111, scale);
            GlStateManager.disableBlend();
            GlStateManager.disableNormalize();
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
