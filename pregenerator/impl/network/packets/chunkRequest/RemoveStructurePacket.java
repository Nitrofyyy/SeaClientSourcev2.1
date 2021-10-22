// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.network.packets.chunkRequest;

import pregenerator.impl.misc.Tuple;
import pregenerator.impl.structure.MapGenStructureDataPregen;
import pregenerator.impl.processor.deleter.IDeletionTask;
import pregenerator.impl.command.structure.DeleteStructureSubCommand;
import pregenerator.impl.structure.StructureManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.ChunkPregenerator;
import net.minecraft.entity.player.EntityPlayer;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.impl.structure.StructureData;
import pregenerator.impl.misc.FilePos;
import pregenerator.base.api.network.PregenPacket;

public class RemoveStructurePacket extends PregenPacket
{
    int dim;
    FilePos pos;
    String type;
    boolean doRemove;
    
    public RemoveStructurePacket() {
    }
    
    public RemoveStructurePacket(final int dim, final StructureData data, final boolean world) {
        this.dim = dim;
        this.pos = data.getPos();
        this.type = data.getType();
        this.doRemove = world;
    }
    
    @Override
    public void read(final IReadableBuffer buffer) {
        this.dim = buffer.readInt();
        this.doRemove = buffer.readBoolean();
        final int x = buffer.readInt();
        final int z = buffer.readInt();
        this.pos = new FilePos(x, z);
        final int size = buffer.readInt();
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; ++i) {
            builder.append(buffer.readChar());
        }
        this.type = builder.toString();
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        buffer.writeInt(this.dim);
        buffer.writeBoolean(this.doRemove);
        buffer.writeInt(this.pos.x);
        buffer.writeInt(this.pos.z);
        buffer.writeInt(this.type.length());
        for (int i = 0; i < this.type.length(); ++i) {
            buffer.writeChar(this.type.charAt(i));
        }
    }
    
    @Override
    public void handle(final EntityPlayer player) {
        final MinecraftServer server = ChunkPregenerator.getServer();
        final CommandContainer container = new CommandContainer(server, player);
        if (!server.func_152345_ab()) {
            server.func_152344_a((Runnable)new Runnable() {
                @Override
                public void run() {
                    RemoveStructurePacket.this.process(container);
                }
            });
            return;
        }
        this.process(container);
    }
    
    public void process(final CommandContainer container) {
        final MapGenStructureDataPregen structure = StructureManager.instance.getStructure(this.dim, this.type);
        if (structure == null) {
            container.sendChatMessage("Structure Manager for " + this.type + " not Found!");
            return;
        }
        final Tuple<FilePos, FilePos> result = structure.deleteStructure(this.pos);
        if (result != null) {
            final FilePos resultPos = result.getFirst();
            container.sendChatMessage("Deleted Structure at: [" + resultPos + "]");
            if (this.doRemove) {
                final FilePos resultEnd = result.getSecond();
                container.getDeleter().startTask(new DeleteStructureSubCommand.RemoveStructure(this.dim, resultPos, resultEnd));
                container.sendChatMessage("Structure Deletion Process starts in 5 seconds.");
            }
        }
        else {
            container.sendChatMessage("Structure not found nearby: [" + this.pos + "]");
        }
    }
}
