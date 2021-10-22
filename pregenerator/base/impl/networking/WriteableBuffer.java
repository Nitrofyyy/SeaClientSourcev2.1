// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.base.impl.networking;

import io.netty.buffer.ByteBuf;
import pregenerator.base.api.network.IWriteableBuffer;

public class WriteableBuffer implements IWriteableBuffer
{
    ByteBuf buf;
    
    public WriteableBuffer(final ByteBuf buffer) {
        this.buf = buffer;
    }
    
    @Override
    public void writeBoolean(final boolean value) {
        this.buf.writeBoolean(value);
    }
    
    @Override
    public void writeByte(final int value) {
        this.buf.writeByte(value);
    }
    
    @Override
    public void writeShort(final int value) {
        this.buf.writeShort(value);
    }
    
    @Override
    public void writeInt(final int value) {
        this.buf.writeInt(value);
    }
    
    @Override
    public void writeLong(final long value) {
        this.buf.writeLong(value);
    }
    
    @Override
    public void writeFloat(final float value) {
        this.buf.writeFloat(value);
    }
    
    @Override
    public void writeDouble(final double value) {
        this.buf.writeDouble(value);
    }
    
    @Override
    public void writeChar(final char value) {
        this.buf.writeChar(value);
    }
    
    @Override
    public void writeByteArray(final byte[] data) {
        this.buf.writeInt(data.length);
        this.buf.writeBytes(data);
    }
}
