// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.network.packets.gui;

import java.util.List;
import pregenerator.impl.processor.generator.ChunkProcessor;
import pregenerator.impl.storage.PregenTask;
import net.minecraft.command.ICommandSender;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.gen.StartMassRadiusSubCommand;
import pregenerator.impl.misc.FilePos;
import net.minecraft.server.MinecraftServer;
import pregenerator.ChunkPregenerator;
import net.minecraft.entity.player.EntityPlayer;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.PregenPacket;

public class MassPregenTaskPacket extends PregenPacket
{
    int[] data;
    
    public MassPregenTaskPacket() {
        this.data = new int[7];
    }
    
    public MassPregenTaskPacket(final int shape, final int dimension, final int centerX, final int centerZ, final int radius, final int split, final int genType) {
        (this.data = new int[7])[0] = shape;
        this.data[1] = dimension;
        this.data[2] = centerX;
        this.data[3] = centerZ;
        this.data[4] = radius;
        this.data[5] = split;
        this.data[6] = genType;
    }
    
    @Override
    public void read(final IReadableBuffer buffer) {
        for (int i = 0; i < 7; ++i) {
            this.data[i] = buffer.readInt();
        }
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        for (int i = 0; i < 7; ++i) {
            buffer.writeInt(this.data[i]);
        }
    }
    
    @Override
    public void handle(final EntityPlayer player) {
        final MinecraftServer server = ChunkPregenerator.getServer();
        if (!server.func_152345_ab()) {
            server.func_152344_a((Runnable)new Runnable() {
                @Override
                public void run() {
                    MassPregenTaskPacket.this.handleServer(player);
                }
            });
            return;
        }
        this.handleServer(player);
    }
    
    public void handleServer(final EntityPlayer player) {
        final List<PregenTask> tasks = StartMassRadiusSubCommand.createTaskList(this.data[0], new FilePos(this.data[2], this.data[3]), this.data[4], this.data[1], this.data[6], this.data[5]);
        final CommandContainer container = new CommandContainer(ChunkPregenerator.getServer(), player);
        if (tasks.isEmpty()) {
            container.sendChatMessage("No Tasks were created");
            return;
        }
        if (container.onProcessStarted(tasks.get(0))) {
            container.sendChatMessage("Pregenerator already running. Adding Task(s) to the TaskStorage");
        }
        else {
            ChunkProcessor.INSTANCE.startTask(tasks.get(0));
        }
        container.getStorage().savePregenTasks(tasks);
    }
}
