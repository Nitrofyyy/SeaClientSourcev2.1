// 
// Decompiled by Procyon v0.5.36
// 

package pl.asie.foamfix.common;

import net.minecraft.block.state.BlockState;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.Block;
import net.minecraftforge.common.property.ExtendedBlockState;

public class FoamyExtendedBlockStateContainer extends ExtendedBlockState
{
    public FoamyExtendedBlockStateContainer(final Block blockIn, final IProperty<?>[] properties, final IUnlistedProperty<?>[] unlistedProperties) {
        super(blockIn, (IProperty[])properties, (IUnlistedProperty[])unlistedProperties);
    }
    
    protected BlockState.StateImplementation createState(final Block block, final ImmutableMap<IProperty, Comparable> properties, final ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties) {
        if (unlistedProperties == null || unlistedProperties.isEmpty()) {
            return new FoamyBlockState(PropertyValueMapper.getOrCreate((BlockState)this), block, properties);
        }
        return new FoamyExtendedBlockState(PropertyValueMapper.getOrCreate((BlockState)this), block, properties, unlistedProperties);
    }
}
