// 
// Decompiled by Procyon v0.5.36
// 

package sea.utils;

import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.stats.StatFileWriter;
import java.util.UUID;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.NetworkManager;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.EnumPacketDirection;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.World;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class FakePlayer extends EntityPlayerSP
{
    public FakePlayer(final Minecraft mc, final World world) {
        this(mc, world, checkNullGameProfile());
    }
    
    public FakePlayer(final Minecraft mc, final World world, final GameProfile gp) {
        super(mc, world, new NetHandlerPlayClient(mc, mc.currentScreen, new FakeNetworkManager(EnumPacketDirection.CLIENTBOUND), gp) {
            @Override
            public NetworkPlayerInfo getPlayerInfo(final String p_175104_1_) {
                return new FakeNetworkPlayerInfo(gp);
            }
            
            @Override
            public NetworkPlayerInfo getPlayerInfo(final UUID p_175102_1_) {
                return new FakeNetworkPlayerInfo(gp);
            }
        }, null);
        this.dimension = 0;
        this.posX = 0.0;
        this.posY = 0.0;
        this.posZ = 0.0;
    }
    
    @Override
    public float getEyeHeight() {
        return 1.82f;
    }
    
    @Override
    public boolean isWearing(final EnumPlayerModelParts p_175148_1_) {
        return true;
    }
    
    @Override
    public boolean hasPlayerInfo() {
        return true;
    }
    
    @Override
    protected NetworkPlayerInfo getPlayerInfo() {
        return new FakeNetworkPlayerInfo(this.getGameProfile());
    }
    
    private static GameProfile checkNullGameProfile() {
        return (Minecraft.getMinecraft().getSession() != null && Minecraft.getMinecraft().getSession().getProfile() != null) ? Minecraft.getMinecraft().getSession().getProfile() : new GameProfile(UUID.randomUUID(), "FakePlayer");
    }
}
