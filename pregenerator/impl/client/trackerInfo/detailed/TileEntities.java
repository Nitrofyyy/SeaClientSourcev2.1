// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.trackerInfo.detailed;

import pregenerator.base.api.misc.IRenderHelper;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.impl.tracking.WorldTracker;
import pregenerator.base.api.network.IWriteableBuffer;

public class TileEntities extends DetailedEntry
{
    int tile;
    int tickingTile;
    
    public TileEntities() {
        this.register();
    }
    
    @Override
    public String getName() {
        return "TileEntities";
    }
    
    @Override
    public void writeServer(final IWriteableBuffer buf) {
        final WorldTracker tracker = this.getWorld();
        buf.writeInt((tracker != null) ? tracker.getLoadedTileEntities() : 0);
        buf.writeInt((tracker != null) ? tracker.getTickingTileEntities() : 0);
    }
    
    @Override
    public void readClient(final IReadableBuffer buf) {
        this.tile = buf.readInt();
        this.tickingTile = buf.readInt();
    }
    
    @Override
    public int getYOffset() {
        return 12;
    }
    
    @Override
    public void render(final int x, int y, final float progress, final int width, final IRenderHelper helper) {
        helper.renderText(x - (width - 25), y, width, "LoadedTileEntities: " + this.tile);
        y += 6;
        helper.renderText(x - (width - 25), y, width, "TickingTileEntities: " + this.tickingTile);
    }
}
