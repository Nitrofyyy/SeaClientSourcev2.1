// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.network.packets.gui;

import pregenerator.impl.processor.generator.ChunkProcessor;
import net.minecraft.command.ICommandSender;
import pregenerator.impl.command.base.CommandContainer;
import net.minecraft.server.MinecraftServer;
import pregenerator.ChunkPregenerator;
import net.minecraft.entity.player.EntityPlayer;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.impl.storage.PregenTask;
import pregenerator.base.api.network.PregenPacket;

public class PregenTaskPacket extends PregenPacket
{
    PregenTask task;
    
    public PregenTaskPacket() {
    }
    
    public PregenTaskPacket(final PregenTask task) {
        this.task = task;
    }
    
    @Override
    public void read(final IReadableBuffer buffer) {
        final int[] data = new int[buffer.readByte()];
        for (int i = 0; i < data.length; ++i) {
            data[i] = buffer.readInt();
        }
        this.task = new PregenTask(data);
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        final int[] data = this.task.save().func_150302_c();
        buffer.writeByte(data.length);
        for (int i = 0; i < data.length; ++i) {
            buffer.writeInt(data[i]);
        }
    }
    
    @Override
    public void handle(final EntityPlayer player) {
        final MinecraftServer server = ChunkPregenerator.getServer();
        if (!server.func_152345_ab()) {
            server.func_152344_a((Runnable)new Runnable() {
                @Override
                public void run() {
                    PregenTaskPacket.this.handleServer(player);
                }
            });
            return;
        }
        this.handleServer(player);
    }
    
    public void handleServer(final EntityPlayer player) {
        final CommandContainer container = new CommandContainer(ChunkPregenerator.getServer(), player);
        if (container.onProcessStarted(this.task)) {
            return;
        }
        ChunkProcessor.INSTANCE.startTask(this.task);
    }
}
