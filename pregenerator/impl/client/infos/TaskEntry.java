// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.infos;

import pregenerator.base.api.misc.IRenderHelper;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.impl.storage.PregenTask;
import pregenerator.base.api.network.IWriteableBuffer;

public class TaskEntry extends InfoEntry
{
    int type;
    
    public TaskEntry() {
        this.register();
    }
    
    @Override
    public String getName() {
        return "Task Type";
    }
    
    @Override
    public void write(final IWriteableBuffer buf) {
        final PregenTask task = this.getProcessor().getTask();
        buf.writeByte((task != null) ? task.getType() : 0);
    }
    
    @Override
    public void read(final IReadableBuffer buf) {
        this.type = buf.readByte();
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
        helper.renderText(x - wid, y, width, "Task Type: " + this.getType());
    }
    
    private String getType() {
        switch (this.type) {
            case 1: {
                return "Circle Generation";
            }
            case 2: {
                return "Mass Square Generation";
            }
            case 3: {
                return "Mass Circle Generation";
            }
            case 4: {
                return "Square Extension Generation";
            }
            case 5: {
                return "Circle Extension Generation";
            }
            case 6: {
                return "Small Benchmark Task";
            }
            case 7: {
                return "Big Benchmark Task";
            }
            default: {
                return "Square Generation";
            }
        }
    }
}
