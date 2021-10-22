// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.huds.impl;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import sea.mods.huds.HudMod;

public class DirectionMod extends HudMod
{
    protected static ScaledResolution scaledResolution;
    public static String markerColor;
    public Gui gui;
    public static int compassIndex;
    String owner;
    boolean skid;
    
    static {
        DirectionMod.markerColor = "c";
        DirectionMod.compassIndex = 0;
    }
    
    public DirectionMod() {
        super("Direction & XYZ", 90, 8, "Render the XYZ nad Direction");
        this.gui = new Gui();
        this.owner = "me BLueDD";
        this.skid = false;
    }
    
    @Override
    public int getWidth() {
        return 102;
    }
    
    @Override
    public int getHieght() {
        return 15;
    }
    
    @Override
    public void draw() {
        Gui.drawRect(this.getX() - 2, this.getY() - 2, this.getX() - 20 + this.getWidth() + 10, this.getY() + this.getHieght() + 27, new Color(0, 0, 0, 170).getRGB());
        this.fr.drawStringWithShadow(String.format("§bX§3: §f %.0f", this.mc.renderViewEntity.posX), (float)this.getX(), (float)this.getY(), -1);
        this.fr.drawStringWithShadow(String.format("§bY§3: §f%.0f", this.mc.renderViewEntity.posY), (float)this.getX(), (float)(this.getY() + this.fr.FONT_HEIGHT + 2), -1);
        this.fr.drawStringWithShadow(String.format("§bZ§3: §f%.0f", this.mc.renderViewEntity.posZ), (float)this.getX(), (float)(this.getY() + this.fr.FONT_HEIGHT * 2 + 4), -1);
        this.fr.drawStringWithShadow(this.mc.theWorld.getBiomeGenForCoords(this.mc.thePlayer.getPosition()).biomeName, (float)this.getX(), (float)(this.getY() + this.fr.FONT_HEIGHT + 23), -1);
        String direction = "";
        switch (this.getDirectionFacing()) {
            case 0: {
                direction = "§bS§f";
                break;
            }
            case 1: {
                direction = "§3SW§f";
                break;
            }
            case 2: {
                direction = "§1W§f";
                break;
            }
            case 3: {
                direction = "§9NW§f";
                break;
            }
            case 4: {
                direction = "§eN§f";
                break;
            }
            case 5: {
                direction = "§cNE§f";
                break;
            }
            case 6: {
                direction = "§4E§f";
                break;
            }
            case 7: {
                direction = "§2SE§f";
                break;
            }
        }
        this.fr.drawStringWithShadow(direction, (float)(this.getX() + 54), (float)(this.getY() + this.fr.FONT_HEIGHT + 2), -1);
        this.drawPlayerHead(this.getX() - 38, this.getY(), 40);
        super.draw();
    }
    
    @Override
    public void renderDummy(final int mouseX, final int mouseY) {
        this.fr.drawStringWithShadow(String.format("X: %.0f", this.mc.renderViewEntity.posX), (float)this.getX(), (float)this.getY(), -1);
        this.fr.drawStringWithShadow(String.format("Y: %.0f", this.mc.renderViewEntity.posY), (float)this.getX(), (float)(this.getY() + this.fr.FONT_HEIGHT + 2), -1);
        this.fr.drawStringWithShadow(String.format("Z: %.0f", this.mc.renderViewEntity.posZ), (float)this.getX(), (float)(this.getY() + this.fr.FONT_HEIGHT * 2 + 4), -1);
        String direction = "";
        switch (this.getDirectionFacing()) {
            case 0: {
                direction = "S";
                break;
            }
            case 1: {
                direction = "SW";
                break;
            }
            case 2: {
                direction = "W";
                break;
            }
            case 3: {
                direction = "NW";
                break;
            }
            case 4: {
                direction = "N";
                break;
            }
            case 5: {
                direction = "NE";
                break;
            }
            case 6: {
                direction = "E";
                break;
            }
            case 7: {
                direction = "SE";
                break;
            }
        }
        this.fr.drawStringWithShadow(direction, (float)(this.getX() + 54), (float)(this.getY() + this.fr.FONT_HEIGHT + 2), -1);
        this.drawPlayerHead(this.getX() - 38, this.getY(), 40);
        super.renderDummy(mouseX, mouseY);
    }
    
    private static int getX(final int width) {
        return width;
    }
    
    private static int getY(final int rowCount, final int height) {
        return height;
    }
    
    private int getDirectionFacing() {
        int yaw = (int)this.mc.renderViewEntity.rotationYaw;
        yaw += 360;
        yaw += 22;
        yaw %= 360;
        return yaw / 45;
    }
    
    public void drawPlayerHead(final int x, final int y, final int width) {
        GlStateManager.pushMatrix();
        final float scale = (float)(width / 32);
        GlStateManager.scale(scale, scale, scale);
        this.mc.getTextureManager().bindTexture(this.mc.thePlayer.getLocationSkin());
        GL11.glEnable(3042);
        this.gui.drawTexturedModalRect(x / scale, y / scale, 32, 32, 32, 32);
        GL11.glDisable(3042);
        GlStateManager.popMatrix();
    }
}
