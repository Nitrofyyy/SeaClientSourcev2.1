// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.base.impl.networking;

import io.netty.buffer.ByteBuf;
import pregenerator.base.api.network.IReadableBuffer;

public class ReadableBuffer implements IReadableBuffer
{
    ByteBuf buf;
    
    public ReadableBuffer(final ByteBuf buffer) {
        this.buf = buffer;
    }
    
    @Override
    public boolean readBoolean() {
        return this.buf.readBoolean();
    }
    
    @Override
    public byte readByte() {
        return this.buf.readByte();
    }
    
    @Override
    public short readShort() {
        return this.buf.readShort();
    }
    
    @Override
    public int readInt() {
        return this.buf.readInt();
    }
    
    @Override
    public long readLong() {
        return this.buf.readLong();
    }
    
    @Override
    public float readFloat() {
        return this.buf.readFloat();
    }
    
    @Override
    public double readDouble() {
        return this.buf.readDouble();
    }
    
    @Override
    public char readChar() {
        return this.buf.readChar();
    }
    
    @Override
    public byte[] readByteArray() {
        final byte[] array = new byte[this.buf.readInt()];
        this.buf.readBytes(array);
        return array;
    }
}
