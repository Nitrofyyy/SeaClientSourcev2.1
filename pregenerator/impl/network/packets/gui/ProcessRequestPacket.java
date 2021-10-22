// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.network.packets.gui;

import pregenerator.ChunkPregenerator;
import pregenerator.impl.storage.TaskStorage;
import pregenerator.impl.processor.deleter.DeleteProcessor;
import pregenerator.impl.processor.generator.ChunkProcessor;
import net.minecraft.entity.player.EntityPlayer;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.PregenPacket;

public class ProcessRequestPacket extends PregenPacket
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
        final boolean running = ChunkProcessor.INSTANCE.isRunning() || DeleteProcessor.INSTANCE.isRunning();
        final boolean hasTasks = TaskStorage.getStorage().hasTasks();
        ChunkPregenerator.networking.sendPacketToPlayer(new ProcessAnswerPacket(running, hasTasks), player);
    }
}
