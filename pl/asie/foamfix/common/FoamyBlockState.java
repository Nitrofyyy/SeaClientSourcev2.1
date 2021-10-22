// 
// Decompiled by Procyon v0.5.36
// 

package pl.asie.foamfix.common;

import java.util.Map;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.BlockState;

public class FoamyBlockState extends BlockState.StateImplementation
{
    protected final PropertyValueMapper owner;
    protected final ImmutableMap<IProperty, Comparable> field_177237_b;
    protected int value;
    
    public FoamyBlockState(final PropertyValueMapper owner, final Block blockIn, final ImmutableMap<IProperty, Comparable> propertiesIn) {
        super(blockIn, propertiesIn);
        this.owner = owner;
        this.field_177237_b = propertiesIn;
    }
    
    public <T extends Comparable<T>, V extends T> IBlockState func_177226_a(final IProperty<T> property, final V value) {
        final Comparable<?> comparable = (Comparable<?>)this.field_177237_b.get((Object)property);
        if (comparable == null) {
            throw new IllegalArgumentException("Cannot set property " + property + " as it does not exist in " + this.func_177230_c().func_176194_O());
        }
        if (comparable == value) {
            return this;
        }
        final IBlockState state = this.owner.withProperty(this.value, property, value);
        if (state == null) {
            throw new IllegalArgumentException("Cannot set property " + property + " to " + value + " on block " + Block.field_149771_c.func_177774_c((Object)this.func_177230_c()) + ", it is not an allowed value");
        }
        return state;
    }
    
    public void func_177235_a(final Map<Map<IProperty, Comparable>, BlockState.StateImplementation> map) {
        this.value = this.owner.generateValue(this);
    }
}
