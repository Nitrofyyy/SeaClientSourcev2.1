// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.impl;

import sea.event.EventTarget;
import sea.mods.huds.HudList;
import sea.event.impl.EventUpdate;
import net.minecraft.client.Minecraft;
import sea.mods.Module;

public class ToggleSprint extends Module
{
    public Minecraft mc;
    
    public ToggleSprint() {
        super("Toggle Sprint", "Toggles your sprinting.");
        this.mc = Minecraft.getMinecraft();
    }
    
    @EventTarget
    private void onUpdate(final EventUpdate event) {
        if (HudList.sprint.isEnabled() && !this.mc.thePlayer.isBlocking() && !this.mc.thePlayer.isSneaking() && !this.mc.gameSettings.keyBindBack.isKeyDown() && this.mc.gameSettings.keyBindForward.isKeyDown() && !this.mc.thePlayer.isEating() && !this.mc.thePlayer.isPotionActive(15)) {
            this.mc.thePlayer.setSprinting(true);
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.mc.thePlayer.setSprinting(false);
    }
}
