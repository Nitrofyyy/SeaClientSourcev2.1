// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.gui;

import java.net.URL;
import java.awt.Desktop;
import net.minecraft.client.gui.GuiScreen;
import java.util.Arrays;
import java.awt.Color;
import pregenerator.base.impl.gui.PatreonTexture;
import pregenerator.impl.client.TrackerInfo;
import pregenerator.impl.client.PregenInfo;
import pregenerator.base.api.network.PregenPacket;
import pregenerator.impl.network.packets.PermissionRequestPacket;
import pregenerator.ChunkPregenerator;
import pregenerator.impl.client.ClientHandler;
import net.minecraft.client.gui.GuiButton;
import pregenerator.base.impl.gui.GuiPregenBase;

public class GuiPregenMenu extends GuiPregenBase
{
    boolean permission;
    
    public GuiPregenMenu() {
        this.permission = false;
    }
    
    public void onPermissionReceived(final boolean result) {
        this.permission = result;
        this.field_146292_n.get(5).field_146124_l = result;
    }
    
    @Override
    public void func_73866_w_() {
        super.func_73866_w_();
        final PregenInfo handler = ClientHandler.INSTANCE.info;
        this.registerButton(0, 10, -30, 100, 20, handler.shouldShow ? "Disable PregenUI" : "Enable PregenUI");
        this.registerButton(1, -110, -30, 100, 20, "PregenUI Options");
        this.registerButton(2, -40, 50, 80, 20, "Back");
        final TrackerInfo info = ClientHandler.INSTANCE.tracker;
        this.registerButton(3, 10, -9, 100, 20, info.shouldShow ? "Disable TrackerUI" : "Enable TrackerUI");
        this.registerButton(4, -110, -9, 100, 20, "TrackerUI Options");
        this.registerButton(5, -90, 12, 80, 20, "World View").field_146124_l = false;
        ChunkPregenerator.networking.sendPacketToServer(new PermissionRequestPacket());
        this.registerButton(10, 45, 50, 20, 20, "");
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.func_146276_q_();
        super.func_73863_a(mouseX, mouseY, partialTicks);
        PatreonTexture.bindTexture();
        this.renderTextureWithOffset(48.0f, 52.0f, 15.0f, 15.0f, this.field_73735_i);
        final TrackerInfo info = ClientHandler.INSTANCE.tracker;
        if (info.shouldShow && !info.running) {
            this.drawText("Pregen Tracker is not enabled.", 10, 12, Color.WHITE.getRGB());
            this.drawText("use /pregen utils enableTracking", 10, 22, Color.WHITE.getRGB());
            this.drawText("to Enable it", 10, 34, Color.WHITE.getRGB());
        }
        if (this.isInsideBox(mouseX, mouseY, 45, 65, 50, 70)) {
            this.drawListText(Arrays.asList("Support Speiger on Patreon"), mouseX, mouseY);
        }
    }
    
    public boolean func_73868_f() {
        return false;
    }
    
    protected void func_146284_a(final GuiButton button) {
        final int id = button.field_146127_k;
        if (id == 0) {
            final PregenInfo handler = ClientHandler.INSTANCE.info;
            handler.setShow(!handler.shouldShow);
            button.field_146126_j = (handler.shouldShow ? "Disable PregenUI" : "Enable PregenUI");
        }
        else if (id == 1) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiUIOptions(this));
        }
        else if (id == 2) {
            this.field_146297_k.func_147108_a((GuiScreen)null);
        }
        else if (id == 3) {
            final TrackerInfo info = ClientHandler.INSTANCE.tracker;
            info.setShow(!info.shouldShow);
            button.field_146126_j = (info.shouldShow ? "Disable TrackerUI" : "Enable TrackerUI");
        }
        else if (id == 4) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiTrackerOptions(this));
        }
        else if (id == 5 && this.permission) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiWorldStuff(this));
        }
        else if (id == 10) {
            try {
                Desktop.getDesktop().browse(new URL("https://www.patreon.com/Speiger").toURI());
            }
            catch (Exception ex) {}
        }
    }
}
