// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.trackerInfo.detailed;

import pregenerator.base.api.misc.IRenderHelper;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.impl.tracking.WorldTracker;
import pregenerator.base.api.network.IWriteableBuffer;

public class EntitiesEntry extends DetailedEntry
{
    int entities;
    
    public EntitiesEntry() {
        this.register();
    }
    
    @Override
    public String getName() {
        return "Entities";
    }
    
    @Override
    public void writeServer(final IWriteableBuffer buf) {
        final WorldTracker tracker = this.getWorld();
        buf.writeInt((tracker != null) ? tracker.getLoadedEntities() : 0);
    }
    
    @Override
    public void readClient(final IReadableBuffer buf) {
        this.entities = buf.readInt();
    }
    
    @Override
    public void render(final int x, final int y, final float progress, final int width, final IRenderHelper helper) {
        helper.renderText(x - (width - 25), y, width, "LoadedEntities: " + this.entities);
    }
}
