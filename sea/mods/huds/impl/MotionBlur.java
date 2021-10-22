// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.huds.impl;

import sea.event.EventTarget;
import net.minecraft.client.renderer.EntityRenderer;
import sea.mods.huds.impl.motionblur.MotionBlurResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import sea.event.impl.ClientTickEvent;
import java.util.Map;
import java.lang.reflect.Field;
import sea.mods.huds.HudMod;

public class MotionBlur extends HudMod
{
    public double blurAmount;
    public final boolean motionBlur;
    private Field cachedFastRender;
    private int ticks;
    private final Map domainResourceManagers;
    private ClientTickEvent event;
    
    public MotionBlur() {
        super("MotionBlur", 0, 0, "Turn off FastRenderer");
        this.motionBlur = this.isEnabled();
        this.domainResourceManagers = ((SimpleReloadableResourceManager)this.mc.getResourceManager()).domainResourceManagers;
    }
    
    @Override
    public void draw() {
        this.tick(this.event);
        super.draw();
    }
    
    @Override
    public int getWidth() {
        return 10000;
    }
    
    @Override
    public int getHieght() {
        return 10000;
    }
    
    @EventTarget
    public void tick(final ClientTickEvent event) {
        final double amount = 0.7 + this.blurAmount / 100.0 * 3.0 - 0.01;
        if (this.isEnabled() && this.mc.thePlayer != null) {
            final EntityRenderer entityRenderer = this.mc.entityRenderer;
            if (entityRenderer == null || entityRenderer.isShaderActive()) {
                return;
            }
            this.mc.entityRenderer.loadShader(new ResourceLocation("motionblur", "motionblur"));
        }
        try {
            this.cachedFastRender = GameSettings.class.getDeclaredField("ofFastRender");
        }
        catch (Exception ex) {}
        if (this.domainResourceManagers != null && !this.domainResourceManagers.containsKey("motionblur")) {
            this.domainResourceManagers.put("motionblur", new MotionBlurResourceManager(this.mc.metadataSerializer_));
        }
        ++this.ticks;
        if (this.ticks % 5000 == 0 && this.isEnabled() && this.mc.thePlayer != null && this.mc.theWorld != null) {
            System.out.println("[MotionBlur] Motion Blur is not compatible with OptiFine's Fast Render.");
        }
    }
    
    public boolean isFastRenderEnabled() {
        try {
            return this.cachedFastRender.getBoolean(this.mc.gameSettings);
        }
        catch (Exception exception) {
            return false;
        }
    }
}
