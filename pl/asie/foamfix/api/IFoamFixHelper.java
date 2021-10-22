// 
// Decompiled by Procyon v0.5.36
// 

package pl.asie.foamfix.api;

import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.Block;

public interface IFoamFixHelper
{
    BlockState createBlockState(final Block p0, final IProperty<?>... p1);
    
    BlockState createExtendedBlockState(final Block p0, final IProperty<?>[] p1, final IUnlistedProperty<?>[] p2);
    
    public static class Default implements IFoamFixHelper
    {
        @Override
        public BlockState createBlockState(final Block block, final IProperty<?>... properties) {
            return new BlockState(block, (IProperty[])properties);
        }
        
        @Override
        public BlockState createExtendedBlockState(final Block block, final IProperty<?>[] properties, final IUnlistedProperty<?>[] unlistedProperties) {
            return (BlockState)new ExtendedBlockState(block, (IProperty[])properties, (IUnlistedProperty[])unlistedProperties);
        }
    }
}
