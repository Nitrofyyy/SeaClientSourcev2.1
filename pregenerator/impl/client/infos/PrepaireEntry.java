// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.infos;

import pregenerator.base.api.misc.IRenderHelper;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.impl.processor.PrepaireProgress;
import pregenerator.base.api.network.IWriteableBuffer;

public class PrepaireEntry extends InfoEntry
{
    long prepare;
    long prepareMax;
    
    public PrepaireEntry() {
        this.register();
    }
    
    @Override
    public String getName() {
        return "Prepare Info";
    }
    
    @Override
    public boolean shouldRender() {
        return this.prepareMax > 0L;
    }
    
    @Override
    public void write(final IWriteableBuffer buf) {
        final PrepaireProgress process = this.getProcessor().progress;
        buf.writeLong(process.getCurrent());
        buf.writeLong(process.getMax());
    }
    
    @Override
    public void read(final IReadableBuffer buf) {
        this.prepare = buf.readLong();
        this.prepareMax = buf.readLong();
    }
    
    @Override
    public int currentValue() {
        return (this.prepare >= 2147483647L) ? Integer.MAX_VALUE : ((int)this.prepare);
    }
    
    @Override
    public int maxValue() {
        return (this.prepareMax >= 2147483647L) ? Integer.MAX_VALUE : ((int)this.prepareMax);
    }
    
    @Override
    public void render(final int x, final int y, final float progress, final int width, final IRenderHelper helper) {
        helper.renderBar(x - width / 2, y, width, (int)(this.prepare / (double)this.prepareMax * width), "Prepare: " + this.prepare + " / " + this.prepareMax);
    }
}
