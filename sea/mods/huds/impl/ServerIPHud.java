// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.huds.impl;

import sea.mods.huds.HudMod;

public class ServerIPHud extends HudMod
{
    public ServerIPHud() {
        super("ServerIpDisplay", 50, 60, "Render the server ip that u join");
    }
    
    @Override
    public int getHieght() {
        return 50;
    }
    
    @Override
    public int getWidth() {
        return 90;
    }
    
    @Override
    public void draw() {
        if (this.mc.isSingleplayer()) {
            this.fr.drawStringWithShadow("§3[§b §fSingleplayer §3]§f", (float)this.getX(), (float)this.getY(), -1);
        }
        else {
            this.fr.drawStringWithShadow("§3[§b" + this.mc.getCurrentServerData().serverIP + " §3]§f", (float)this.getX(), (float)this.getY(), -1);
        }
        super.draw();
    }
    
    @Override
    public void renderDummy(final int mouseX, final int mouseY) {
        if (this.mc.isSingleplayer()) {
            this.fr.drawStringWithShadow("§3[§b §fSingleplayer §3]§f", (float)this.getX(), (float)this.getY(), -1);
        }
        else {
            this.fr.drawStringWithShadow("§3[§b" + this.mc.getCurrentServerData().serverIP + " §3]§f", (float)this.getX(), (float)this.getY(), -1);
        }
        super.renderDummy(mouseX, mouseY);
    }
}
