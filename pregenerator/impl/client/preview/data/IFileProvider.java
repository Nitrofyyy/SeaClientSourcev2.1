// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.preview.data;

import java.nio.ByteBuffer;
import pregenerator.impl.client.preview.world.data.ChunkData;
import pregenerator.impl.client.preview.world.data.CompressedChunkData;
import pregenerator.impl.client.preview.world.data.IChunkData;
import net.minecraft.world.chunk.Chunk;

public interface IFileProvider
{
    boolean hasIndex(final int p0, final int p1);
    
    long getIndex(final int p0, final int p1, final FileType p2);
    
    long getOrCreateIndex(final int p0, final int p1, final FileType p2);
    
    long getTotalOffset(final FileType p0);
    
    int getStored();
    
    public enum FileType
    {
        Chunk_Data(1600L, 1000000L), 
        Compressed_Chunk_Data(530L, 4000000L), 
        HeightData(260L, 4000000L);
        
        long offset;
        long buffer;
        
        private FileType(final long offset, final long instances) {
            this.offset = offset;
            this.buffer = offset * instances;
        }
        
        public long getOffset() {
            return this.offset;
        }
        
        public long getBufferLimit() {
            return this.buffer;
        }
        
        public IChunkData createData(final Chunk chunk) {
            if (this == FileType.Compressed_Chunk_Data) {
                return new CompressedChunkData(chunk);
            }
            return new ChunkData(chunk);
        }
        
        public IChunkData createData(final ByteBuffer chunkBuffer, final ByteBuffer heightData) throws Exception {
            if (this == FileType.Compressed_Chunk_Data) {
                return CompressedChunkData.createDataFromBuffer(chunkBuffer, heightData);
            }
            return ChunkData.createDataFromBuffer(chunkBuffer, heightData);
        }
    }
}
