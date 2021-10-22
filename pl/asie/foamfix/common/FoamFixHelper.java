// 
// Decompiled by Procyon v0.5.36
// 

package pl.asie.foamfix.common;

import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.Block;
import pl.asie.foamfix.api.IFoamFixHelper;

public class FoamFixHelper implements IFoamFixHelper
{
    @Override
    public BlockState createBlockState(final Block block, final IProperty... properties) {
        return new FoamyBlockStateContainer(block, (IProperty<?>[])properties);
    }
    
    @Override
    public BlockState createExtendedBlockState(final Block block, final IProperty[] properties, final IUnlistedProperty[] unlistedProperties) {
        return (BlockState)new FoamyExtendedBlockStateContainer(block, properties, unlistedProperties);
    }
}
