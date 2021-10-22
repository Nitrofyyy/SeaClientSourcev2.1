// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.infos;

import pregenerator.base.api.misc.IRenderHelper;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.impl.storage.PregenTask;
import pregenerator.base.api.network.IWriteableBuffer;

public class GenerationType extends InfoEntry
{
    int type;
    
    public GenerationType() {
        this.register();
    }
    
    @Override
    public String getName() {
        return "Processing Type";
    }
    
    @Override
    public void write(final IWriteableBuffer buf) {
        final PregenTask task = this.getProcessor().getTask();
        buf.writeByte((task != null) ? task.getPostType().ordinal() : 0);
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
        helper.renderText(x - wid, y, width, "Gen-Type: " + this.getType());
    }
    
    private String getType() {
        switch (this.type) {
            case 1: {
                return "Terrain & Post Processing";
            }
            case 2: {
                return "Post Processing Only";
            }
            case 3: {
                return "Terrain & Blocking Post";
            }
            case 4: {
                return "Retrogen";
            }
            default: {
                return "Terrain Only";
            }
        }
    }
}
