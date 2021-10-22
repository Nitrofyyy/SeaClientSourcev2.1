// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.base;

import pregenerator.ChunkPregenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.common.DimensionManager;
import java.util.Hashtable;
import net.minecraft.world.World;
import java.util.Iterator;
import java.util.Collection;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pregenerator.base.impl.misc.RenderManager;
import pregenerator.base.api.misc.IRenderHelper;
import pregenerator.base.impl.networking.PacketHandler;
import pregenerator.base.api.network.INetworkManager;
import pregenerator.base.impl.misc.PregenConfig;
import pregenerator.base.api.misc.IConfig;
import java.io.File;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class PregenAPI
{
    public void init() {
    }
    
    public void postInit() {
    }
    
    public void registerTickEvent(final Object obj) {
        FMLCommonHandler.instance().bus().register(obj);
        MinecraftForge.EVENT_BUS.register(obj);
    }
    
    public IConfig getConfig(final File file) {
        return new PregenConfig(file);
    }
    
    public INetworkManager createNetworking() {
        return new PacketHandler().init();
    }
    
    @SideOnly(Side.CLIENT)
    public IRenderHelper createRenderHelper() {
        return new RenderManager().init();
    }
    
    public void sendChatMessage(final ICommandSender sender, final String message) {
        sender.func_145747_a((IChatComponent)new ChatComponentText(message));
    }
    
    public void sendChatMessage(final Collection<ICommandSender> senders, final String message) {
        final ChatComponentText text = new ChatComponentText(message);
        for (final ICommandSender sender : senders) {
            sender.func_145747_a((IChatComponent)text);
        }
    }
    
    public void setState(final World world, final boolean active) {
        try {
            final Hashtable<Integer, Boolean> table = (Hashtable<Integer, Boolean>)ReflectionHelper.getPrivateValue((Class)DimensionManager.class, (Object)null, new String[] { "spawnSettings" });
            table.put(world.field_73011_w.func_177502_q(), active);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean hasPermission(final EntityPlayer player, final String id) {
        return ChunkPregenerator.isOpped(player);
    }
}
