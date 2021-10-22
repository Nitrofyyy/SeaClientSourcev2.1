// 
// Decompiled by Procyon v0.5.36
// 

package sea;

import net.minecraft.util.ResourceLocation;
import net.minecraft.client.multiplayer.ServerData;

public class ServerDataFeatured extends ServerData
{
    public static final ResourceLocation STAR_ICON;
    
    static {
        STAR_ICON = new ResourceLocation("textures/seaclient/star.png");
    }
    
    public ServerDataFeatured(final String serverName, final String serverIp) {
        super(serverName, serverIp, false);
    }
}
