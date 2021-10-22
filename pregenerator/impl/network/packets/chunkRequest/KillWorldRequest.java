// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.network.packets.chunkRequest;

import java.util.List;
import java.util.Iterator;
import pregenerator.impl.tracking.WorldTracker;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.Entity;
import java.util.Collection;
import pregenerator.impl.tracking.CollectorMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.command.ICommandSender;
import pregenerator.impl.tracking.ServerTracker;
import net.minecraft.server.MinecraftServer;
import pregenerator.ChunkPregenerator;
import net.minecraft.entity.player.EntityPlayer;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.impl.tracking.ChunkEntry;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.PregenPacket;

public class KillWorldRequest extends PregenPacket
{
    int dim;
    String type;
    boolean tiles;
    
    public KillWorldRequest() {
    }
    
    public KillWorldRequest(final int dim, final String s, final boolean tile) {
        this.dim = dim;
        this.type = s;
        this.tiles = tile;
    }
    
    @Override
    public void read(final IReadableBuffer buffer) {
        this.tiles = buffer.readBoolean();
        this.dim = buffer.readInt();
        this.type = ChunkEntry.readString(buffer);
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        buffer.writeBoolean(this.tiles);
        buffer.writeInt(this.dim);
        ChunkEntry.writeString(this.type, buffer);
    }
    
    @Override
    public void handle(final EntityPlayer player) {
        final MinecraftServer server = ChunkPregenerator.getServer();
        if (!server.func_152345_ab()) {
            server.func_152344_a((Runnable)new Runnable() {
                @Override
                public void run() {
                    KillWorldRequest.this.process(player);
                }
            });
            return;
        }
        this.process(player);
    }
    
    public void process(final EntityPlayer player) {
        final WorldTracker tracker = ServerTracker.INSTANCE.getWorld(this.dim);
        if (tracker == null) {
            ChunkPregenerator.pregenBase.sendChatMessage(player, "Dimension [" + this.dim + "] not loaded on the Server");
            return;
        }
        if (this.tiles) {
            final Class<? extends TileEntity> clz = ChunkEntry.idToClass.get(this.type);
            if (clz == null) {
                ChunkPregenerator.pregenBase.sendChatMessage(player, "TileEntity [" + this.type + "] doesn't exist on the Server");
                return;
            }
            final CollectorMap<TileEntity> map = new CollectorMap<TileEntity>();
            map.addAll(tracker.getTileList());
            int removed = 0;
            for (final TileEntity tile : map.getAllOfType(clz)) {
                tracker.world.func_175698_g(tile.func_174877_v());
                ++removed;
            }
            ChunkPregenerator.pregenBase.sendChatMessage(player, "Removed " + removed + " TileEntities of type [" + this.type + "]");
        }
        else {
            final Class<? extends Entity> clz2 = EntityList.field_75625_b.get(this.type);
            if (clz2 == null) {
                ChunkPregenerator.pregenBase.sendChatMessage(player, "Entity [" + this.type + "] doesn't exist on the Server");
                return;
            }
            final CollectorMap<Entity> map2 = new CollectorMap<Entity>();
            map2.addAll(tracker.getEntityList());
            final List<Entity> list = map2.getAllOfType(clz2);
            for (final Entity e : list) {
                e.func_70106_y();
            }
            ChunkPregenerator.pregenBase.sendChatMessage(player, "Removed " + list.size() + " Entities of type [" + this.type + "]");
        }
    }
}
