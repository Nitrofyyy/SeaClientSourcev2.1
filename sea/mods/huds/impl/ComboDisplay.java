// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.huds.impl;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import sea.event.Priority;
import sea.event.EventTarget;
import sea.event.entity.AttackEntityEvent;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import sea.mods.huds.HudMod;

public class ComboDisplay extends HudMod
{
    private long sentAttackTime;
    private long lastHitTime;
    private int lastAttackId;
    private int currentCombo;
    S19PacketEntityStatus packet;
    AttackEntityEvent event;
    private int sentAttack;
    
    public ComboDisplay() {
        super("ComboDisplay", 12, 7, "Displays your combo");
        this.packet = new S19PacketEntityStatus();
        this.sentAttack = -1;
    }
    
    @EventTarget(priority = Priority.HIGHEST)
    public void onAttack(final AttackEntityEvent event) {
        if (!event.isCanceled() && event.entity == this.mc.thePlayer) {
            this.sentAttack = event.target.getEntityId();
            this.sentAttackTime = System.currentTimeMillis();
        }
    }
    
    public void onEntityStatusPacket(final S19PacketEntityStatus packet) {
        if (packet.getOpCode() == 2) {
            final Entity target = packet.getEntity(this.mc.theWorld);
            if (target != null) {
                if (this.sentAttack != -1 && target.getEntityId() == this.sentAttack) {
                    this.sentAttack = -1;
                    if (System.currentTimeMillis() - this.sentAttackTime > 2000L) {
                        this.sentAttackTime = 0L;
                        this.currentCombo = 0;
                        return;
                    }
                    if (this.lastAttackId == target.getEntityId()) {
                        ++this.currentCombo;
                    }
                    else {
                        this.currentCombo = 1;
                    }
                    this.lastHitTime = System.currentTimeMillis();
                    this.lastAttackId = target.getEntityId();
                }
                else if (target.getEntityId() == this.mc.thePlayer.getEntityId()) {
                    this.currentCombo = 0;
                }
            }
        }
    }
    
    public long getSentAttackTime() {
        return this.sentAttackTime;
    }
    
    public long getLastHitTime() {
        return this.lastHitTime;
    }
    
    public int getLastAttackId() {
        return this.lastAttackId;
    }
    
    public int getCurrentCombo() {
        return this.currentCombo;
    }
    
    public int getSentAttack() {
        return this.sentAttack;
    }
    
    @Override
    public void draw() {
        this.fr.drawStringWithShadow("Combo: " + this.getCurrentCombo(), (float)this.getX(), (float)this.getY(), -1);
        super.draw();
    }
    
    @Override
    public void renderDummy(final int mouseX, final int mouseY) {
        this.fr.drawStringWithShadow("Combo: " + this.getCurrentCombo(), (float)this.getX(), (float)this.getY(), -1);
        super.renderDummy(mouseX, mouseY);
    }
}
