// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.base.api.network;

public interface IReadableBuffer
{
    boolean readBoolean();
    
    byte readByte();
    
    short readShort();
    
    int readInt();
    
    long readLong();
    
    float readFloat();
    
    double readDouble();
    
    char readChar();
    
    byte[] readByteArray();
}
