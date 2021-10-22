// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.network.packets.chunkRequest;

import java.util.Iterator;
import java.util.List;
import pregenerator.impl.structure.StructureData;
import java.util.ArrayList;
import pregenerator.impl.structure.StructureManager;
import net.minecraft.server.MinecraftServer;
import pregenerator.ChunkPregenerator;
import net.minecraft.entity.player.EntityPlayer;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.PregenPacket;

public class StructureRequestPacket extends PregenPacket
{
    int dimensionID;
    
    public StructureRequestPacket() {
    }
    
    public StructureRequestPacket(final int dim) {
        this.dimensionID = dim;
    }
    
    @Override
    public void read(final IReadableBuffer buffer) {
        this.dimensionID = buffer.readInt();
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        buffer.writeInt(this.dimensionID);
    }
    
    @Override
    public void handle(final EntityPlayer player) {
        final MinecraftServer server = ChunkPregenerator.getServer();
        if (!server.func_152345_ab()) {
            server.func_152344_a((Runnable)new Runnable() {
                @Override
                public void run() {
                    StructureRequestPacket.this.process(player);
                }
            });
            return;
        }
        this.process(player);
    }
    
    public void process(final EntityPlayer player) {
        final List<StructureData> entry = StructureManager.instance.getStructures(this.dimensionID);
        if (entry.isEmpty()) {
            final StructureAnswerPacket packet = new StructureAnswerPacket();
            packet.setType(-1);
            ChunkPregenerator.networking.sendPacketToPlayer(packet, player);
            return;
        }
        int bytesLeft = 30000;
        final List<StructureAnswerPacket> toSend = new ArrayList<StructureAnswerPacket>();
        StructureAnswerPacket packet2 = new StructureAnswerPacket();
        for (final StructureData data : entry) {
            final int bytes = data.getBytes();
            if (bytes > bytesLeft) {
                toSend.add(packet2);
                packet2 = new StructureAnswerPacket();
            }
            packet2.addData(data);
            bytesLeft -= bytes;
        }
        if (packet2.hasData()) {
            toSend.add(packet2);
        }
        for (int i = 0; i < toSend.size(); ++i) {
            final StructureAnswerPacket data2 = toSend.get(i);
            data2.setType(toSend.size() - (i + 1));
            ChunkPregenerator.networking.sendPacketToPlayer(data2, player);
        }
    }
}
