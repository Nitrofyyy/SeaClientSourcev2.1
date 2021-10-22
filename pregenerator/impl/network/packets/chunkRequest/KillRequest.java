// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.network.packets.chunkRequest;

import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.World;
import java.util.Map;
import net.minecraft.util.BlockPos;
import java.util.LinkedHashMap;
import pregenerator.impl.tracking.ChunkEntry;
import net.minecraft.tileentity.TileEntity;
import pregenerator.impl.tracking.CollectorMap;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.Entity;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.common.DimensionManager;
import net.minecraft.server.MinecraftServer;
import pregenerator.ChunkPregenerator;
import net.minecraft.entity.player.EntityPlayer;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.impl.misc.FilePos;
import pregenerator.base.api.network.PregenPacket;

public class KillRequest extends PregenPacket
{
    int dim;
    int type;
    int x;
    int z;
    String id;
    
    public KillRequest() {
    }
    
    public KillRequest(final int dimension, final FilePos pos, final int type, final String id) {
        this.dim = dimension;
        this.type = type;
        this.x = pos.x;
        this.z = pos.z;
        this.id = id;
    }
    
    @Override
    public void read(final IReadableBuffer buffer) {
        this.dim = buffer.readInt();
        this.type = buffer.readByte();
        this.x = buffer.readInt();
        this.z = buffer.readInt();
        final StringBuilder builder = new StringBuilder();
        for (int expected = buffer.readShort(), i = 0; i < expected; ++i) {
            builder.append(buffer.readChar());
        }
        this.id = builder.toString();
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        buffer.writeInt(this.dim);
        buffer.writeByte(this.type);
        buffer.writeInt(this.x);
        buffer.writeInt(this.z);
        buffer.writeShort(this.id.length());
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
                    KillRequest.this.process(player);
                }
            });
            return;
        }
        this.process(player);
    }
    
    public void process(final EntityPlayer player) {
        final World world = DimensionManager.getWorld(this.dim);
        if (world == null) {
            ChunkPregenerator.pregenBase.sendChatMessage(player, "Dimension [" + this.dim + "] is unlaoded. Kill Request can't be done");
            return;
        }
        if (this.type == 0) {
            final Class<? extends Entity> clz = EntityList.field_75625_b.get(this.id);
            if (clz == null) {
                ChunkPregenerator.pregenBase.sendChatMessage(player, "Entity ID [" + this.id + "] doesn't have a Entity Bound to it on the Server!");
                return;
            }
            final Chunk chunk = world.func_72964_e(this.x, this.z);
            final CollectorMap<Entity> entity = new CollectorMap<Entity>();
            for (final Collection<Entity> collect : chunk.func_177429_s()) {
                entity.addAll(collect);
            }
            final List<Entity> list = entity.getAllOfType(clz);
            for (final Entity entry : list) {
                entry.func_70106_y();
            }
            ChunkPregenerator.pregenBase.sendChatMessage(player, "Killed " + list.size() + " Entities of type [" + this.id + "]");
        }
        else if (this.type == 1) {
            final Class<? extends TileEntity> clz2 = ChunkEntry.idToClass.get(this.id);
            if (clz2 == null) {
                ChunkPregenerator.pregenBase.sendChatMessage(player, "TileEntity ID [" + this.id + "] doesn't have a TileEntity Bound to it on the Server!");
                return;
            }
            final Chunk chunk = world.func_72964_e(this.x, this.z);
            int deleted = 0;
            for (final Map.Entry<BlockPos, TileEntity> entry2 : new LinkedHashMap<BlockPos, TileEntity>(chunk.func_177434_r()).entrySet()) {
                if (clz2.isInstance(entry2.getValue())) {
                    world.func_175698_g((BlockPos)entry2.getKey());
                    ++deleted;
                }
            }
            ChunkPregenerator.pregenBase.sendChatMessage(player, "Removed " + deleted + " TileEntities of type [" + this.id + "]");
        }
    }
}
