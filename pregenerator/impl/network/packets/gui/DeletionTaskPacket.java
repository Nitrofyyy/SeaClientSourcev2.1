// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.network.packets.gui;

import pregenerator.impl.command.delete.DeleteRadiusSubCommand;
import pregenerator.impl.processor.deleter.IDeletionTask;
import pregenerator.impl.command.delete.DeleteExpansionSubCommand;
import pregenerator.impl.misc.FilePos;
import pregenerator.impl.processor.deleter.DeleteProcessor;
import net.minecraft.server.MinecraftServer;
import pregenerator.ChunkPregenerator;
import net.minecraft.entity.player.EntityPlayer;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.PregenPacket;

public class DeletionTaskPacket extends PregenPacket
{
    boolean expansion;
    int[] data;
    
    public DeletionTaskPacket() {
        this.data = new int[6];
    }
    
    public DeletionTaskPacket(final boolean exp, final int shape, final int dimension, final int centerX, final int centerZ, final int radius, final int maxRadius) {
        this.data = new int[6];
        this.expansion = exp;
        this.data[0] = shape;
        this.data[1] = dimension;
        this.data[2] = centerX;
        this.data[3] = centerZ;
        this.data[4] = radius;
        this.data[5] = maxRadius;
    }
    
    @Override
    public void read(final IReadableBuffer buffer) {
        this.expansion = buffer.readBoolean();
        for (int i = 0; i < 6; ++i) {
            this.data[i] = buffer.readInt();
        }
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        buffer.writeBoolean(this.expansion);
        for (int i = 0; i < 6; ++i) {
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
                    DeletionTaskPacket.this.handleServer(player);
                }
            });
            return;
        }
        this.handleServer(player);
    }
    
    public void handleServer(final EntityPlayer player) {
        if (this.expansion) {
            DeleteProcessor.INSTANCE.startTask(new DeleteExpansionSubCommand.ExpansionTask(this.data[0], this.data[1], new FilePos(this.data[2], this.data[3]), this.data[4], this.data[5]));
        }
        else {
            DeleteProcessor.INSTANCE.startTask(new DeleteRadiusSubCommand.RadiusTask(this.data[0], this.data[1], new FilePos(this.data[2], this.data[3]), this.data[4]));
        }
    }
}
