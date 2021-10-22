// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.base.impl.networking;

import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.IWriteableBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import pregenerator.base.api.network.PregenPacket;
import net.minecraftforge.fml.common.network.FMLIndexedMessageToMessageCodec;

@io.netty.channel.ChannelHandler.Sharable
public class ChannelHandler extends FMLIndexedMessageToMessageCodec<PregenPacket>
{
    int counter;
    
    public ChannelHandler() {
        this.counter = 0;
    }
    
    public void registerPacket(final Class<? extends PregenPacket> packet) {
        this.addDiscriminator(this.counter, (Class)packet);
        ++this.counter;
    }
    
    public void encodeInto(final ChannelHandlerContext ctx, final PregenPacket msg, final ByteBuf target) throws Exception {
        try {
            msg.write(new WriteableBuffer(target));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void decodeInto(final ChannelHandlerContext ctx, final ByteBuf source, final PregenPacket msg) {
        try {
            msg.read(new ReadableBuffer(source));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
