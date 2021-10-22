// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.trackerInfo;

import pregenerator.base.api.misc.IRenderHelper;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.IWriteableBuffer;

public class RamUsage extends TrackerEntry
{
    int min;
    int max;
    
    public RamUsage() {
        this.register();
    }
    
    @Override
    public String getName() {
        return "ServerRam";
    }
    
    @Override
    public void writeServer(final IWriteableBuffer buf) {
        final Runtime time = Runtime.getRuntime();
        buf.writeInt(this.toMB(time.totalMemory() - time.freeMemory()));
        buf.writeInt(this.toMB(time.maxMemory()));
    }
    
    @Override
    public void readClient(final IReadableBuffer buf) {
        this.min = buf.readInt();
        this.max = buf.readInt();
    }
    
    @Override
    public int currentValue() {
        return this.min;
    }
    
    @Override
    public int maxValue() {
        return this.max;
    }
    
    @Override
    public void render(final int x, final int y, final float progress, final int width, final IRenderHelper helper) {
        final int progresBar = (int)(progress * width);
        helper.renderBar(x - width / 2, y, width, progresBar, "ServerRam: " + this.min + "/" + this.max + " MB");
    }
    
    private int toMB(final long input) {
        return (int)(input / 1024L / 1024L);
    }
}
