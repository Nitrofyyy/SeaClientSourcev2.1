// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.huds;

import sea.RenderUtil1;
import sea.event.EventManager;
import java.util.Arrays;
import java.awt.Color;
import sea.SeaClient;
import sea.config.settings.Settings;
import java.util.ArrayList;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.Minecraft;

public class HudMod
{
    public Minecraft mc;
    public FontRenderer fr;
    public ArrayList<Settings> settings;
    public String name;
    public boolean enabled;
    public boolean disable;
    public String description;
    public Component drag;
    public int x;
    public int y;
    
    public HudMod(final String name, final int x, final int y, final String description) {
        this.mc = Minecraft.getMinecraft();
        this.fr = Minecraft.fontRendererObj;
        this.enabled = true;
        this.disable = false;
        this.description = description;
        this.name = name;
        try {
            this.x = (int)SeaClient.INSTANCE.config.config.get(String.valueOf(String.valueOf(name)) + " x");
            this.y = (int)SeaClient.INSTANCE.config.config.get(String.valueOf(String.valueOf(name)) + " y");
            this.setEnabled((boolean)SeaClient.INSTANCE.config.config.get(String.valueOf(String.valueOf(name)) + " enabled"));
        }
        catch (NullPointerException nullpointerexception) {
            nullpointerexception.printStackTrace();
            this.x = x;
            this.y = y;
            this.enabled = false;
        }
        this.settings = new ArrayList<Settings>();
        this.drag = new Component(this.x, this.y, this.getWidth(), y + this.getHieght(), new Color(0, 0, 0, 0).getRGB());
    }
    
    public void addSettings(final Settings... sets) {
        this.settings.add((Settings)Arrays.asList(sets));
    }
    
    public int getWidth() {
        return 50;
    }
    
    public int getHieght() {
        return 50;
    }
    
    public void draw() {
    }
    
    public void renderDummy(final int mouseX, final int mouseY) {
        this.drag.draw(mouseX, mouseY);
    }
    
    public int getX() {
        return this.drag.getPositionX();
    }
    
    public void onEnable() {
        final EventManager eventManager = SeaClient.eventManager;
        EventManager.register(this);
    }
    
    public void onDisable() {
        final EventManager eventManager = SeaClient.eventManager;
        EventManager.unregister(this);
    }
    
    public int getY() {
        return this.drag.getPositionY();
    }
    
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
    
    public void toggle() {
        this.setEnabled(!this.enabled);
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public boolean isDisabled() {
        return !this.enabled;
    }
    
    public boolean isHovered(final int mouseX, final int mouseY) {
        return RenderUtil1.insatance.isHoverd(this.x, this.y, this.x + this.getWidth(), this.y + this.getHieght(), mouseX, mouseY);
    }
}
