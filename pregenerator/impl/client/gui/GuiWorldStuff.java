// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import pregenerator.base.impl.gui.GuiPregenBase;

public class GuiWorldStuff extends GuiPregenBase
{
    GuiScreen lastScreen;
    
    public GuiWorldStuff(final GuiScreen screen) {
        this.lastScreen = screen;
    }
    
    protected void func_146284_a(final GuiButton button) {
        final int id = button.field_146127_k;
        if (id == 0) {
            this.field_146297_k.func_147108_a(this.lastScreen);
        }
        else if (id == 1) {
            GuiWorldView.TILE_ENTITIES.openUI();
            this.field_146297_k.func_147108_a((GuiScreen)GuiWorldView.TILE_ENTITIES);
        }
        else if (id == 2) {
            GuiWorldView.ENTITIES.openUI();
            this.field_146297_k.func_147108_a((GuiScreen)GuiWorldView.ENTITIES);
        }
        else if (id == 3) {
            GuiChunkInfo.INSTANCE.openUI();
            this.field_146297_k.func_147108_a((GuiScreen)GuiChunkInfo.INSTANCE);
        }
        else if (id == 4) {
            GuiStructureView.INSTANCE.openUI();
            this.field_146297_k.func_147108_a((GuiScreen)GuiStructureView.INSTANCE);
        }
        else if (id == 5) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiRetrogen());
        }
        else if (id == 6) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiSelectCommand(this));
        }
    }
    
    @Override
    public void func_73866_w_() {
        super.func_73866_w_();
        this.registerButton(0, -40, 40, 80, 20, "Back");
        this.registerButton(1, -140, -20, 80, 20, "TileEntities");
        this.registerButton(2, 60, -20, 80, 20, "Entities");
        this.registerButton(3, -40, -20, 80, 20, "Chunks");
        this.registerButton(4, -40, 10, 80, 20, "Structures");
        this.registerButton(5, -140, 10, 80, 20, "Retrogen");
        this.registerButton(6, 60, 10, 80, 20, "Commands");
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.func_146276_q_();
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
    
    public boolean func_73868_f() {
        return false;
    }
}
