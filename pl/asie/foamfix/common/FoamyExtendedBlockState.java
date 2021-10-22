// 
// Decompiled by Procyon v0.5.36
// 

package pl.asie.foamfix.common;

import java.util.Collections;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import com.google.common.collect.Iterables;
import com.google.common.base.Predicates;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.Block;
import com.google.common.base.Optional;
import net.minecraftforge.common.property.IUnlistedProperty;
import com.google.common.collect.ImmutableMap;
import net.minecraftforge.common.property.IExtendedBlockState;

public class FoamyExtendedBlockState extends FoamyBlockState implements IExtendedBlockState
{
    private final ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties;
    
    public FoamyExtendedBlockState(final PropertyValueMapper owner, final Block block, final ImmutableMap<IProperty, Comparable> properties, final ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties) {
        super(owner, block, properties);
        this.unlistedProperties = unlistedProperties;
    }
    
    public FoamyExtendedBlockState(final PropertyValueMapper owner, final Block block, final ImmutableMap<IProperty, Comparable> properties, final ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties, final int value) {
        super(owner, block, properties);
        this.unlistedProperties = unlistedProperties;
        this.value = value;
    }
    
    @Override
    public <T extends Comparable<T>, V extends T> IBlockState func_177226_a(final IProperty<T> property, final V propertyValue) {
        if (!this.func_177228_b().containsKey((Object)property)) {
            throw new IllegalArgumentException("Cannot set property " + property + " as it does not exist in " + this.func_177230_c().func_176194_O());
        }
        if (!property.func_177700_c().contains(propertyValue)) {
            throw new IllegalArgumentException("Cannot set property " + property + " to " + this.value + " on block " + Block.field_149771_c.func_177774_c((Object)this.func_177230_c()) + ", it is not an allowed value");
        }
        if (this.func_177228_b().get((Object)property) == propertyValue) {
            return this;
        }
        final int newValue = this.owner.withPropertyValue(this.value, property, propertyValue);
        if (newValue == -1) {
            throw new IllegalArgumentException("Cannot set property " + property + " because FoamFix could not find a mapping for it! Please reproduce without FoamFix first!");
        }
        final IBlockState state = this.owner.getPropertyByValue(newValue);
        if (Iterables.all((Iterable)this.unlistedProperties.values(), Predicates.equalTo((Object)Optional.absent()))) {
            return state;
        }
        return new FoamyExtendedBlockState(this.owner, this.func_177230_c(), (ImmutableMap<IProperty, Comparable>)state.func_177228_b(), this.unlistedProperties, newValue);
    }
    
    public <V> IExtendedBlockState withProperty(final IUnlistedProperty<V> property, final V value) {
        if (!this.unlistedProperties.containsKey((Object)property)) {
            throw new IllegalArgumentException("Cannot set unlisted property " + property + " as it does not exist in " + this.func_177230_c().func_176194_O());
        }
        if (!property.isValid(value)) {
            throw new IllegalArgumentException("Cannot set unlisted property " + property + " to " + value + " on block " + Block.field_149771_c.func_177774_c((Object)this.func_177230_c()) + ", it is not an allowed value");
        }
        final Map<IUnlistedProperty<?>, Optional<?>> newMap = new HashMap<IUnlistedProperty<?>, Optional<?>>((Map<? extends IUnlistedProperty<?>, ? extends Optional<?>>)this.unlistedProperties);
        newMap.put(property, (Optional<?>)Optional.fromNullable((Object)value));
        if (Iterables.all((Iterable)newMap.values(), Predicates.equalTo((Object)Optional.absent()))) {
            return (IExtendedBlockState)this.owner.getPropertyByValue(this.value);
        }
        return (IExtendedBlockState)new FoamyExtendedBlockState(this.owner, this.func_177230_c(), (ImmutableMap<IProperty, Comparable>)this.func_177228_b(), (ImmutableMap<IUnlistedProperty<?>, Optional<?>>)ImmutableMap.copyOf((Map)newMap), this.value);
    }
    
    public Collection<IUnlistedProperty<?>> getUnlistedNames() {
        return Collections.unmodifiableCollection((Collection<? extends IUnlistedProperty<?>>)this.unlistedProperties.keySet());
    }
    
    public <V> V getValue(final IUnlistedProperty<V> property) {
        if (!this.unlistedProperties.containsKey((Object)property)) {
            throw new IllegalArgumentException("Cannot get unlisted property " + property + " as it does not exist in " + this.func_177230_c().func_176194_O());
        }
        return property.getType().cast(((Optional)this.unlistedProperties.get((Object)property)).orNull());
    }
    
    public ImmutableMap<IUnlistedProperty<?>, Optional<?>> getUnlistedProperties() {
        return this.unlistedProperties;
    }
    
    public IBlockState getClean() {
        return this.owner.getPropertyByValue(this.value);
    }
}
