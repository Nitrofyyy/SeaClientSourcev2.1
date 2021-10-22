// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.infos;

import pregenerator.base.api.misc.IRenderHelper;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.IWriteableBuffer;

public class RunningTimeEntry extends InfoEntry
{
    long time;
    
    public RunningTimeEntry() {
        this.register();
    }
    
    @Override
    public String getName() {
        return "Running Time";
    }
    
    @Override
    public void write(final IWriteableBuffer buf) {
        buf.writeLong(this.getProcessor().getWorkTime());
    }
    
    @Override
    public void read(final IReadableBuffer buf) {
        this.time = buf.readLong();
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
        final int wid = width - 25;
        helper.renderText(x - wid, y, width, "Running Time: " + this.formatIntoTime(this.time));
    }
    
    String formatIntoTime(long time) {
        time /= 1000L;
        final int sec = (int)time % 60;
        time /= 60L;
        final int min = (int)time % 60;
        time /= 60L;
        final int hour = (int)time % 24;
        time /= 24L;
        return String.format("%02d:%02d:%02d:%02d", time, hour, min, sec);
    }
}
