// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.impl;

import java.util.Arrays;
import net.minecraft.client.Minecraft;
import sea.mods.huds.HudList;
import net.minecraft.util.IChatComponent;
import sea.mods.Module;

public class AutoGG extends Module
{
    public static final AutoGG INSTANCE;
    private long lastTrigger;
    
    static {
        INSTANCE = new AutoGG();
    }
    
    public AutoGG() {
        super("AutoGG", "");
    }
    
    public void onChat(final IChatComponent message) {
        if (HudList.getGg().isEnabled() && Minecraft.getMinecraft().getCurrentServerData() != null && Minecraft.getMinecraft().getCurrentServerData().serverIP != null && System.currentTimeMillis() > this.lastTrigger + 1000L && Arrays.asList(getHypixelTrigger().split("\n")).stream().anyMatch(trigger -> message.getUnformattedText().contains(trigger))) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("gg");
            Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages("gg");
            this.lastTrigger = System.currentTimeMillis();
        }
    }
    
    public static String getHypixelTrigger() {
        return "? TheBridge ? Match Recap - Games Played: \n Total Points: \n Kills: \n Deaths: \n Victories: \n";
    }
}
