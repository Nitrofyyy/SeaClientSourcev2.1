// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.trackerInfo;

import pregenerator.base.api.misc.IRenderHelper;
import pregenerator.base.api.network.IReadableBuffer;
import java.util.Iterator;
import java.util.List;
import pregenerator.impl.tracking.WorldTracker;
import pregenerator.base.api.network.IWriteableBuffer;
import java.util.HashMap;
import java.util.Map;

public class WorldUsage extends TrackerEntry
{
    Map<Integer, Long> mapping;
    
    public WorldUsage() {
        this.mapping = new HashMap<Integer, Long>();
        this.register();
    }
    
    @Override
    public String getName() {
        return "Worlds-Usage";
    }
    
    @Override
    public void writeServer(final IWriteableBuffer buf) {
        final List<WorldTracker> trackers = this.getTracker().getTracker();
        buf.writeShort(trackers.size());
        for (final WorldTracker track : trackers) {
            buf.writeInt(track.getDimID());
            buf.writeLong(track.getAverage());
        }
    }
    
    @Override
    public void readClient(final IReadableBuffer buf) {
        final Map<Integer, Long> map = new HashMap<Integer, Long>();
        for (int size = buf.readShort(), i = 0; i < size; ++i) {
            final int id = buf.readInt();
            final long value = buf.readLong();
            map.put(id, value);
        }
        this.mapping = map;
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
    public int getYOffset() {
        return super.getYOffset() * this.mapping.size();
    }
    
    @Override
    public void render(final int x, int y, float progress, final int width, final IRenderHelper helper) {
        for (final Map.Entry<Integer, Long> entry : new HashMap<Integer, Long>(this.mapping).entrySet()) {
            final long actual = entry.getValue();
            final int value = this.getValue(actual);
            progress = this.clamp(0.0f, 1.0f, value / 50.0f);
            final int progresBar = (int)(progress * width);
            helper.renderBar(x - width / 2, y, width, progresBar, "Dim " + entry.getKey() + ": " + value + " / 50 ms (" + actual / 1000L + " qs)");
            y += 6;
        }
    }
    
    public int getValue(final long value) {
        return (int)(value / 1000L / 1000L);
    }
    
    public float clamp(final float min, final float max, final float current) {
        return (current < min) ? min : ((current > max) ? max : current);
    }
}
