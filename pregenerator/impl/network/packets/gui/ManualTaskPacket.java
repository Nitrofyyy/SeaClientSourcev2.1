// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.network.packets.gui;

import pregenerator.impl.storage.TaskStorage;
import net.minecraft.command.ICommandSender;
import pregenerator.impl.processor.generator.ChunkProcessor;
import pregenerator.impl.processor.deleter.DeleteProcessor;
import net.minecraft.server.MinecraftServer;
import pregenerator.ChunkPregenerator;
import net.minecraft.entity.player.EntityPlayer;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.PregenPacket;

public class ManualTaskPacket extends PregenPacket
{
    int mod;
    
    public ManualTaskPacket() {
    }
    
    public ManualTaskPacket(final int mod) {
        this.mod = mod;
    }
    
    @Override
    public void read(final IReadableBuffer buffer) {
        this.mod = buffer.readByte();
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        buffer.writeByte(this.mod);
    }
    
    @Override
    public void handle(final EntityPlayer player) {
        final MinecraftServer server = ChunkPregenerator.getServer();
        if (!server.func_152345_ab()) {
            server.func_152344_a((Runnable)new Runnable() {
                @Override
                public void run() {
                    ManualTaskPacket.this.handleServer(player);
                }
            });
            return;
        }
        this.handleServer(player);
    }
    
    public void handleServer(final EntityPlayer player) {
        if (this.mod == 0) {
            DeleteProcessor.INSTANCE.interruptTask();
            ChunkProcessor.INSTANCE.interruptTask(false);
            ChunkPregenerator.pregenBase.sendChatMessage(player, "Stopped Processors");
            ChunkPregenerator.networking.sendPacketToPlayer(new ProcessAnswerPacket(false, TaskStorage.getStorage().hasTasks()), player);
        }
        else if (this.mod == 1) {
            DeleteProcessor.INSTANCE.interruptTask();
            ChunkProcessor.INSTANCE.interruptTask(false);
            TaskStorage.getStorage().clearAll();
            ChunkPregenerator.pregenBase.sendChatMessage(player, "Stopped Processors & Cleared Tasks");
            ChunkPregenerator.networking.sendPacketToPlayer(new ProcessAnswerPacket(false, false), player);
        }
        else {
            final TaskStorage storage = TaskStorage.getStorage();
            if (storage.hasTasks()) {
                ChunkProcessor.INSTANCE.startTask(storage.getNextTask());
                ChunkPregenerator.pregenBase.sendChatMessage(player, "Started Pregenerator");
                ChunkPregenerator.networking.sendPacketToPlayer(new ProcessAnswerPacket(true, true), player);
            }
            else {
                ChunkPregenerator.pregenBase.sendChatMessage(player, "No Tasks Aviable");
                ChunkPregenerator.networking.sendPacketToPlayer(new ProcessAnswerPacket(false, false), player);
            }
        }
    }
}
