// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.network.packets;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pregenerator.impl.client.ClientHandler;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Iterator;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.base.api.network.IReadableBuffer;
import java.util.Collection;
import pregenerator.impl.processor.generator.ChunkProcessor;
import java.util.ArrayList;
import pregenerator.impl.client.infos.InfoEntry;
import java.util.List;
import pregenerator.base.api.network.PregenPacket;

public class AnswerPacket extends PregenPacket
{
    List<InfoEntry> entries;
    boolean processing;
    
    public AnswerPacket() {
        this.entries = new ArrayList<InfoEntry>();
    }
    
    public AnswerPacket(final List<InfoEntry> list) {
        this.entries = new ArrayList<InfoEntry>();
        this.processing = ChunkProcessor.INSTANCE.isRunning();
        this.entries.addAll(list);
    }
    
    @Override
    public void read(final IReadableBuffer buffer) {
        if (!(this.processing = buffer.readBoolean())) {
            return;
        }
        for (int amount = buffer.readByte(), i = 0; i < amount; ++i) {
            final InfoEntry entry = InfoEntry.getByID(buffer.readByte());
            if (entry != null) {
                entry.read(buffer);
            }
        }
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        buffer.writeBoolean(this.processing);
        if (!this.processing) {
            return;
        }
        buffer.writeByte(this.entries.size());
        for (final InfoEntry entry : this.entries) {
            buffer.writeByte(entry.getID());
            entry.write(buffer);
        }
    }
    
    @Override
    public void handle(final EntityPlayer player) {
        this.handleClient();
    }
    
    @SideOnly(Side.CLIENT)
    public void handleClient() {
        ClientHandler.INSTANCE.info.running = this.processing;
    }
}
