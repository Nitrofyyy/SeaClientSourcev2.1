// 
// Decompiled by Procyon v0.5.36
// 

package sea.utils;

import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetworkManager;

public class FakeNetworkManager extends NetworkManager
{
    public FakeNetworkManager(final EnumPacketDirection packetDirection) {
        super(packetDirection);
    }
}
