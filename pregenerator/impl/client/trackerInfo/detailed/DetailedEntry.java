// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.trackerInfo.detailed;

import pregenerator.impl.tracking.WorldTracker;
import pregenerator.base.api.network.IReadableBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pregenerator.impl.client.ClientHandler;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.impl.client.trackerInfo.TrackerEntry;

public abstract class DetailedEntry extends TrackerEntry
{
    public int dim;
    
    @SideOnly(Side.CLIENT)
    @Override
    public void writeClient(final IWriteableBuffer buf) {
        buf.writeInt(ClientHandler.INSTANCE.tracker.targetDim);
    }
    
    @Override
    public void readServer(final IReadableBuffer buf) {
        this.dim = buf.readInt();
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldRender() {
        return ClientHandler.INSTANCE.tracker.showDetailed;
    }
    
    public WorldTracker getWorld() {
        return this.getTracker().getWorld(this.dim);
    }
    
    @Override
    public int currentValue() {
        return 0;
    }
    
    @Override
    public int maxValue() {
        return 0;
    }
    
    public int getValue(final long value) {
        return (int)(value / 1000L / 1000L);
    }
    
    public float clamp(final float min, final float max, final float current) {
        return (current < min) ? min : ((current > max) ? max : current);
    }
}
