// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.base.api.network;

public interface IWriteableBuffer
{
    void writeBoolean(final boolean p0);
    
    void writeByte(final int p0);
    
    void writeShort(final int p0);
    
    void writeInt(final int p0);
    
    void writeLong(final long p0);
    
    void writeFloat(final float p0);
    
    void writeDouble(final double p0);
    
    void writeChar(final char p0);
    
    void writeByteArray(final byte[] p0);
}
