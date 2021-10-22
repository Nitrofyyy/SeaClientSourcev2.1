// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.network.packets;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pregenerator.impl.client.gui.GuiPregenMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.PregenPacket;

public class PermissionAnswerPacket extends PregenPacket
{
    boolean perm;
    
    public PermissionAnswerPacket() {
    }
    
    public PermissionAnswerPacket(final boolean has) {
        this.perm = has;
    }
    
    @Override
    public void read(final IReadableBuffer buffer) {
        this.perm = buffer.readBoolean();
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        buffer.writeBoolean(this.perm);
    }
    
    @Override
    public void handle(final EntityPlayer player) {
        this.handleClient();
    }
    
    @SideOnly(Side.CLIENT)
    public void handleClient() {
        final Minecraft mc = Minecraft.func_71410_x();
        if (mc.field_71462_r instanceof GuiPregenMenu) {
            final GuiPregenMenu menu = (GuiPregenMenu)mc.field_71462_r;
            menu.onPermissionReceived(this.perm);
        }
    }
}
