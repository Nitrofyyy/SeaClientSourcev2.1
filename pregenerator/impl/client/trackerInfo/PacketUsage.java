// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.trackerInfo;

import pregenerator.base.api.misc.IRenderHelper;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.IWriteableBuffer;

public class PacketUsage extends TrackerEntry
{
    int packets;
    
    public PacketUsage() {
        this.register();
    }
    
    @Override
    public String getName() {
        return "PacketData";
    }
    
    @Override
    public void writeServer(final IWriteableBuffer buf) {
        buf.writeInt(this.getTracker().getPackets());
    }
    
    @Override
    public void readClient(final IReadableBuffer buf) {
        this.packets = buf.readInt();
    }
    
    @Override
    public int currentValue() {
        return 0;
    }
    
    @Override
    public int maxValue() {
        return 0;
    }
    
    @Override
    public void render(final int x, final int y, final float progress, final int width, final IRenderHelper helper) {
        helper.renderText(x - (width - 25), y, width, "Server Packets Processed: " + this.packets);
    }
}
