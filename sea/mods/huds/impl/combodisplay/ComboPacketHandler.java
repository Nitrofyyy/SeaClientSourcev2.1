// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.huds.impl.combodisplay;

import net.minecraft.network.play.server.S19PacketEntityStatus;
import io.netty.channel.ChannelHandlerContext;
import sea.mods.huds.impl.ComboDisplay;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ComboPacketHandler extends ChannelInboundHandlerAdapter
{
    private final ComboDisplay comboDisplayMod;
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        if (msg instanceof S19PacketEntityStatus) {
            this.comboDisplayMod.onEntityStatusPacket((S19PacketEntityStatus)msg);
        }
        super.channelRead(ctx, msg);
    }
    
    public ComboPacketHandler(final ComboDisplay comboDisplayMod) {
        this.comboDisplayMod = comboDisplayMod;
    }
}
