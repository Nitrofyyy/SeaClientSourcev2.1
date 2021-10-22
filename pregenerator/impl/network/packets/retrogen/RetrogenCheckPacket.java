// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.network.packets.retrogen;

import pregenerator.impl.retrogen.RetrogenHandler;
import net.minecraft.server.MinecraftServer;
import pregenerator.ChunkPregenerator;
import net.minecraft.entity.player.EntityPlayer;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.PregenPacket;

public class RetrogenCheckPacket extends PregenPacket
{
    @Override
    public void read(final IReadableBuffer buffer) {
        buffer.readBoolean();
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        buffer.writeBoolean(true);
    }
    
    @Override
    public void handle(final EntityPlayer player) {
        final MinecraftServer server = ChunkPregenerator.getServer();
        if (!server.func_152345_ab()) {
            server.func_152344_a((Runnable)new Runnable() {
                @Override
                public void run() {
                    RetrogenCheckPacket.this.process(player);
                }
            });
            return;
        }
        this.process(player);
    }
    
    public void process(final EntityPlayer player) {
        ChunkPregenerator.networking.sendPacketToPlayer(new RetrogenCheckAnswerPacket(RetrogenHandler.INSTANCE.getActiveGenerators()), player);
    }
}
