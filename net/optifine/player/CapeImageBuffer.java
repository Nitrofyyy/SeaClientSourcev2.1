// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.player;

import java.awt.image.BufferedImage;
import java.lang.ref.WeakReference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ImageBufferDownload;

public class CapeImageBuffer extends ImageBufferDownload
{
    private AbstractClientPlayer player;
    private ResourceLocation resourceLocation;
    private boolean elytraOfCape;
    public ImageBufferDownload imageBufferDownload;
    public final WeakReference playerRef;
    
    public CapeImageBuffer(final AbstractClientPlayer player, final ResourceLocation resourceLocation) {
        this.player = player;
        this.resourceLocation = resourceLocation;
        this.playerRef = new WeakReference((T)player);
        this.resourceLocation = resourceLocation;
        this.imageBufferDownload = new ImageBufferDownload();
    }
    
    @Override
    public BufferedImage parseUserSkin(final BufferedImage imageRaw) {
        return parseCape(imageRaw);
    }
    
    private static BufferedImage parseCape(final BufferedImage image) {
        return null;
    }
    
    @Override
    public void skinAvailable() {
        final AbstractClientPlayer player = (AbstractClientPlayer)this.playerRef.get();
        if (player != null) {
            setLocationOfCape(player, this.resourceLocation);
        }
    }
    
    private static void setLocationOfCape(final AbstractClientPlayer player, final ResourceLocation resourceLocation) {
    }
    
    public void cleanup() {
        this.player = null;
    }
    
    public boolean isElytraOfCape() {
        return this.elytraOfCape;
    }
}
