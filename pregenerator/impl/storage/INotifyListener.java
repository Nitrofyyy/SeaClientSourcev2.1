// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.storage;

import java.util.Objects;
import net.minecraft.entity.player.EntityPlayer;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;

public interface INotifyListener
{
    NBTTagCompound save();
    
    boolean isListening();
    
    boolean matches(final ICommandSender p0);
    
    ICommandSender getSender(final MinecraftServer p0);
    
    public static class PlayerListener implements INotifyListener
    {
        UUID player;
        boolean isListening;
        
        public PlayerListener(final NBTTagCompound nbt) {
            this.player = new UUID(nbt.func_74763_f("Most"), nbt.func_74763_f("Least"));
            this.isListening = nbt.func_74767_n("Listen");
        }
        
        public PlayerListener(final EntityPlayer player, final boolean listens) {
            this(player.func_110124_au(), listens);
        }
        
        public PlayerListener(final UUID uid, final boolean listens) {
            this.player = uid;
            this.isListening = listens;
        }
        
        @Override
        public NBTTagCompound save() {
            final NBTTagCompound nbt = new NBTTagCompound();
            nbt.func_74768_a("Type", 1);
            nbt.func_74757_a("Listen", this.isListening);
            nbt.func_74772_a("Most", this.player.getMostSignificantBits());
            nbt.func_74772_a("Least", this.player.getLeastSignificantBits());
            return nbt;
        }
        
        @Override
        public boolean matches(final ICommandSender sender) {
            return sender instanceof EntityPlayer && ((EntityPlayer)sender).func_110124_au().equals(this.player);
        }
        
        @Override
        public ICommandSender getSender(final MinecraftServer server) {
            return server.func_71203_ab().func_177451_a(this.player);
        }
        
        @Override
        public boolean isListening() {
            return this.isListening;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(this.player);
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof PlayerListener) {
                final PlayerListener listener = (PlayerListener)obj;
                return listener.player.equals(this.player);
            }
            return false;
        }
    }
    
    public static class ServerListener implements INotifyListener
    {
        boolean listens;
        
        public ServerListener(final NBTTagCompound nbt) {
            this.listens = nbt.func_74767_n("Listen");
        }
        
        public ServerListener(final boolean listen) {
            this.listens = listen;
        }
        
        @Override
        public NBTTagCompound save() {
            final NBTTagCompound nbt = new NBTTagCompound();
            nbt.func_74768_a("Type", 0);
            nbt.func_74757_a("Listen", this.listens);
            return nbt;
        }
        
        @Override
        public boolean matches(final ICommandSender sender) {
            return sender instanceof MinecraftServer;
        }
        
        @Override
        public ICommandSender getSender(final MinecraftServer server) {
            return server;
        }
        
        @Override
        public int hashCode() {
            return 0;
        }
        
        @Override
        public boolean equals(final Object obj) {
            return obj instanceof ServerListener;
        }
        
        @Override
        public boolean isListening() {
            return this.listens;
        }
    }
}
