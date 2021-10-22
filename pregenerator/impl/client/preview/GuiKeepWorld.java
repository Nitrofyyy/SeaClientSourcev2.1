// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.preview;

import java.util.Arrays;
import java.awt.Color;
import pregenerator.base.impl.gui.PatreonTexture;
import java.net.URL;
import java.awt.Desktop;
import java.io.File;
import pregenerator.impl.client.preview.world.WorldSeed;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiButton;
import pregenerator.base.impl.gui.GuiPregenBase;

public class GuiKeepWorld extends GuiPregenBase
{
    GuiSeedPreview preview;
    boolean safe;
    int state;
    
    public GuiKeepWorld(final GuiSeedPreview last, final boolean saveEnough) {
        this.state = 0;
        this.preview = last;
        this.safe = saveEnough;
    }
    
    @Override
    public void func_73866_w_() {
        super.func_73866_w_();
        this.registerButton(0, -142, 50, 120, 20, "Back");
        this.registerButton(1, 2, 50, 120, 20, "Continue");
        this.registerButton(10, -20, 50, 20, 20, "");
    }
    
    protected void func_146284_a(final GuiButton button) {
        final int id = button.field_146127_k;
        if (id == 0) {
            this.preview.onReopening();
            this.field_146297_k.func_147108_a((GuiScreen)this.preview);
        }
        else if (id == 1) {
            if (!this.preview.removePreview(true)) {
                if (this.state == 1) {
                    this.field_146297_k.func_147108_a(this.preview.seed.getMainMenu());
                }
                this.state = 1;
                return;
            }
            WorldSeed.getPreviewFolder().renameTo(new File("saves/" + this.preview.seed.getFolderName()));
            this.field_146297_k.func_147108_a(this.preview.seed.getMainMenu());
        }
        else if (id == 10) {
            try {
                Desktop.getDesktop().browse(new URL("https://www.patreon.com/Speiger").toURI());
            }
            catch (Exception ex) {}
        }
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.func_146276_q_();
        super.func_73863_a(mouseX, mouseY, partialTicks);
        PatreonTexture.bindTexture();
        this.renderTextureWithOffset(-17.0f, 52.0f, 15.0f, 15.0f, this.field_73735_i);
        if (!this.safe) {
            this.drawCenterText("One or more of your World's is only Terrain Generated!", 0, -(this.field_146295_m / 5), Color.white.getRGB());
            this.drawCenterText("Terrain Only Generation has only small Performance Improvements", 0, -(this.field_146295_m / 5) + 15, Color.white.getRGB());
            this.drawCenterText("Unless you know what you are doing. Please go back and \"Add Post\"", 0, -(this.field_146295_m / 5) + 30, Color.white.getRGB());
            if (this.state == 1) {
                this.drawCenterText("Tool was not able to transfer the Data out of the Temporary Folder", 0, -(this.field_146295_m / 5) + 45, Color.RED.getRGB());
                this.drawCenterText("Please Stop the game and rename the \"Preview\" Folder Name to something else", 0, -(this.field_146295_m / 5) + 55, Color.RED.getRGB());
                this.drawCenterText("Press Continue to retry and to return to the main screen", 0, -(this.field_146295_m / 5) + 65, Color.RED.getRGB());
            }
        }
        else {
            this.drawCenterText("Are you sure that your World is ready to keep?", 0, -(this.field_146295_m / 5), Color.white.getRGB());
            if (this.state == 1) {
                this.drawCenterText("Tool was not able to transfer the Data out of the Temporary Folder", 0, -(this.field_146295_m / 5) + 15, Color.RED.getRGB());
                this.drawCenterText("Please Stop the game and rename the \"Preview\" Folder Name to something else", 0, -(this.field_146295_m / 5) + 25, Color.RED.getRGB());
                this.drawCenterText("Press Continue to retry and to return to the main screen", 0, -(this.field_146295_m / 5) + 35, Color.RED.getRGB());
            }
        }
        if (this.isInsideBox(mouseX, mouseY, -20, 50, 0, 70)) {
            this.drawListText(Arrays.asList("Support Speiger on Patreon"), mouseX, mouseY);
        }
    }
}
