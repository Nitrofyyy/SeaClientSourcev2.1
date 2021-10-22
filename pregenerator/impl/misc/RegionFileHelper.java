// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.misc;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.io.RandomAccessFile;
import java.io.File;

public class RegionFileHelper
{
    private static final byte[] EMPTY_SECTOR;
    private final File fileName;
    private RandomAccessFile dataFile;
    private final int[] offsets;
    private final int[] chunkTimestamps;
    Set<FilePos> existingChunks;
    private int sizeDelta;
    private long lastModified;
    
    public RegionFileHelper(final File fileNameIn) {
        this.offsets = new int[1024];
        this.chunkTimestamps = new int[1024];
        this.existingChunks = new LinkedHashSet<FilePos>();
        this.fileName = fileNameIn;
        this.sizeDelta = 0;
        try {
            if (fileNameIn.exists()) {
                this.lastModified = fileNameIn.lastModified();
            }
            this.dataFile = new RandomAccessFile(fileNameIn, "rw");
            if (this.dataFile.length() < 4096L) {
                this.dataFile.write(RegionFileHelper.EMPTY_SECTOR);
                this.dataFile.write(RegionFileHelper.EMPTY_SECTOR);
                this.sizeDelta += 8192;
            }
            if ((this.dataFile.length() & 0xFFFL) != 0x0L) {
                for (int i = 0; i < (this.dataFile.length() & 0xFFFL); ++i) {
                    this.dataFile.write(0);
                }
            }
            this.dataFile.seek(0L);
            for (int j1 = 0; j1 < 1024; ++j1) {
                final int k = this.dataFile.readInt();
                if ((this.offsets[j1] = k) > 0) {
                    this.existingChunks.add(new FilePos(j1 % 32, j1 / 32));
                }
            }
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
    
    public boolean close() throws IOException {
        if (this.dataFile != null) {
            this.dataFile.close();
        }
        return this.existingChunks.size() > 0;
    }
    
    private boolean outOfBounds(final int x, final int z) {
        return x < 0 || x >= 32 || z < 0 || z >= 32;
    }
    
    private int getOffset(final int x, final int z) {
        return this.offsets[x + z * 32];
    }
    
    public boolean isChunkSaved(final int x, final int z) {
        return this.getOffset(x, z) != 0;
    }
    
    public void deleteChunk(final int x, final int z) {
        if (this.outOfBounds(x, z)) {
            return;
        }
        try {
            this.setOffset(x, z, 0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setOffset(final int x, final int z, final int offset) throws IOException {
        final boolean was = this.offsets[x + z * 32] != 0;
        this.offsets[x + z * 32] = offset;
        this.dataFile.seek((x + z * 32) * 4);
        this.dataFile.writeInt(offset);
        final boolean is = offset != 0;
        if (was != is) {
            if (is) {
                this.existingChunks.add(new FilePos(x, z));
            }
            else {
                this.existingChunks.remove(new FilePos(x, z));
            }
        }
    }
    
    public List<FilePos> getInstalledChunks() {
        return new ArrayList<FilePos>(this.existingChunks);
    }
    
    public File getFile() {
        return this.fileName;
    }
    
    static {
        EMPTY_SECTOR = new byte[4096];
    }
}
