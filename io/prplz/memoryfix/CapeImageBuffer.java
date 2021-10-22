// 
// Decompiled by Procyon v0.5.36
// 

package io.prplz.memoryfix;

import java.awt.image.BufferedImage;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.entity.AbstractClientPlayer;
import java.lang.ref.WeakReference;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.IImageBuffer;

public class CapeImageBuffer implements IImageBuffer
{
    public ImageBufferDownload imageBufferDownload;
    public final WeakReference<AbstractClientPlayer> playerRef;
    public final ResourceLocation resourceLocation;
    
    public CapeImageBuffer(final AbstractClientPlayer player, final ResourceLocation resourceLocation) {
        this.playerRef = new WeakReference<AbstractClientPlayer>(player);
        this.resourceLocation = resourceLocation;
        this.imageBufferDownload = new ImageBufferDownload();
    }
    
    public BufferedImage func_78432_a(final BufferedImage image) {
        return parseCape(image);
    }
    
    private static BufferedImage parseCape(final BufferedImage image) {
        return null;
    }
    
    public void func_152634_a() {
        final AbstractClientPlayer player = this.playerRef.get();
        if (player != null) {
            setLocationOfCape(player, this.resourceLocation);
        }
    }
    
    private static void setLocationOfCape(final AbstractClientPlayer player, final ResourceLocation resourceLocation) {
    }
}
