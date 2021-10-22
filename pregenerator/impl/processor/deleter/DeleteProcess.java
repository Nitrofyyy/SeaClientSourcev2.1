// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.processor.deleter;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.util.List;
import java.io.DataOutput;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.world.chunk.storage.RegionFile;
import pregenerator.impl.misc.RegionFileHelper;
import java.util.Iterator;
import pregenerator.impl.misc.FilePos;
import java.util.BitSet;
import java.util.Map;
import java.io.File;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.WorldServer;
import java.util.LinkedList;

public class DeleteProcess
{
    LinkedList<DeleteFile> positions;
    boolean loaded;
    WorldServer server;
    ChunkProviderServer prov;
    File worldFile;
    int total;
    
    public DeleteProcess(final File worldFile, final Map<Long, BitSet> data) {
        this.positions = new LinkedList<DeleteFile>();
        this.worldFile = new File(worldFile, "region");
        for (final Map.Entry<Long, BitSet> entry : data.entrySet()) {
            final DeleteFile file = new DeleteFile(this, new FilePos(entry.getKey()), entry.getValue());
            if (file.isValid()) {
                this.positions.add(file);
                this.total += file.getCount();
            }
        }
    }
    
    public void setChunkHost(final WorldServer world) {
        this.server = world;
        this.prov = (ChunkProviderServer)world.func_72863_F();
    }
    
    public boolean hasWork() {
        return this.positions.size() > 0;
    }
    
    public DeleteFile getEntry() {
        return this.positions.poll();
    }
    
    public int getTotalWork() {
        return this.total;
    }
    
    public boolean loaded(final FilePos pos) {
        return this.prov != null && (this.prov.func_73149_a(pos.x, pos.z) || this.server.func_73040_p().func_152621_a(pos.x, pos.z));
    }
    
    public static class DeleteFile
    {
        DeleteProcess process;
        FilePos pos;
        File file;
        RegionFileHelper helper;
        BitSet positions;
        int tasks;
        
        public DeleteFile(final DeleteProcess process, final FilePos entry, final BitSet positions) {
            this.tasks = 0;
            this.process = process;
            this.pos = entry;
            this.positions = positions;
            this.tasks = positions.cardinality();
            this.file = new File(process.worldFile, "r." + this.pos.x + "." + this.pos.z + ".mca");
        }
        
        public boolean isValid() {
            return this.file.exists();
        }
        
        public int getCount() {
            return this.tasks;
        }
        
        public void update() {
            this.helper = new RegionFileHelper(this.file);
            for (int i = 0; i < 1024; ++i) {
                if (this.positions.get(i)) {
                    this.helper.deleteChunk(i % 32, i / 32);
                }
            }
            this.cleanup();
        }
        
        public void cleanup() {
            try {
                if (this.helper.close()) {
                    final List<FilePos> filePos = this.helper.getInstalledChunks();
                    final File temp = new File("Temp.mca");
                    final File file = this.helper.getFile();
                    file.renameTo(temp);
                    final RegionFile from = new RegionFile(temp);
                    final RegionFile to = new RegionFile(file);
                    for (final FilePos pos : filePos) {
                        if (from.func_76709_c(pos.x & 0x1F, pos.z & 0x1F)) {
                            final DataInputStream read = from.func_76704_a(pos.x & 0x1F, pos.z & 0x1F);
                            if (read == null) {
                                continue;
                            }
                            final DataOutputStream write = to.func_76710_b(pos.x & 0x1F, pos.z & 0x1F);
                            CompressedStreamTools.func_74800_a(CompressedStreamTools.func_74794_a(read), (DataOutput)write);
                            read.close();
                            write.close();
                        }
                    }
                    from.func_76708_c();
                    to.func_76708_c();
                    temp.delete();
                }
                else {
                    this.helper.getFile().delete();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
