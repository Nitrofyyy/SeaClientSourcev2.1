// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.network.packets.retrogen;

import pregenerator.impl.retrogen.RetrogenHandler;
import net.minecraft.command.ICommandSender;
import pregenerator.impl.processor.generator.ChunkProcessor;
import net.minecraft.server.MinecraftServer;
import pregenerator.ChunkPregenerator;
import net.minecraft.entity.player.EntityPlayer;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.PregenPacket;

public class RetrogenChangePacket extends PregenPacket
{
    String id;
    boolean newState;
    boolean client;
    
    public RetrogenChangePacket() {
    }
    
    public RetrogenChangePacket(final String s, final boolean state) {
        this.id = s;
        this.newState = state;
    }
    
    public void setClient() {
        this.client = true;
    }
    
    @Override
    public void read(final IReadableBuffer buffer) {
        this.newState = buffer.readBoolean();
        this.client = buffer.readBoolean();
        final StringBuilder builder = new StringBuilder();
        for (int size = buffer.readInt(), i = 0; i < size; ++i) {
            builder.append(buffer.readChar());
        }
        this.id = builder.toString();
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        buffer.writeBoolean(this.newState);
        buffer.writeBoolean(this.client);
        buffer.writeInt(this.id.length());
        for (int i = 0; i < this.id.length(); ++i) {
            buffer.writeChar(this.id.charAt(i));
        }
    }
    
    @Override
    public void handle(final EntityPlayer player) {
        final MinecraftServer server = ChunkPregenerator.getServer();
        if (!server.func_152345_ab()) {
            server.func_152344_a((Runnable)new Runnable() {
                @Override
                public void run() {
                    RetrogenChangePacket.this.process(player);
                }
            });
            return;
        }
        this.process(player);
    }
    
    public void process(final EntityPlayer player) {
        if (!this.client && ChunkProcessor.INSTANCE.isRunning()) {
            ChunkPregenerator.pregenBase.sendChatMessage(player, "Pregenerator is running. You cant Edit the Retrogen midGeneration");
            final RetrogenChangePacket packet = new RetrogenChangePacket(this.id, !this.newState);
            packet.setClient();
            ChunkPregenerator.networking.sendPacketToPlayer(packet, player);
            return;
        }
        if (this.newState) {
            RetrogenHandler.INSTANCE.enableGenerator(this.id);
        }
        else {
            RetrogenHandler.INSTANCE.disableGenerator(this.id);
        }
    }
}
