// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.network.packets.chunkRequest;

import java.util.Iterator;
import java.util.List;
import pregenerator.impl.tracking.WorldTracker;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.Entity;
import pregenerator.impl.tracking.ChunkEntry;
import java.util.Set;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.tileentity.TileEntity;
import pregenerator.impl.tracking.CollectorMap;
import pregenerator.impl.tracking.ServerTracker;
import net.minecraft.server.MinecraftServer;
import pregenerator.ChunkPregenerator;
import net.minecraft.entity.player.EntityPlayer;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.PregenPacket;

public class EntityRequestPacket extends PregenPacket
{
    int dim;
    boolean tiles;
    
    public EntityRequestPacket() {
    }
    
    public EntityRequestPacket(final int dim, final boolean tiles) {
        this.dim = dim;
        this.tiles = tiles;
    }
    
    @Override
    public void read(final IReadableBuffer buffer) {
        this.dim = buffer.readInt();
        this.tiles = buffer.readBoolean();
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        buffer.writeInt(this.dim);
        buffer.writeBoolean(this.tiles);
    }
    
    @Override
    public void handle(final EntityPlayer player) {
        final MinecraftServer server = ChunkPregenerator.getServer();
        if (!server.func_152345_ab()) {
            server.func_152344_a((Runnable)new Runnable() {
                @Override
                public void run() {
                    EntityRequestPacket.this.process(player);
                }
            });
            return;
        }
        this.process(player);
    }
    
    public void process(final EntityPlayer player) {
        final WorldTracker tracker = ServerTracker.INSTANCE.getWorld(this.dim);
        if (tracker == null) {
            final EntityAnswerPacket answer = new EntityAnswerPacket(this.tiles);
            answer.setType(-1);
            ChunkPregenerator.networking.sendPacketToPlayer(answer, player);
            return;
        }
        if (this.tiles) {
            final List<TileEntity> tileList = tracker.getTileList();
            if (tileList.isEmpty()) {
                final EntityAnswerPacket answer2 = new EntityAnswerPacket(this.tiles);
                answer2.setType(-1);
                ChunkPregenerator.networking.sendPacketToPlayer(answer2, player);
                return;
            }
            final CollectorMap<TileEntity> collect = new CollectorMap<TileEntity>();
            collect.addAll(tileList);
            final List<EntityAnswerPacket> packets = new ArrayList<EntityAnswerPacket>();
            EntityAnswerPacket current = new EntityAnswerPacket(this.tiles);
            int bytesLeft = 30000;
            for (final Map.Entry<Class<TileEntity>, Set<TileEntity>> entry : collect.entrySet()) {
                final String s = ChunkEntry.classToID.get(entry.getKey());
                final int value = entry.getValue().size();
                if (s == null) {
                    continue;
                }
                final int bytes = 8 + s.length() * 2;
                if (bytes >= bytesLeft) {
                    packets.add(current);
                    current = new EntityAnswerPacket(this.tiles);
                    bytesLeft = 30000;
                }
                current.addEntry(s, value);
                bytesLeft -= bytes;
            }
            if (current.hasData()) {
                packets.add(current);
            }
            for (int i = 0; i < packets.size(); ++i) {
                final EntityAnswerPacket packet = packets.get(i);
                packet.setType(packets.size() - (i + 1));
                ChunkPregenerator.networking.sendPacketToPlayer(packet, player);
            }
        }
        else {
            final List<Entity> tileList2 = tracker.getEntityList();
            if (tileList2.isEmpty()) {
                final EntityAnswerPacket answer2 = new EntityAnswerPacket(this.tiles);
                answer2.setType(-1);
                ChunkPregenerator.networking.sendPacketToPlayer(answer2, player);
                return;
            }
            final CollectorMap<Entity> collect2 = new CollectorMap<Entity>();
            collect2.addAll(tileList2);
            collect2.removeAll(EntityPlayer.class);
            if (collect2.isEmpty()) {
                final EntityAnswerPacket answer3 = new EntityAnswerPacket(this.tiles);
                answer3.setType(-1);
                ChunkPregenerator.networking.sendPacketToPlayer(answer3, player);
                return;
            }
            final List<EntityAnswerPacket> packets = new ArrayList<EntityAnswerPacket>();
            EntityAnswerPacket current = new EntityAnswerPacket(this.tiles);
            int bytesLeft = 30000;
            for (final Map.Entry<Class<Entity>, Set<Entity>> entry2 : collect2.entrySet()) {
                final String s = EntityList.field_75626_c.get(entry2.getKey());
                final int value = entry2.getValue().size();
                if (s == null) {
                    continue;
                }
                final int bytes = 8 + s.length() * 2;
                if (bytes >= bytesLeft) {
                    packets.add(current);
                    current = new EntityAnswerPacket(this.tiles);
                    bytesLeft = 30000;
                }
                current.addEntry(s, value);
                bytesLeft -= bytes;
            }
            if (current.hasData()) {
                packets.add(current);
            }
            for (int i = 0; i < packets.size(); ++i) {
                final EntityAnswerPacket packet = packets.get(i);
                packet.setType(packets.size() - (i + 1));
                ChunkPregenerator.networking.sendPacketToPlayer(packet, player);
            }
        }
    }
}
