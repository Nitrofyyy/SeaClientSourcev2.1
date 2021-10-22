// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.base.api.network;

import net.minecraft.entity.player.EntityPlayer;

public interface INetworkManager
{
    INetworkManager init();
    
    void registerPacket(final Class<? extends PregenPacket> p0);
    
    void sendPacketToServer(final PregenPacket p0);
    
    void sendPacketToPlayer(final PregenPacket p0, final EntityPlayer p1);
}
