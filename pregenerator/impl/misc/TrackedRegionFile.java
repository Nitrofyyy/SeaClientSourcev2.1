// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.misc;

import java.io.IOException;
import java.io.File;
import net.minecraft.world.chunk.storage.RegionFile;

public class TrackedRegionFile extends RegionFile
{
    boolean valid;
    
    public TrackedRegionFile(final File fileNameIn) {
        super(fileNameIn);
        this.valid = true;
    }
    
    public void func_76708_c() throws IOException {
        this.valid = false;
        super.func_76708_c();
    }
    
    public boolean isValid() {
        return this.valid;
    }
}
