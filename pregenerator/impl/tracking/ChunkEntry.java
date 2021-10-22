// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.tracking;

import pregenerator.impl.misc.FilePos;
import pregenerator.base.api.network.IWriteableBuffer;
import java.util.Iterator;
import java.util.Collection;
import net.minecraft.util.ClassInheritanceMultiMap;
import org.apache.commons.lang3.mutable.MutableInt;
import java.util.LinkedHashMap;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.util.ITickable;
import net.minecraft.entity.EntityList;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.world.chunk.Chunk;
import pregenerator.base.api.network.IReadableBuffer;
import java.util.HashMap;
import net.minecraft.tileentity.TileEntity;
import java.util.Map;

public class ChunkEntry
{
    public static final Map<String, Class<? extends TileEntity>> idToClass;
    public static final Map<Class<? extends TileEntity>, String> classToID;
    int data;
    public int xPos;
    public int zPos;
    public Map<String, Integer> entityCount;
    public Map<String, Integer> tileEntityCount;
    public Map<Integer, Integer> biomeCount;
    public int entities;
    public int entityTypes;
    public int tileEntities;
    public int tickableTileEntities;
    public int tileTypes;
    public int randomUpdateBlocks;
    
    public static void init() {
    }
    
    private ChunkEntry() {
        this.entityCount = new HashMap<String, Integer>();
        this.tileEntityCount = new HashMap<String, Integer>();
        this.biomeCount = new HashMap<Integer, Integer>();
    }
    
    public static ChunkEntry fromBuffer(final IReadableBuffer buffer) {
        final ChunkEntry entry = new ChunkEntry();
        entry.readFromBuffer(buffer);
        return entry;
    }
    
    public static ChunkEntry fromChunk(final Chunk chunk) {
        final ChunkEntry data = new ChunkEntry();
        data.data = 16;
        data.xPos = chunk.field_76635_g;
        data.zPos = chunk.field_76647_h;
        final CollectorMap<Entity> entityMap = new CollectorMap<Entity>();
        for (final Collection<Entity> collect : chunk.func_177429_s()) {
            entityMap.addAll(collect);
        }
        entityMap.removeAll(EntityPlayer.class);
        data.entities = entityMap.size();
        data.entityTypes = entityMap.getTypeCount();
        for (final Map.Entry<Class<Entity>, Set<Entity>> entry : entityMap.entrySet()) {
            final String res = EntityList.field_75626_c.get(entry.getKey());
            if (res == null) {
                continue;
            }
            data.addEntityType(res.toString(), entry.getValue().size());
        }
        int tickable = 0;
        final CollectorMap<TileEntity> tileMap = new CollectorMap<TileEntity>();
        for (final TileEntity tile : chunk.func_177434_r().values()) {
            if (tile instanceof ITickable) {
                ++tickable;
            }
            tileMap.addEntry(tile);
        }
        data.tickableTileEntities = tickable;
        data.tileEntities = tileMap.size();
        data.tileTypes = tileMap.getTypeCount();
        for (final Map.Entry<Class<TileEntity>, Set<TileEntity>> entry2 : tileMap.entrySet()) {
            final String res2 = ChunkEntry.classToID.get(entry2.getKey());
            if (res2 == null) {
                continue;
            }
            data.addTileEntityType(res2.toString(), entry2.getValue().size());
        }
        final ExtendedBlockStorage[] extendet = chunk.func_76587_i();
        for (int i = 0; i < 16 && extendet[i] != null; ++i) {
            try {
                final ChunkEntry chunkEntry = data;
                chunkEntry.randomUpdateBlocks += (int)ReflectionHelper.getPrivateValue((Class)ExtendedBlockStorage.class, (Object)extendet[i], 2);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        final Map<Integer, MutableInt> biomeInfo = new LinkedHashMap<Integer, MutableInt>();
        for (final byte biomeID : chunk.func_76605_m()) {
            final Integer id = new Integer(biomeID);
            MutableInt counter = biomeInfo.get(id);
            if (counter == null) {
                counter = new MutableInt();
                biomeInfo.put(id, counter);
            }
            counter.increment();
        }
        for (final Map.Entry<Integer, MutableInt> entry3 : biomeInfo.entrySet()) {
            data.addBiomeType(entry3.getKey(), entry3.getValue().intValue());
        }
        return data;
    }
    
    private void addEntityType(final String s, final int count) {
        if (s == null) {
            return;
        }
        this.entityCount.put(s, count);
        this.data += 6 + s.length() * 2;
    }
    
    private void addTileEntityType(final String s, final int count) {
        if (s == null) {
            return;
        }
        this.tileEntityCount.put(s, count);
        this.data += 6 + s.length() * 2;
    }
    
    private void addBiomeType(final int id, final int count) {
        this.biomeCount.put(id, count);
        this.data += 8;
    }
    
    public void writeToBuffer(final IWriteableBuffer buffer) {
        buffer.writeInt(this.xPos);
        buffer.writeInt(this.zPos);
        buffer.writeInt(this.entityCount.size());
        for (final Map.Entry<String, Integer> entry : this.entityCount.entrySet()) {
            writeString(entry.getKey(), buffer);
            buffer.writeInt(entry.getValue());
        }
        buffer.writeInt(this.tileEntityCount.size());
        for (final Map.Entry<String, Integer> entry : this.tileEntityCount.entrySet()) {
            writeString(entry.getKey(), buffer);
            buffer.writeInt(entry.getValue());
        }
        buffer.writeInt(this.biomeCount.size());
        for (final Map.Entry<Integer, Integer> entry2 : this.biomeCount.entrySet()) {
            buffer.writeInt(entry2.getKey());
            buffer.writeInt(entry2.getValue());
        }
        buffer.writeInt(this.tickableTileEntities);
        buffer.writeInt(this.randomUpdateBlocks);
    }
    
    public static void writeString(final String s, final IWriteableBuffer buffer) {
        buffer.writeShort(s.length());
        for (int i = 0; i < s.length(); ++i) {
            buffer.writeChar(s.charAt(i));
        }
    }
    
    public void readFromBuffer(final IReadableBuffer buffer) {
        this.xPos = buffer.readInt();
        this.zPos = buffer.readInt();
        for (int expected = buffer.readInt(), i = 0; i < expected; ++i) {
            final String s = readString(buffer);
            final int count = buffer.readInt();
            this.entityCount.put(s, count);
            this.entities += count;
        }
        this.entityTypes = this.entityCount.size();
        for (int expected = buffer.readInt(), i = 0; i < expected; ++i) {
            final String s = readString(buffer);
            final int count = buffer.readInt();
            this.tileEntityCount.put(s, count);
            this.tileEntities += count;
        }
        for (int expected = buffer.readInt(), i = 0; i < expected; ++i) {
            final int id = buffer.readInt();
            final int count = buffer.readInt();
            this.biomeCount.put(id, count);
        }
        this.tileTypes = this.tileEntityCount.size();
        this.tickableTileEntities = buffer.readInt();
        this.randomUpdateBlocks = buffer.readInt();
    }
    
    public static String readString(final IReadableBuffer buffer) {
        final StringBuilder builder = new StringBuilder();
        for (int size = buffer.readShort(), i = 0; i < size; ++i) {
            builder.append(buffer.readChar());
        }
        return builder.toString();
    }
    
    public int getBytes() {
        return this.data;
    }
    
    public FilePos getPos() {
        return new FilePos(this.xPos, this.zPos);
    }
    
    static {
        idToClass = (Map)ReflectionHelper.getPrivateValue((Class)TileEntity.class, (Object)null, 1);
        classToID = (Map)ReflectionHelper.getPrivateValue((Class)TileEntity.class, (Object)null, 2);
    }
}
