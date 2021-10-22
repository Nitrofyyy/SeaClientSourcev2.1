// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.network.packets.chunkRequest;

import java.util.List;
import pregenerator.impl.tracking.WorldTracker;
import pregenerator.impl.tracking.ChunkEntry;
import net.minecraft.world.chunk.Chunk;
import java.util.ArrayList;
import pregenerator.impl.tracking.ServerTracker;
import net.minecraft.server.MinecraftServer;
import pregenerator.ChunkPregenerator;
import net.minecraft.entity.player.EntityPlayer;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.PregenPacket;

public class ChunkRequest extends PregenPacket
{
    int currentDim;
    
    public ChunkRequest(final int dim) {
        this.currentDim = dim;
    }
    
    public ChunkRequest() {
    }
    
    @Override
    public void read(final IReadableBuffer buffer) {
        this.currentDim = buffer.readInt();
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        buffer.writeInt(this.currentDim);
    }
    
    @Override
    public void handle(final EntityPlayer player) {
        final MinecraftServer server = ChunkPregenerator.getServer();
        if (!server.func_152345_ab()) {
            server.func_152344_a((Runnable)new Runnable() {
                @Override
                public void run() {
                    ChunkRequest.this.process(player);
                }
            });
            return;
        }
        this.process(player);
    }
    
    public void process(final EntityPlayer player) {
        final WorldTracker tracker = ServerTracker.INSTANCE.getWorld(this.currentDim);
        if (tracker == null) {
            final ChunkAnswerPacket answer = new ChunkAnswerPacket();
            answer.setType(-1);
            ChunkPregenerator.networking.sendPacketToPlayer(answer, player);
            return;
        }
        final List<Chunk> chunks = tracker.getChunks();
        if (chunks.isEmpty()) {
            final ChunkAnswerPacket answer2 = new ChunkAnswerPacket();
            answer2.setType(-1);
            ChunkPregenerator.networking.sendPacketToPlayer(answer2, player);
            return;
        }
        final List<ChunkAnswerPacket> toSend = new ArrayList<ChunkAnswerPacket>();
        ChunkAnswerPacket current = new ChunkAnswerPacket();
        int bytesLeft = 30000;
        for (int i = 0; i < chunks.size(); ++i) {
            final ChunkEntry entry = ChunkEntry.fromChunk(chunks.get(i));
            if (entry.getBytes() > bytesLeft) {
                toSend.add(current);
                current = new ChunkAnswerPacket();
                bytesLeft = 30000;
            }
            current.addChunkEntry(entry);
            bytesLeft -= entry.getBytes();
        }
        if (current.hasData()) {
            toSend.add(current);
        }
        for (int i = 0; i < toSend.size(); ++i) {
            final ChunkAnswerPacket packet = toSend.get(i);
            packet.setType(toSend.size() - (i + 1));
            ChunkPregenerator.networking.sendPacketToPlayer(packet, player);
        }
    }
}
