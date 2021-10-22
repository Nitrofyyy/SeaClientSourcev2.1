// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.huds.impl;

import java.text.DecimalFormat;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import sea.event.EventTarget;
import sea.event.impl.ClientTickEvent;
import sea.event.entity.AttackEntityEvent;
import sea.mods.huds.HudMod;

public class ReachDisplay extends HudMod
{
    String ReachDisplay;
    private long lastAttack;
    private String rangeText;
    private AttackEntityEvent eventAttack;
    private ClientTickEvent event;
    private boolean enabled;
    private int decimals;
    
    public ReachDisplay() {
        super("Reach", 18, 10, "Renders your reach");
        this.ReachDisplay = "";
        this.rangeText = "";
        this.enabled = this.isEnabled();
    }
    
    @Override
    public int getWidth() {
        return this.fr.FONT_HEIGHT;
    }
    
    @Override
    public int getHieght() {
        return 64;
    }
    
    @EventTarget
    public void onTick(final ClientTickEvent event) {
        if (System.currentTimeMillis() - this.lastAttack >= 2000L) {
            this.ReachDisplay = "Hasn't attacked";
        }
    }
    
    @EventTarget
    public void onAttack(final AttackEntityEvent event) {
        Gui.drawRect(this.getX() - 2, this.getY() - 2, this.getX() + this.getHieght(), this.getY() + this.getWidth(), new Color(0, 0, 0, 170).getRGB());
        try {
            if (this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && this.isEnabled() && this.mc.objectMouseOver.entityHit.getEntityId() == event.getTarget().getEntityId()) {
                final Vec3 vec3 = this.mc.getRenderViewEntity().getPositionEyes(1.0f);
                final double range = this.mc.objectMouseOver.hitVec.distanceTo(vec3);
                this.ReachDisplay = String.valueOf(this.getFormatter().format(range)) + " blocks";
            }
            else {
                this.ReachDisplay = "Hasn't attacked";
            }
            this.lastAttack -= System.currentTimeMillis();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
        this.fr.drawString(this.ReachDisplay, this.getX(), this.getY(), -1);
    }
    
    @Override
    public void draw() {
        this.onAttack(this.eventAttack);
        super.draw();
    }
    
    @Override
    public void renderDummy(final int mouseX, final int mouseY) {
        Gui.drawRect(this.getX() - 2, this.getY() - 2, this.getX() + this.getHieght(), this.getY() + this.getWidth(), new Color(0, 0, 0, 170).getRGB());
        this.fr.drawString(this.ReachDisplay, this.getX(), this.getY(), -1);
        super.renderDummy(mouseX, mouseY);
    }
    
    private DecimalFormat getFormatter() {
        final StringBuilder format = new StringBuilder("0.");
        for (int i = 0; this.decimals > i; ++i) {
            format.append('0');
        }
        return new DecimalFormat(format.toString());
    }
}
