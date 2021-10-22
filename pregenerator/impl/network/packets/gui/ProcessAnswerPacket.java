// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.network.packets.gui;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import pregenerator.impl.client.gui.GuiSelectCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.PregenPacket;

public class ProcessAnswerPacket extends PregenPacket
{
    boolean running;
    boolean hasTasks;
    
    public ProcessAnswerPacket() {
    }
    
    public ProcessAnswerPacket(final boolean running, final boolean hasTasks) {
        this.running = running;
        this.hasTasks = hasTasks;
    }
    
    @Override
    public void read(final IReadableBuffer buffer) {
        this.running = buffer.readBoolean();
        this.hasTasks = buffer.readBoolean();
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        buffer.writeBoolean(this.running);
        buffer.writeBoolean(this.hasTasks);
    }
    
    @Override
    public void handle(final EntityPlayer player) {
        this.handleClient();
    }
    
    @SideOnly(Side.CLIENT)
    public void handleClient() {
        final GuiScreen screen = Minecraft.func_71410_x().field_71462_r;
        if (screen instanceof GuiSelectCommand) {
            final GuiSelectCommand com = (GuiSelectCommand)screen;
            com.hasTask = this.hasTasks;
            com.running = this.running;
        }
    }
}
