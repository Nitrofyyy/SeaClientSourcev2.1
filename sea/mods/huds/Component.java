// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.huds;

import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.Gui;

public class Component
{
    private int x;
    private int y;
    private int width;
    private int height;
    private int color;
    private int lastX;
    private int lastY;
    private boolean dragging;
    
    public Component(final int x, final int y, final int width, final int height, final int color) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.color = color;
    }
    
    public int getPositionX() {
        return this.x;
    }
    
    public int getPositionY() {
        return this.y;
    }
    
    public void setxPosition(final int x) {
        this.x = x;
    }
    
    public void setyPosition(final int y) {
        this.y = y;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getColor() {
        return this.color;
    }
    
    public void setColor(final int color) {
        this.color = color;
    }
    
    public void draw(final int mouseX, final int mouseY) {
        this.draggingFix(mouseX, mouseY);
        Gui.drawRect(this.getPositionX(), this.getPositionY(), this.getPositionX() + this.getWidth(), this.getPositionY() + this.getHeight(), this.getColor());
        final boolean mouseOverX = mouseX >= this.getPositionX() && mouseX <= this.getPositionX() + this.getWidth();
        final boolean mouseOverY = mouseY >= this.getPositionY() && mouseY <= this.getPositionY() + this.getHeight();
        if (mouseOverX && mouseOverY && Mouse.isButtonDown(0) && !this.dragging) {
            this.lastX = this.x - mouseX;
            this.lastY = this.y - mouseY;
            this.dragging = true;
        }
    }
    
    private void draggingFix(final int mouseX, final int mouseY) {
        if (this.dragging) {
            this.x = mouseX + this.lastX;
            this.y = mouseY + this.lastY;
            if (!Mouse.isButtonDown(0)) {
                this.dragging = false;
            }
        }
    }
}
