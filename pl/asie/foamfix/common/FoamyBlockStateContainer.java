// 
// Decompiled by Procyon v0.5.36
// 

package pl.asie.foamfix.common;

import com.google.common.base.Optional;
import net.minecraftforge.common.property.IUnlistedProperty;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;

public class FoamyBlockStateContainer extends BlockState
{
    public FoamyBlockStateContainer(final Block blockIn, final IProperty<?>... properties) {
        super(blockIn, (IProperty[])properties);
    }
    
    protected StateImplementation createState(final Block block, final ImmutableMap<IProperty, Comparable> properties, final ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties) {
        return new FoamyBlockState(PropertyValueMapper.getOrCreate(this), block, properties);
    }
}
