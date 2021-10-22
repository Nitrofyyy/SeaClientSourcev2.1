// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.tracking;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IWorldAccess;

public class WorldListener implements IWorldAccess
{
    WorldTracker tracker;
    
    public WorldListener(final WorldTracker world) {
        this.tracker = world;
    }
    
    public void func_174960_a(final BlockPos pos) {
        this.tracker.onBlockUpdate(pos);
    }
    
    public void func_174959_b(final BlockPos pos) {
    }
    
    public void func_147585_a(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
    }
    
    public void func_72704_a(final String soundName, final double x, final double y, final double z, final float volume, final float pitch) {
    }
    
    public void func_85102_a(final EntityPlayer except, final String soundName, final double x, final double y, final double z, final float volume, final float pitch) {
    }
    
    public void func_180442_a(final int particleID, final boolean ignoreRange, final double xCoord, final double yCoord, final double zCoord, final double xOffset, final double yOffset, final double zOffset, final int... p_180442_15_) {
    }
    
    public void func_72703_a(final Entity entityIn) {
    }
    
    public void func_72709_b(final Entity entityIn) {
    }
    
    public void func_174961_a(final String recordName, final BlockPos blockPosIn) {
    }
    
    public void func_180440_a(final int p_180440_1_, final BlockPos p_180440_2_, final int p_180440_3_) {
    }
    
    public void func_180439_a(final EntityPlayer player, final int sfxType, final BlockPos blockPosIn, final int p_180439_4_) {
    }
    
    public void func_180441_b(final int breakerId, final BlockPos pos, final int progress) {
    }
}
