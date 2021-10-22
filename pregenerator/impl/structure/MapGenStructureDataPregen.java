// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.structure;

import net.minecraft.world.gen.structure.StructureBoundingBox;
import pregenerator.impl.misc.Tuple;
import java.util.Collection;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import java.util.LinkedHashSet;
import pregenerator.impl.misc.FilePos;
import java.util.Set;
import net.minecraft.world.gen.structure.StructureStart;
import java.util.HashMap;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenStructureData;

public class MapGenStructureDataPregen extends MapGenStructureData
{
    StructureController cont;
    MapGenStructure structure;
    HashMap<Long, StructureStart> storage;
    Set<FilePos> toDelete;
    
    public MapGenStructureDataPregen(final String name) {
        super(name);
        this.toDelete = new LinkedHashSet<FilePos>();
    }
    
    public MapGenStructureDataPregen(final MapGenStructureData structure) {
        super(structure.field_76190_i);
        this.toDelete = new LinkedHashSet<FilePos>();
        this.transfer(structure);
    }
    
    public void setOwner(final MapGenStructure base, final StructureController control) {
        this.structure = base;
        this.cont = control;
        try {
            this.storage = (HashMap<Long, StructureStart>)ReflectionHelper.getPrivateValue((Class)MapGenStructure.class, (Object)base, 1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void transfer(final MapGenStructureData data) {
        final NBTTagCompound oldCompound = data.func_143041_a();
        final NBTTagCompound newCompound = this.func_143041_a();
        for (final String key : oldCompound.func_150296_c()) {
            newCompound.func_74782_a(key, oldCompound.func_74781_a(key));
        }
    }
    
    public void func_143043_a(final NBTTagCompound tagCompoundIn, final int chunkX, final int chunkZ) {
        if (this.isInsideSaveZone(chunkX, chunkZ)) {
            this.deleteStructure(chunkX, chunkZ);
            return;
        }
        super.func_143043_a(tagCompoundIn, chunkX, chunkZ);
    }
    
    private boolean isInsideSaveZone(final int x, final int z) {
        return this.cont.isInsideBox(this.field_76190_i, x, z);
    }
    
    public void markForDeletion(final int x, final int z) {
        this.toDelete.add(new FilePos(x, z));
    }
    
    public void deleteStructure(final int chunkX, final int chunkZ) {
        if (this.storage != null) {
            this.storage.remove(FilePos.asLong(chunkX, chunkZ));
        }
        this.func_143041_a().func_82580_o(func_143042_b(chunkX, chunkZ));
    }
    
    public void onSaveZoneCheck() {
        for (final StructureStart entry : this.storage.values()) {
            if (this.isInsideSaveZone(entry.func_143019_e(), entry.func_143018_f())) {
                this.markForDeletion(entry.func_143019_e(), entry.func_143018_f());
            }
        }
        this.onDeletion();
    }
    
    public void onDeletion() {
        for (final FilePos pos : this.toDelete) {
            this.deleteStructure(pos.x, pos.z);
        }
        this.toDelete.clear();
    }
    
    public MapGenStructure getStructure() {
        return this.structure;
    }
    
    public Collection<StructureStart> getStarts() {
        return this.storage.values();
    }
    
    public Tuple<FilePos, FilePos> deleteStructure(final FilePos pos) {
        double d0 = Double.MAX_VALUE;
        StructureStart start = null;
        for (final StructureStart structurestart : this.storage.values()) {
            if (structurestart.func_75069_d()) {
                final double distance = pos.getSqDistance(structurestart.func_143019_e() * 16, structurestart.func_143018_f() * 16);
                if (d0 <= distance) {
                    continue;
                }
                d0 = distance;
                start = structurestart;
            }
        }
        if (start == null) {
            return null;
        }
        final StructureBoundingBox box = start.func_75071_a();
        final int minX = (box.field_78897_a >> 4) - 1;
        final int maxX = (box.field_78893_d >> 4) + 1;
        final int minZ = (box.field_78896_c >> 4) - 1;
        final int maxZ = (box.field_78892_f >> 4) + 1;
        this.cont.addSaveZone(this.field_76190_i, minX, minZ, (maxX - minX) * 2, (maxZ - minZ) * 2);
        return new Tuple<FilePos, FilePos>(new FilePos(minX, minZ), new FilePos(maxX, maxZ));
    }
}
