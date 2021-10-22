// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.infos;

import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.IWriteableBuffer;

public class LagBarEntry extends BarEntry
{
    boolean working;
    
    public LagBarEntry() {
        this.working = false;
        this.register();
    }
    
    @Override
    public String getText(final int current, final int max) {
        if (!this.working) {
            return current + "/" + max + " MS (Paused)";
        }
        return current + "/" + max + " MS";
    }
    
    @Override
    public String getName() {
        return "Lag-Meter";
    }
    
    @Override
    public int getCurrentServer() {
        return this.getProcessor().getAverageCPUTime();
    }
    
    @Override
    public int getMaxServer() {
        return this.getProcessor().getMaxTime();
    }
    
    @Override
    public void write(final IWriteableBuffer buf) {
        super.write(buf);
        buf.writeBoolean(this.getProcessor().isWorking());
    }
    
    @Override
    public void read(final IReadableBuffer buf) {
        super.read(buf);
        this.working = buf.readBoolean();
    }
}
