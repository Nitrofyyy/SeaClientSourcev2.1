// 
// Decompiled by Procyon v0.5.36
// 

package sea.utils;

import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.world.WorldSettings;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.NetworkPlayerInfo;

public class FakeNetworkPlayerInfo extends NetworkPlayerInfo
{
    public FakeNetworkPlayerInfo(final GameProfile gp) {
        super(gp);
    }
    
    @Override
    public IChatComponent getDisplayName() {
        return new ChatComponentText(this.getGameProfile().getName());
    }
    
    @Override
    public WorldSettings.GameType getGameType() {
        return WorldSettings.GameType.CREATIVE;
    }
    
    @Override
    public int getResponseTime() {
        return 0;
    }
    
    @Override
    public String getSkinType() {
        return "default";
    }
    
    @Override
    public ScorePlayerTeam getPlayerTeam() {
        return null;
    }
}
