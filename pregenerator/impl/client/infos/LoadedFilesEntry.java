// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.infos;

import pregenerator.impl.processor.generator.ChunkThread;

public class LoadedFilesEntry extends BarEntry
{
    public LoadedFilesEntry() {
        this.register();
    }
    
    @Override
    public String getText(final int current, final int max) {
        return "Loaded Files: " + current + " / " + max;
    }
    
    @Override
    public int getMaxServer() {
        return 60;
    }
    
    @Override
    public int getCurrentServer() {
        return ChunkThread.getLoadedFiles();
    }
    
    @Override
    public String getName() {
        return "Loaded Files Info";
    }
}
