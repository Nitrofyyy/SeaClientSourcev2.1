// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.structure;

import pregenerator.impl.misc.FilePos;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.IWriteableBuffer;
import net.minecraft.world.gen.structure.StructureStart;

public class StructureData
{
    String type;
    short parts;
    boolean suitable;
    int x;
    int z;
    
    public StructureData() {
    }
    
    public StructureData(final String id, final StructureStart start) {
        this.type = id;
        this.x = start.func_143019_e() * 16;
        this.z = start.func_143018_f() * 16;
        this.suitable = start.func_75069_d();
        this.parts = (short)start.func_75073_b().size();
    }
    
    public int getBytes() {
        return 13 + this.type.length() * 2;
    }
    
    public void writeToBuffer(final IWriteableBuffer buffer) {
        buffer.writeInt(this.x);
        buffer.writeInt(this.z);
        buffer.writeShort(this.parts);
        buffer.writeBoolean(this.suitable);
        buffer.writeShort(this.type.length());
        for (int i = 0; i < this.type.length(); ++i) {
            buffer.writeChar(this.type.charAt(i));
        }
    }
    
    public void readFromBuffer(final IReadableBuffer buffer) {
        this.x = buffer.readInt();
        this.z = buffer.readInt();
        this.parts = buffer.readShort();
        this.suitable = buffer.readBoolean();
        final int size = buffer.readShort();
        final StringBuilder builder = new StringBuilder(size);
        for (int i = 0; i < size; ++i) {
            builder.append(buffer.readChar());
        }
        this.type = builder.toString();
    }
    
    public String getType() {
        return this.type;
    }
    
    public short getParts() {
        return this.parts;
    }
    
    public boolean isSuitable() {
        return this.suitable;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getZ() {
        return this.z;
    }
    
    public FilePos getPos() {
        return new FilePos(this.x, this.z);
    }
}
