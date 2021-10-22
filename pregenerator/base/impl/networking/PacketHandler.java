// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.base.impl.networking;

import pregenerator.ChunkPregenerator;
import net.minecraft.network.NetHandlerPlayServer;
import io.netty.util.AttributeKey;
import net.minecraft.network.INetHandler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.relauncher.Side;
import java.util.EnumMap;
import io.netty.channel.ChannelHandler;
import pregenerator.base.api.network.INetworkManager;
import pregenerator.base.api.network.PregenPacket;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class PacketHandler extends SimpleChannelInboundHandler<PregenPacket> implements INetworkManager
{
    private EnumMap<Side, FMLEmbeddedChannel> channel;
    pregenerator.base.impl.networking.ChannelHandler manager;
    
    @Override
    public INetworkManager init() {
        this.manager = new pregenerator.base.impl.networking.ChannelHandler();
        this.channel = (EnumMap<Side, FMLEmbeddedChannel>)NetworkRegistry.INSTANCE.newChannel("chunkpregenerator", new ChannelHandler[] { (ChannelHandler)this.manager, this });
        return this;
    }
    
    @Override
    public void registerPacket(final Class<? extends PregenPacket> packet) {
        this.manager.registerPacket(packet);
    }
    
    @Override
    public void sendPacketToServer(final PregenPacket packet) {
        this.channel.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        this.channel.get(Side.CLIENT).writeOutbound(new Object[] { packet });
    }
    
    @Override
    public void sendPacketToPlayer(final PregenPacket packet, final EntityPlayer player) {
        this.channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        this.channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        this.channel.get(Side.SERVER).writeOutbound(new Object[] { packet });
    }
    
    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final PregenPacket msg) throws Exception {
        final INetHandler netHandler = ctx.channel().attr((AttributeKey<INetHandler>)NetworkRegistry.NET_HANDLER).get();
        if (netHandler == null) {
            throw new RuntimeException("No nethandler found: " + ctx);
        }
        msg.handle(this.getPlayer(netHandler));
    }
    
    public EntityPlayer getPlayer(final INetHandler handler) {
        if (handler instanceof NetHandlerPlayServer) {
            return ((NetHandlerPlayServer)handler).field_147369_b;
        }
        return ChunkPregenerator.proxy.getClientPlayer();
    }
}
