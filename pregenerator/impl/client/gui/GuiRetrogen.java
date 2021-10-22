// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.gui;

import org.lwjgl.input.Mouse;
import pregenerator.impl.network.packets.retrogen.RetrogenChangePacket;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiButton;
import pregenerator.base.api.network.PregenPacket;
import pregenerator.impl.network.packets.retrogen.RetrogenCheckPacket;
import pregenerator.ChunkPregenerator;
import pregenerator.impl.retrogen.RetrogenHandler;
import pregenerator.base.impl.misc.SelectionList;
import pregenerator.base.impl.gui.GuiPregenBase;

public class GuiRetrogen extends GuiPregenBase
{
    SelectionList<String> generators;
    
    public GuiRetrogen() {
        this.generators = new SelectionList<String>(RetrogenHandler.INSTANCE.getAllGenerators()).setNoLoop();
        ChunkPregenerator.networking.sendPacketToServer(new RetrogenCheckPacket());
    }
    
    protected void func_146284_a(final GuiButton button) {
        int id = button.field_146127_k;
        if (id == 0) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiWorldStuff(new GuiPregenMenu()));
        }
        else if (id == 1) {
            this.generators.prev();
        }
        else if (id == 2) {
            this.generators.next();
        }
        else if (id >= 10) {
            id -= 10;
            final int i = id / 2;
            final boolean enable = id % 2 == 0;
            if (enable) {
                RetrogenHandler.INSTANCE.enableGenerator(this.generators.getValue(this.generators.getIndex() + i));
            }
            else {
                RetrogenHandler.INSTANCE.disableGenerator(this.generators.getValue(this.generators.getIndex() + i));
            }
            ChunkPregenerator.networking.sendPacketToServer(new RetrogenChangePacket(this.generators.getValue(this.generators.getIndex() + i), enable));
        }
    }
    
    @Override
    public void func_73866_w_() {
        super.func_73866_w_();
        this.registerButton(0, -30, 75, 60, 20, "Back");
        this.registerButton(1, 100, -100, 20, 20, "-");
        this.registerButton(2, 121, -100, 20, 20, "+");
        int yOffset = -65;
        int buttonID = 10;
        for (int i = 0; i + this.generators.getIndex() < this.generators.size() && i < 5; ++i) {
            final boolean enable = RetrogenHandler.INSTANCE.isGeneratorActive(this.generators.getValue(this.generators.getIndex() + i));
            this.registerButton(buttonID, 104, yOffset, 40, 20, "Enable").field_146124_l = !enable;
            this.registerButton(buttonID + 1, 53, yOffset, 50, 20, "Disable").field_146124_l = enable;
            yOffset += 26;
            buttonID += 2;
        }
    }
    
    public boolean func_73868_f() {
        return false;
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        final int effect = Mouse.getDWheel() / 120;
        this.generators.setIndex(this.generators.getIndex() - effect);
        this.func_73866_w_();
        this.func_146276_q_();
        this.drawRectangle(150, 100, 0, 0, -3750202, false);
        int startY = -55;
        int end = 0;
        for (int i = 0; i + this.generators.getIndex() < this.generators.size() && i < 5; ++i) {
            this.drawRectangle(145, 11, 0, startY, -7631989, true);
            startY += 26;
            ++end;
        }
        super.func_73863_a(mouseX, mouseY, partialTicks);
        this.drawCenterText("Retrogen Generators", 4, -95, 4210752);
        this.drawLeftText(this.generators.getIndex() + "/" + this.generators.size(), 144, -75, 4210752);
        startY = -55;
        for (int i = 0; i < end; ++i) {
            final String entry = this.generators.getValue(this.generators.getIndex() + i);
            this.drawSplitText(this.generators.getValue(this.generators.getIndex() + i), -144, -9 + startY, 200, 4210752);
            startY += 26;
        }
    }
}
