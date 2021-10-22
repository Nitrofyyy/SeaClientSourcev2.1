// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.processor.generator;

import java.util.Iterator;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraft.world.chunk.storage.RegionFileCache;
import net.minecraft.world.storage.ThreadedFileIOBase;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.world.chunk.storage.RegionFile;
import java.io.File;
import java.util.Map;

public class ChunkThread implements Runnable
{
    static Map<File, RegionFile> map;
    AtomicBoolean clear;
    static ChunkThread currentInstance;
    long myThreadID;
    ChunkProcessor process;
    
    public ChunkThread(final ChunkProcessor instance) {
        this.clear = new AtomicBoolean(false);
        this.myThreadID = instance.threadID.get();
        this.process = instance;
        ChunkThread.currentInstance = this;
    }
    
    public static void clearCache() {
        if (ChunkThread.currentInstance != null) {
            ChunkThread.currentInstance.clear.lazySet(true);
        }
    }
    
    public static int getLoadedFiles() {
        return (ChunkThread.map != null) ? ChunkThread.map.size() : 0;
    }
    
    @Override
    public void run() {
        while (this.process.threadID.get() == this.myThreadID && this.process.isRunning()) {
            this.update();
        }
    }
    
    private void update() {
        try {
            ThreadedFileIOBase.func_178779_a().func_75734_a();
            if (ChunkThread.map == null) {
                ChunkThread.map = (Map<File, RegionFile>)ReflectionHelper.getPrivateValue((Class)RegionFileCache.class, (Object)null, 0);
            }
            else if (ChunkThread.map != null && ChunkThread.map.size() > 50) {
                while (ChunkThread.map.size() > 50) {
                    final Iterator<File> files = ChunkThread.map.keySet().iterator();
                    if (!files.hasNext()) {
                        break;
                    }
                    final RegionFile file = ChunkThread.map.remove(files.next());
                    if (file == null) {
                        continue;
                    }
                    file.func_76708_c();
                }
            }
            Thread.sleep(1L);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
