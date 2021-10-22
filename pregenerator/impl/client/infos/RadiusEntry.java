// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.infos;

import pregenerator.base.api.misc.IRenderHelper;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.impl.storage.PregenTask;
import pregenerator.base.api.network.IWriteableBuffer;

public class RadiusEntry extends InfoEntry
{
    boolean isXOnly;
    int xRange;
    int zRange;
    
    public RadiusEntry() {
        this.isXOnly = false;
        this.xRange = 0;
        this.zRange = 0;
        this.register();
    }
    
    @Override
    public String getName() {
        return "Radius Info";
    }
    
    @Override
    public void write(final IWriteableBuffer buf) {
        final PregenTask task = this.getProcessor().getTask();
        if (task != null) {
            if (task.getType() < 2 || task.isBenchmarkTask()) {
                buf.writeBoolean(true);
                buf.writeShort(task.getXRadius());
            }
            else {
                buf.writeBoolean(false);
                buf.writeShort(task.getXRadius());
                buf.writeShort(task.getZRadius());
            }
        }
        else {
            buf.writeBoolean(true);
            buf.writeShort(0);
        }
    }
    
    @Override
    public void read(final IReadableBuffer buf) {
        this.isXOnly = buf.readBoolean();
        if (this.isXOnly) {
            this.xRange = buf.readShort();
            this.zRange = this.xRange;
        }
        else {
            this.xRange = buf.readShort();
            this.zRange = buf.readShort();
        }
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
        if (this.isXOnly) {
            helper.renderText(x - wid, y, width, "Radius: " + this.xRange);
        }
        else {
            helper.renderText(x - wid, y, width, "Radius: X: " + this.xRange + ", Z: " + this.zRange);
        }
    }
}
