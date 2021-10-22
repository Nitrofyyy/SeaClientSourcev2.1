// 
// Decompiled by Procyon v0.5.36
// 

package io.prplz.memoryfix;

import java.io.IOException;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ResourcePackImageScaler
{
    public static final int SIZE = 64;
    
    public static BufferedImage scalePackImage(final BufferedImage image) throws IOException {
        if (image == null) {
            return null;
        }
        System.out.println("Scaling resource pack icon from " + image.getWidth() + " to " + 64);
        final BufferedImage smallImage = new BufferedImage(64, 64, 2);
        final Graphics graphics = smallImage.getGraphics();
        graphics.drawImage(image, 0, 0, 64, 64, null);
        graphics.dispose();
        return smallImage;
    }
}
