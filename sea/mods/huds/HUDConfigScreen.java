// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.huds;

import java.io.IOException;
import sea.cosmetic.gui.CosmeticGui;
import sea.mods.gui.ModToggleGui;
import java.util.Iterator;
import sea.SeaClient;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.GuiScreen;

public class HUDConfigScreen extends GuiScreen
{
    @Override
    public void initGui() {
        new ScaledResolution(this.mc);
        super.initGui();
        this.buttonList.add(new GuiButton(6340, HUDConfigScreen.width / 2 - 50, HUDConfigScreen.height / 2 - 10, 100, 20, "ClickGui"));
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        for (final HudMod m : SeaClient.INSTANCE.hudList.hudMods) {
            if (m.isEnabled()) {
                m.renderDummy(mouseX, mouseY);
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 6340: {
                this.mc.displayGuiScreen(new ModToggleGui());
                break;
            }
        }
        if (button.id == 19) {
            this.mc.displayGuiScreen(new CosmeticGui());
        }
        super.actionPerformed(button);
    }
}
