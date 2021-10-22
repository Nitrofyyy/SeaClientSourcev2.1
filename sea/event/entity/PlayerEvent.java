// 
// Decompiled by Procyon v0.5.36
// 

package sea.event.entity;

import java.io.File;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.block.state.IBlockState;
import sea.event.Cancelable;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerEvent extends LivingEvent
{
    public final EntityPlayer entityPlayer;
    
    public PlayerEvent(final EntityPlayer player) {
        super(player);
        this.entityPlayer = player;
    }
    
    public static class HarvestCheck extends PlayerEvent
    {
        public final Block block;
        public boolean success;
        
        public HarvestCheck(final EntityPlayer player, final Block block, final boolean success) {
            super(player);
            this.block = block;
            this.success = success;
        }
    }
    
    @Cancelable
    public static class BreakSpeed extends PlayerEvent
    {
        public final IBlockState state;
        public final float originalSpeed;
        public float newSpeed;
        public final BlockPos pos;
        
        public BreakSpeed(final EntityPlayer player, final IBlockState state, final float original, final BlockPos pos) {
            super(player);
            this.newSpeed = 0.0f;
            this.state = state;
            this.originalSpeed = original;
            this.newSpeed = original;
            this.pos = pos;
        }
    }
    
    public static class NameFormat extends PlayerEvent
    {
        public final String username;
        public String displayname;
        
        public NameFormat(final EntityPlayer player, final String username) {
            super(player);
            this.username = username;
            this.displayname = username;
        }
    }
    
    public static class Clone extends PlayerEvent
    {
        public final EntityPlayer original;
        public final boolean wasDeath;
        
        public Clone(final EntityPlayer _new, final EntityPlayer oldPlayer, final boolean wasDeath) {
            super(_new);
            this.original = oldPlayer;
            this.wasDeath = wasDeath;
        }
    }
    
    public static class StartTracking extends PlayerEvent
    {
        public final Entity target;
        
        public StartTracking(final EntityPlayer player, final Entity target) {
            super(player);
            this.target = target;
        }
    }
    
    public static class StopTracking extends PlayerEvent
    {
        public final Entity target;
        
        public StopTracking(final EntityPlayer player, final Entity target) {
            super(player);
            this.target = target;
        }
    }
    
    public static class LoadFromFile extends PlayerEvent
    {
        public final File playerDirectory;
        public final String playerUUID;
        
        public LoadFromFile(final EntityPlayer player, final File originDirectory, final String playerUUID) {
            super(player);
            this.playerDirectory = originDirectory;
            this.playerUUID = playerUUID;
        }
        
        public File getPlayerFile(final String suffix) {
            if ("dat".equals(suffix)) {
                throw new IllegalArgumentException("The suffix 'dat' is reserved");
            }
            return new File(this.playerDirectory, String.valueOf(this.playerUUID) + "." + suffix);
        }
    }
    
    public static class SaveToFile extends PlayerEvent
    {
        public final File playerDirectory;
        public final String playerUUID;
        
        public SaveToFile(final EntityPlayer player, final File originDirectory, final String playerUUID) {
            super(player);
            this.playerDirectory = originDirectory;
            this.playerUUID = playerUUID;
        }
        
        public File getPlayerFile(final String suffix) {
            if ("dat".equals(suffix)) {
                throw new IllegalArgumentException("The suffix 'dat' is reserved");
            }
            return new File(this.playerDirectory, String.valueOf(this.playerUUID) + "." + suffix);
        }
    }
}
