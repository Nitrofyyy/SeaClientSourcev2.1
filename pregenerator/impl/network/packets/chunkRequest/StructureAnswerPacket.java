// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.network.packets.chunkRequest;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pregenerator.impl.client.gui.GuiStructureView;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Iterator;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.base.api.network.IReadableBuffer;
import java.util.ArrayList;
import pregenerator.impl.structure.StructureData;
import java.util.List;
import pregenerator.base.api.network.PregenPacket;

public class StructureAnswerPacket extends PregenPacket
{
    byte type;
    List<StructureData> data;
    
    public StructureAnswerPacket() {
        this.data = new ArrayList<StructureData>();
    }
    
    public void setType(final int type) {
        this.type = (byte)type;
    }
    
    public void addData(final StructureData entry) {
        this.data.add(entry);
    }
    
    public boolean hasData() {
        return this.data.size() > 0;
    }
    
    @Override
    public void read(final IReadableBuffer buffer) {
        this.type = buffer.readByte();
        if (this.type == -1) {
            return;
        }
        for (int size = buffer.readInt(), i = 0; i < size; ++i) {
            final StructureData entry = new StructureData();
            entry.readFromBuffer(buffer);
            this.data.add(entry);
        }
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        buffer.writeByte(this.type);
        if (this.type == -1) {
            return;
        }
        buffer.writeInt(this.data.size());
        for (final StructureData entry : this.data) {
            entry.writeToBuffer(buffer);
        }
    }
    
    @Override
    public void handle(final EntityPlayer player) {
        this.handleClient();
    }
    
    @SideOnly(Side.CLIENT)
    public void handleClient() {
        if (this.type == -1) {
            GuiStructureView.INSTANCE.noDataFound();
            return;
        }
        GuiStructureView.INSTANCE.addData(this.data, this.type);
    }
}
