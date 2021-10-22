// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.trackerInfo.detailed;

import pregenerator.base.api.misc.IRenderHelper;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.impl.tracking.WorldTracker;
import pregenerator.base.api.network.IWriteableBuffer;

public class BlockTicksEntry extends DetailedEntry
{
    int value;
    int average;
    
    public BlockTicksEntry() {
        this.register();
    }
    
    @Override
    public String getName() {
        return "BlockTicks";
    }
    
    @Override
    public void writeServer(final IWriteableBuffer buf) {
        final WorldTracker tracker = this.getWorld();
        buf.writeInt((tracker != null) ? tracker.getAverageBlockTicks() : 0);
        buf.writeInt((tracker != null) ? tracker.getBlockTicks() : 0);
    }
    
    @Override
    public void readClient(final IReadableBuffer buf) {
        this.average = buf.readInt();
        this.value = buf.readInt();
    }
    
    @Override
    public int getYOffset() {
        return 12;
    }
    
    @Override
    public void render(final int x, int y, float progress, final int width, final IRenderHelper helper) {
        helper.renderText(x - (width - 25), y, width, "Current BlockTicks: " + this.value);
        int actual = this.average;
        progress = this.clamp(0.0f, 1.0f, actual / 65536.0f);
        int progresBar = (int)(progress * width);
        helper.renderBar(x - width / 2, y, width, progresBar, "Average BlockTicks: " + actual + " / 65536");
        y += 6;
        actual = this.value;
        progress = this.clamp(0.0f, 1.0f, actual / 65536.0f);
        progresBar = (int)(progress * width);
        helper.renderBar(x - width / 2, y, width, progresBar, "BlockTicks: " + actual + " / 65536");
    }
}
