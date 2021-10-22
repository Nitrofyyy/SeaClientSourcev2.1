// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.preview.data;

import net.minecraft.util.MathHelper;
import pregenerator.impl.misc.Tuple;
import java.util.Deque;
import pregenerator.impl.client.preview.world.WorldSeed;
import pregenerator.impl.misc.FilePos;
import com.google.common.cache.Cache;
import pregenerator.impl.client.preview.world.data.IChunkData;
import java.nio.ByteBuffer;
import java.io.RandomAccessFile;

public class Tasks
{
    public static ByteBuffer readBytes(final RandomAccessFile input, final long offset, final long size) throws Exception {
        final byte[] data = new byte[(int)size];
        input.seek(offset);
        input.readFully(data);
        return ByteBuffer.wrap(data);
    }
    
    public static class FetchChunkTask implements ITask
    {
        int chunkX;
        int chunkY;
        IChunkData data;
        boolean done;
        IFileProvider.FileType type;
        Cache<FilePos, IChunkData> cache;
        
        public FetchChunkTask(final int x, final int z, final Cache<FilePos, IChunkData> data) {
            this.done = false;
            this.cache = data;
            this.chunkX = x;
            this.chunkY = z;
            this.type = (WorldSeed.isUsingCompression() ? IFileProvider.FileType.Compressed_Chunk_Data : IFileProvider.FileType.Chunk_Data);
        }
        
        @Override
        public void handleTask(final RandomAccessFile chunkData, final RandomAccessFile heightData, final IFileProvider provider) throws Exception {
            if (!provider.hasIndex(this.chunkX, this.chunkY)) {
                this.data = null;
                this.done = true;
                return;
            }
            final ByteBuffer chunkBuffer = Tasks.readBytes(chunkData, provider.getIndex(this.chunkX, this.chunkY, this.type), this.type.getOffset());
            if (chunkBuffer.get() <= 0) {
                this.data = null;
                this.done = true;
                return;
            }
            this.data = this.type.createData(chunkBuffer, Tasks.readBytes(heightData, provider.getIndex(this.chunkX, this.chunkY, IFileProvider.FileType.HeightData), IFileProvider.FileType.HeightData.getOffset()));
            this.done = true;
            if (this.data != null) {
                this.cache.put((Object)new FilePos(this.chunkX, this.chunkY), (Object)this.data);
            }
        }
        
        public boolean isDone() {
            return this.done;
        }
        
        public IChunkData getData() {
            return this.data;
        }
    }
    
    public static class MassFetchTask implements ITask
    {
        Deque<Tuple<IChunkData, Integer>> result;
        int view;
        IFileProvider.FileType chunkType;
        
        public MassFetchTask(final Deque<Tuple<IChunkData, Integer>> result, final int view) {
            this.result = result;
            this.view = view;
            this.chunkType = (WorldSeed.isUsingCompression() ? IFileProvider.FileType.Compressed_Chunk_Data : IFileProvider.FileType.Chunk_Data);
        }
        
        @Override
        public void handleTask(final RandomAccessFile chunkData, final RandomAccessFile heightData, final IFileProvider provider) throws Exception {
            for (int total = provider.getStored(), i = 0; i < total; i += 1024) {
                this.gatherChunks(i, MathHelper.func_76125_a(total - i, 0, 1024), chunkData, heightData);
            }
        }
        
        public void gatherChunks(final long startValue, final long size, final RandomAccessFile chunkData, final RandomAccessFile heightData) throws Exception {
            if (size <= 0L) {
                return;
            }
            final ByteBuffer chunkBuffer = Tasks.readBytes(chunkData, startValue * IFileProvider.FileType.Chunk_Data.getOffset(), size * IFileProvider.FileType.Chunk_Data.getOffset());
            final ByteBuffer heigthBuffer = Tasks.readBytes(heightData, startValue * IFileProvider.FileType.HeightData.getOffset(), size * IFileProvider.FileType.HeightData.getOffset());
            for (int k = 0; k < size; ++k) {
                chunkBuffer.position((int)(k * IFileProvider.FileType.Chunk_Data.getOffset()));
                heigthBuffer.position((int)(k * IFileProvider.FileType.HeightData.getOffset()));
                if (chunkBuffer.get() > 0) {
                    this.result.add(new Tuple<IChunkData, Integer>(this.chunkType.createData(chunkBuffer, heigthBuffer), this.view));
                }
            }
        }
    }
    
    public static class FetchHeightData implements ITask
    {
        int x;
        int y;
        int[] data;
        boolean done;
        
        public FetchHeightData(final int x, final int y) {
            this.data = new int[0];
            this.done = false;
            this.x = x;
            this.y = y;
        }
        
        @Override
        public void handleTask(final RandomAccessFile chunkData, final RandomAccessFile heightData, final IFileProvider provider) throws Exception {
            final ByteBuffer buffer = Tasks.readBytes(heightData, provider.getIndex(this.x, this.y, IFileProvider.FileType.HeightData), IFileProvider.FileType.HeightData.getOffset());
            if (buffer.get() <= 0) {
                this.done = true;
                return;
            }
            final int[] fetch = new int[256];
            for (int i = 0; i < fetch.length; ++i) {
                fetch[i] = buffer.get() + 128;
            }
            this.data = fetch;
            this.done = true;
        }
        
        public boolean isDone() {
            return this.done;
        }
        
        public int[] getHeightData() {
            return this.data;
        }
    }
    
    public static class StoreHeightData implements ITask
    {
        int x;
        int y;
        int[] data;
        
        public StoreHeightData(final int x, final int y, final int[] data) {
            this.x = x;
            this.y = y;
            this.data = data;
        }
        
        @Override
        public void handleTask(final RandomAccessFile chunkData, final RandomAccessFile heightData, final IFileProvider provider) throws Exception {
            final ByteBuffer buffer = ByteBuffer.allocate((int)IFileProvider.FileType.HeightData.getOffset());
            buffer.put((byte)1);
            for (int i = 0; i < this.data.length; ++i) {
                buffer.put((byte)(this.data[i] - 128));
            }
            heightData.seek(provider.getOrCreateIndex(this.x, this.y, IFileProvider.FileType.HeightData));
            heightData.write(buffer.array());
        }
    }
}
