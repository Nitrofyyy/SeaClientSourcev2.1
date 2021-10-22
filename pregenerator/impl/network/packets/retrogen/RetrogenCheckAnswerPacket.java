// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.network.packets.retrogen;

import pregenerator.impl.retrogen.RetrogenHandler;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Iterator;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.base.api.network.IReadableBuffer;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;
import pregenerator.base.api.network.PregenPacket;

public class RetrogenCheckAnswerPacket extends PregenPacket
{
    List<String> list;
    
    public RetrogenCheckAnswerPacket(final Set<String> active) {
        (this.list = new ArrayList<String>()).addAll(active);
    }
    
    public RetrogenCheckAnswerPacket() {
        this.list = new ArrayList<String>();
    }
    
    @Override
    public void read(final IReadableBuffer buffer) {
        for (int size = buffer.readInt(), x = 0; x < size; ++x) {
            final int chars = buffer.readInt();
            final StringBuilder builder = new StringBuilder();
            for (int y = 0; y < chars; ++y) {
                builder.append(buffer.readChar());
            }
            this.list.add(builder.toString());
        }
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        buffer.writeInt(this.list.size());
        for (final String s : this.list) {
            buffer.writeInt(s.length());
            for (int i = 0; i < s.length(); ++i) {
                buffer.writeChar(s.charAt(i));
            }
        }
    }
    
    @Override
    public void handle(final EntityPlayer player) {
        RetrogenHandler.INSTANCE.updateActiveGenerators(this.list);
    }
}
