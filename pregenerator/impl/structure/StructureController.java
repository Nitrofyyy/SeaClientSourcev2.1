// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.structure;

import java.util.Collection;
import net.minecraft.world.gen.structure.StructureStart;
import java.lang.reflect.Field;
import java.util.ArrayList;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraft.world.gen.structure.MapGenStructureData;
import java.util.List;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.WorldServer;
import java.util.Iterator;
import pregenerator.impl.misc.BoundingBox;
import net.minecraft.world.WorldSavedData;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.world.World;

public class StructureController
{
    World world;
    Map<String, MapGenStructureDataPregen> pregen;
    SaveZoneData data;
    
    public StructureController(final World world) {
        this.pregen = new LinkedHashMap<String, MapGenStructureDataPregen>();
        this.world = world;
        this.data = (SaveZoneData)world.getPerWorldStorage().func_75742_a((Class)SaveZoneData.class, "SaveZoneData");
        if (this.data == null) {
            this.data = new SaveZoneData("SaveZoneData");
            world.getPerWorldStorage().func_75745_a("SaveZoneData", (WorldSavedData)this.data);
        }
        this.init();
    }
    
    public void addSaveZone(final String type, final int x, final int z, final int width, final int height) {
        if (type.equalsIgnoreCase("All")) {
            final BoundingBox box = new BoundingBox(x, z, width, height);
            for (final String realType : StructureManager.instance.getTypes(this.world.field_73011_w.func_177502_q())) {
                this.data.addSaveZone(realType, box);
            }
        }
        else {
            this.data.addSaveZone(type, new BoundingBox(x, z, width, height));
        }
        this.onSaveZoneChange();
    }
    
    private void onSaveZoneChange() {
        for (final MapGenStructureDataPregen entry : this.pregen.values()) {
            entry.onSaveZoneCheck();
        }
    }
    
    public void onPopulation() {
        for (final MapGenStructureDataPregen entry : this.pregen.values()) {
            entry.onDeletion();
        }
    }
    
    public SaveZoneData getStorage() {
        return this.data;
    }
    
    public void cleanup() {
        this.pregen.clear();
        this.world = null;
    }
    
    private void init() {
        if (this.world instanceof WorldServer) {
            final WorldServer serverWorld = (WorldServer)this.world;
            final List<MapGenStructure> list = this.getStructures(((ChunkProviderServer)serverWorld.func_72863_F()).field_73246_d);
            for (final MapGenStructure entry : list) {
                if (entry == null) {
                    continue;
                }
                this.applyChanges(entry);
            }
        }
        else {
            FMLLog.getLogger().info("A Client world tried to be read");
        }
    }
    
    private void applyChanges(final MapGenStructure structure) {
        StructureManager.instance.register(this.world, structure.func_143025_a());
        final MapGenStructureData data = (MapGenStructureData)this.world.getPerWorldStorage().func_75742_a((Class)MapGenStructureDataPregen.class, structure.func_143025_a());
        if (data instanceof MapGenStructureDataPregen) {
            ((MapGenStructureDataPregen)data).setOwner(structure, this);
            this.pregen.put(structure.func_143025_a(), (MapGenStructureDataPregen)data);
        }
        else if (data == null) {
            final MapGenStructureDataPregen helper = new MapGenStructureDataPregen(structure.func_143025_a());
            helper.setOwner(structure, this);
            this.world.getPerWorldStorage().func_75745_a(structure.func_143025_a(), (WorldSavedData)helper);
            this.pregen.put(structure.func_143025_a(), helper);
        }
        else {
            final MapGenStructureDataPregen helper = new MapGenStructureDataPregen(data);
            helper.setOwner(structure, this);
            this.world.getPerWorldStorage().func_75745_a(structure.func_143025_a(), (WorldSavedData)helper);
            ReflectionHelper.setPrivateValue((Class)MapGenStructure.class, (Object)structure, (Object)helper, new String[] { "structureData", "field_143029_e" });
            this.pregen.put(structure.func_143025_a(), helper);
        }
    }
    
    private List<MapGenStructure> getStructures(final IChunkProvider generator) {
        final List<MapGenStructure> list = new ArrayList<MapGenStructure>();
        final Class<?> structure = MapGenStructure.class;
        try {
            final Class<?> clz = generator.getClass();
            final Field[] fields = clz.getDeclaredFields();
            for (int i = 0; i < fields.length; ++i) {
                final Field field = fields[i];
                if (structure.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    list.add((MapGenStructure)field.get(generator));
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public void addStructures(final List<StructureData> data) {
        for (final Map.Entry<String, MapGenStructureDataPregen> entry : this.pregen.entrySet()) {
            for (final StructureStart start : entry.getValue().getStarts()) {
                data.add(new StructureData(entry.getKey(), start));
            }
        }
    }
    
    public MapGenStructureDataPregen getStructure(final String type) {
        return this.pregen.get(type);
    }
    
    public List<MapGenStructureDataPregen> getStructures() {
        return new ArrayList<MapGenStructureDataPregen>(this.pregen.values());
    }
    
    public boolean isInsideBox(final String type, final int x, final int z) {
        return this.data.contains(type, x, z);
    }
}
