// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.infos;

import pregenerator.base.api.misc.IRenderHelper;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.impl.storage.PregenTask;
import pregenerator.base.api.network.IWriteableBuffer;

public class CenterEntry extends InfoEntry
{
    int xCenter;
    int zCenter;
    
    public CenterEntry() {
        this.xCenter = 0;
        this.zCenter = 0;
        this.register();
    }
    
    @Override
    public String getName() {
        return "TaskCenter";
    }
    
    @Override
    public void write(final IWriteableBuffer buf) {
        final PregenTask task = this.getProcessor().getTask();
        if (task != null) {
            buf.writeInt(task.getCenterX());
            buf.writeInt(task.getCenterZ());
        }
        else {
            buf.writeInt(0);
            buf.writeInt(0);
        }
    }
    
    @Override
    public void read(final IReadableBuffer buf) {
        this.xCenter = buf.readInt();
        this.zCenter = buf.readInt();
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
        helper.renderText(x - wid, y, width, "CenterPos: X: " + this.xCenter + ", Z: " + this.zCenter);
    }
}
