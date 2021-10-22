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
import pregenerator.impl.tracking.ServerTracker;
import java.util.Collection;
import java.util.ArrayList;
import pregenerator.impl.client.trackerInfo.TrackerEntry;
import java.util.List;
import pregenerator.base.api.network.PregenPacket;

public class TrackerAnswerPacket extends PregenPacket
{
    List<TrackerEntry> entries;
    boolean enabled;
    
    public TrackerAnswerPacket() {
        this.entries = new ArrayList<TrackerEntry>();
        this.enabled = false;
    }
    
    public TrackerAnswerPacket(final List<TrackerEntry> list) {
        this.entries = new ArrayList<TrackerEntry>();
        this.enabled = false;
        this.entries.addAll(list);
        this.enabled = ServerTracker.INSTANCE.isEnabled();
    }
    
    @Override
    public void read(final IReadableBuffer buffer) {
        if (!(this.enabled = buffer.readBoolean())) {
            return;
        }
        for (int amount = buffer.readByte(), i = 0; i < amount; ++i) {
            final TrackerEntry entry = TrackerEntry.getByID(buffer.readByte());
            if (entry != null) {
                entry.readClient(buffer);
            }
        }
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        buffer.writeBoolean(this.enabled);
        if (!this.enabled) {
            return;
        }
        buffer.writeByte(this.entries.size());
        for (final TrackerEntry entry : this.entries) {
            buffer.writeByte(entry.getID());
            entry.writeServer(buffer);
        }
    }
    
    @Override
    public void handle(final EntityPlayer player) {
        this.handleClient();
    }
    
    @SideOnly(Side.CLIENT)
    public void handleClient() {
        ClientHandler.INSTANCE.tracker.running = this.enabled;
    }
}
