// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.infos;

import pregenerator.base.api.misc.IRenderHelper;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.IWriteableBuffer;

public class LoadedChunksEntry extends InfoEntry
{
    int chunks;
    
    public LoadedChunksEntry() {
        this.register();
    }
    
    @Override
    public String getName() {
        return "Chunk Info";
    }
    
    @Override
    public void write(final IWriteableBuffer buf) {
        buf.writeInt(this.getProcessor().getLoadedChunks());
    }
    
    @Override
    public void read(final IReadableBuffer buf) {
        this.chunks = buf.readInt();
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
        helper.renderText(x - wid, y, width, "Loaded Chunks: " + this.chunks);
    }
}
