// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.base.impl.gui;

import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiSlider extends GuiButtonExt
{
    public double sliderValue;
    public String dispString;
    public boolean dragging;
    public boolean showDecimal;
    public double minValue;
    public double maxValue;
    public int precision;
    public ISlider parent;
    public String suffix;
    public boolean drawString;
    public double scrollEffect;
    
    public GuiSlider(final int id, final int xPos, final int yPos, final int width, final int height, final String prefix, final String suf, final double minVal, final double maxVal, final double currentVal, final boolean showDec, final boolean drawStr) {
        this(id, xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, null);
    }
    
    public GuiSlider(final int id, final int xPos, final int yPos, final int width, final int height, final String prefix, final String suf, final double minVal, final double maxVal, final double currentVal, final boolean showDec, final boolean drawStr, final ISlider par) {
        super(id, xPos, yPos, width, height, prefix);
        this.sliderValue = 1.0;
        this.dispString = "";
        this.dragging = false;
        this.showDecimal = true;
        this.minValue = 0.0;
        this.maxValue = 5.0;
        this.precision = 1;
        this.parent = null;
        this.suffix = "";
        this.drawString = true;
        this.scrollEffect = 0.0;
        this.minValue = minVal;
        this.maxValue = maxVal;
        this.sliderValue = (currentVal - this.minValue) / (this.maxValue - this.minValue);
        this.dispString = prefix;
        this.parent = par;
        this.suffix = suf;
        this.showDecimal = showDec;
        String val;
        if (this.showDecimal) {
            val = Double.toString(this.sliderValue * (this.maxValue - this.minValue) + this.minValue);
            this.precision = Math.min(val.substring(val.indexOf(".") + 1).length(), 4);
        }
        else {
            val = Integer.toString((int)Math.round(this.sliderValue * (this.maxValue - this.minValue) + this.minValue));
            this.precision = 0;
        }
        this.field_146126_j = this.dispString + val + this.suffix;
        if (!(this.drawString = drawStr)) {
            this.field_146126_j = "";
        }
    }
    
    public GuiSlider(final int id, final int xPos, final int yPos, final String displayStr, final double minVal, final double maxVal, final double currentVal, final ISlider par) {
        this(id, xPos, yPos, 150, 20, displayStr, "", minVal, maxVal, currentVal, true, true, par);
    }
    
    public GuiSlider setScrollEffect(final double value) {
        this.scrollEffect = value;
        return this;
    }
    
    public int func_146114_a(final boolean par1) {
        return 0;
    }
    
    public void func_146112_a(final Minecraft mc, final int mouseX, final int mouseY) {
        super.func_146112_a(mc, mouseX, mouseY);
        if (this.scrollEffect != 0.0 && this.field_146123_n && this.field_146124_l && this.field_146125_m) {
            double effect = Mouse.getDWheel() / 120.0;
            if (effect != 0.0) {
                effect *= (GuiScreen.func_146271_m() ? 10.0 : 1.0);
                this.setValue(this.getValueInt() + effect);
                this.updateSlider();
            }
        }
    }
    
    protected void func_146119_b(final Minecraft par1Minecraft, final int par2, final int par3) {
        if (this.field_146125_m) {
            if (this.dragging) {
                this.sliderValue = (par2 - (this.field_146128_h + 4)) / (float)(this.field_146120_f - 8);
                this.updateSlider();
            }
            GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
            GuiUtils.drawContinuousTexturedBox(GuiSlider.field_146122_a, this.field_146128_h + (int)(this.sliderValue * (this.field_146120_f - 8)), this.field_146129_i, 0, 66, 8, this.field_146121_g, 200, 20, 2, 3, 2, 2, this.field_73735_i);
        }
    }
    
    public boolean func_146116_c(final Minecraft par1Minecraft, final int par2, final int par3) {
        if (super.func_146116_c(par1Minecraft, par2, par3)) {
            this.sliderValue = (par2 - (this.field_146128_h + 4)) / (float)(this.field_146120_f - 8);
            this.updateSlider();
            return this.dragging = true;
        }
        return false;
    }
    
    public void updateSlider() {
        if (this.sliderValue < 0.0) {
            this.sliderValue = 0.0;
        }
        if (this.sliderValue > 1.0) {
            this.sliderValue = 1.0;
        }
        String val;
        if (this.showDecimal) {
            val = Double.toString(this.sliderValue * (this.maxValue - this.minValue) + this.minValue);
            if (val.substring(val.indexOf(".") + 1).length() > this.precision) {
                val = val.substring(0, val.indexOf(".") + this.precision + 1);
                if (val.endsWith(".")) {
                    val = val.substring(0, val.indexOf(".") + this.precision);
                }
            }
            else {
                while (val.substring(val.indexOf(".") + 1).length() < this.precision) {
                    val += "0";
                }
            }
        }
        else {
            val = Integer.toString((int)Math.round(this.sliderValue * (this.maxValue - this.minValue) + this.minValue));
        }
        if (this.drawString) {
            this.field_146126_j = this.dispString + val + this.suffix;
        }
        if (this.parent != null) {
            this.parent.onChangeSliderValue(this);
        }
    }
    
    public void func_146118_a(final int par1, final int par2) {
        this.dragging = false;
    }
    
    public int getValueInt() {
        return (int)Math.round(this.sliderValue * (this.maxValue - this.minValue) + this.minValue);
    }
    
    public double getValue() {
        return this.sliderValue * (this.maxValue - this.minValue) + this.minValue;
    }
    
    public void setValue(final double d) {
        this.sliderValue = (d - this.minValue) / (this.maxValue - this.minValue);
    }
    
    public interface ISlider
    {
        void onChangeSliderValue(final GuiSlider p0);
    }
}
