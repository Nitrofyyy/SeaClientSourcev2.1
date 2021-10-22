// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.trackerInfo.detailed;

import pregenerator.base.api.misc.IRenderHelper;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.impl.tracking.WorldTracker;
import pregenerator.base.api.network.IWriteableBuffer;

public class LoadedChunks extends DetailedEntry
{
    int value;
    
    public LoadedChunks() {
        this.register();
    }
    
    @Override
    public String getName() {
        return "LoadedChunks";
    }
    
    @Override
    public void writeServer(final IWriteableBuffer buf) {
        final WorldTracker tracker = this.getWorld();
        buf.writeInt((tracker != null) ? tracker.getLoadedChunks() : 0);
    }
    
    @Override
    public void readClient(final IReadableBuffer buf) {
        this.value = buf.readInt();
    }
    
    @Override
    public void render(final int x, final int y, final float progress, final int width, final IRenderHelper helper) {
        helper.renderText(x - (width - 25), y, width, "LoadedChunks: " + this.value);
    }
}
