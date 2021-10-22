// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.trackerInfo.detailed;

import pregenerator.base.api.misc.IRenderHelper;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.impl.tracking.WorldTracker;
import pregenerator.base.api.network.IWriteableBuffer;

public class DimensionEntry extends DetailedEntry
{
    long usage;
    long average;
    
    public DimensionEntry() {
        this.register();
    }
    
    @Override
    public String getName() {
        return "Dim-Usage";
    }
    
    @Override
    public void writeServer(final IWriteableBuffer buf) {
        final WorldTracker tracker = this.getWorld();
        buf.writeLong((tracker != null) ? tracker.getAverage() : 0L);
        buf.writeLong((tracker != null) ? tracker.getCurrent() : 0L);
    }
    
    @Override
    public void readClient(final IReadableBuffer buf) {
        this.average = buf.readLong();
        this.usage = buf.readLong();
    }
    
    @Override
    public int getYOffset() {
        return 12;
    }
    
    @Override
    public void render(final int x, int y, float progress, final int width, final IRenderHelper helper) {
        long actual = this.average;
        int value = this.getValue(actual);
        progress = this.clamp(0.0f, 1.0f, value / 50.0f);
        int progresBar = (int)(progress * width);
        helper.renderBar(x - width / 2, y, width, progresBar, "Average: " + value + " / 50 ms (" + actual / 1000L + " qs)");
        y += 6;
        actual = this.usage;
        value = this.getValue(actual);
        progress = this.clamp(0.0f, 1.0f, value / 50.0f);
        progresBar = (int)(progress * width);
        helper.renderBar(x - width / 2, y, width, progresBar, "Usage: " + value + " / 50 ms (" + actual / 1000L + " qs)");
    }
}
