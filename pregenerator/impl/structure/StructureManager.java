// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.structure;

import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;
import java.util.LinkedHashSet;
import net.minecraft.world.gen.structure.StructureStart;
import java.util.Collection;
import java.util.ArrayList;
import net.minecraftforge.common.DimensionManager;
import pregenerator.impl.misc.BoundingBox;
import java.util.HashSet;
import java.util.Iterator;
import net.minecraftforge.common.MinecraftForge;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import net.minecraft.world.World;
import java.util.Map;

public class StructureManager
{
    public static StructureManager instance;
    Map<World, StructureController> controller;
    Map<Integer, Set<String>> types;
    List<String> allTypes;
    
    public StructureManager() {
        this.controller = new LinkedHashMap<World, StructureController>();
        this.types = new HashMap<Integer, Set<String>>();
        this.allTypes = null;
    }
    
    public void init() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    public void onServerStopped() {
        for (final StructureController entry : this.controller.values()) {
            entry.cleanup();
        }
        this.controller.clear();
        this.allTypes = null;
    }
    
    public Set<String> getTypes(final int dimension) {
        Set<String> set = this.types.get(dimension);
        if (set == null) {
            set = new HashSet<String>();
        }
        return set;
    }
    
    public Map<String, List<BoundingBox>> getBoxes() {
        final Map<String, List<BoundingBox>> map = new LinkedHashMap<String, List<BoundingBox>>();
        for (final StructureController entry : this.controller.values()) {
            map.putAll(entry.getStorage().getBoxes());
        }
        return map;
    }
    
    public Map<String, List<BoundingBox>> getBoxes(final int dimension) {
        final StructureController control = this.controller.get(DimensionManager.getWorld(dimension));
        final Map<String, List<BoundingBox>> boxes = new LinkedHashMap<String, List<BoundingBox>>();
        if (control != null) {
            boxes.putAll(control.getStorage().getBoxes());
        }
        return boxes;
    }
    
    public List<BoundingBox> getBoxes(final int dimension, final String type) {
        final StructureController control = this.controller.get(DimensionManager.getWorld(dimension));
        if (control != null) {
            return control.getStorage().getBoxes(type);
        }
        return new ArrayList<BoundingBox>();
    }
    
    public List<String> generateAllTypes() {
        if (this.allTypes == null) {
            this.allTypes = new ArrayList<String>();
            for (final Set<String> set : this.types.values()) {
                this.allTypes.addAll(set);
            }
        }
        return this.allTypes;
    }
    
    public List<String> getAllTypes(final int world) {
        final Set<String> set = this.types.get(world);
        if (set == null || set.isEmpty()) {
            return new ArrayList<String>();
        }
        return new ArrayList<String>(set);
    }
    
    public List<String> getDims() {
        final List<String> list = new ArrayList<String>();
        for (final Integer data : this.types.keySet()) {
            list.add(data.toString());
        }
        return list;
    }
    
    public List<StructureData> getStructures(final int dim) {
        final List<StructureData> list = new ArrayList<StructureData>();
        final StructureController cont = this.controller.get(DimensionManager.getWorld(dim));
        if (cont != null) {
            cont.addStructures(list);
        }
        return list;
    }
    
    public List<String> getXPositions(final int dim, final String type) {
        final StructureController control = this.controller.get(DimensionManager.getWorld(dim));
        if (control != null) {
            final MapGenStructureDataPregen data = control.getStructure(type);
            if (data != null) {
                final List<String> s = new ArrayList<String>();
                for (final StructureStart start : data.getStarts()) {
                    s.add(start.func_143019_e() + "");
                }
                return s;
            }
        }
        return new ArrayList<String>();
    }
    
    public List<String> getZPositions(final int dim, final String type, final int x) {
        final StructureController control = this.controller.get(DimensionManager.getWorld(dim));
        if (control != null) {
            final MapGenStructureDataPregen data = control.getStructure(type);
            if (data != null) {
                final List<String> s = new ArrayList<String>();
                for (final StructureStart start : data.getStarts()) {
                    if (start.func_143019_e() == x) {
                        s.add(start.func_143018_f() + "");
                    }
                }
                return s;
            }
        }
        return new ArrayList<String>();
    }
    
    public boolean validateType(final int dim, final String type) {
        if (type.equalsIgnoreCase("All")) {
            return true;
        }
        final Set<String> set = this.types.get(dim);
        return set != null && set.contains(type);
    }
    
    public void register(final World world, final String type) {
        Set<String> set = this.types.get(world.field_73011_w.func_177502_q());
        if (set == null) {
            set = new LinkedHashSet<String>();
            this.types.put(world.field_73011_w.func_177502_q(), set);
        }
        set.add(type);
    }
    
    @SubscribeEvent
    public void onWorldLoad(final WorldEvent.Load event) {
        final World world = event.world;
        if (world.field_72995_K) {
            return;
        }
        this.controller.put(world, new StructureController(world));
        this.allTypes = null;
    }
    
    @SubscribeEvent
    public void onWorldUnload(final WorldEvent.Unload event) {
        final StructureController control = this.controller.remove(event.world);
        if (control != null) {
            control.cleanup();
            this.allTypes = null;
        }
    }
    
    @SubscribeEvent
    public void onPopulation(final PopulateChunkEvent.Pre event) {
        final StructureController control = this.controller.get(event.world);
        if (control != null) {
            control.onPopulation();
        }
    }
    
    public void createSaveZone(final int x, final int z, final int radius, final String type, final int dimension) {
        final StructureController control = this.controller.get(DimensionManager.getWorld(dimension));
        if (control != null) {
            control.addSaveZone(type, x, z, radius, radius);
        }
    }
    
    public boolean clearZoneAt(final int index, final int dim, final String type) {
        final StructureController control = this.controller.get(DimensionManager.getWorld(dim));
        return control != null && control.getStorage().removeAt(index, type);
    }
    
    public boolean clearZoneLast(final int dim, final String type) {
        final StructureController control = this.controller.get(DimensionManager.getWorld(dim));
        return control != null && control.getStorage().removeLast(type);
    }
    
    public void clearAllZones(final int dim, final String type) {
        final StructureController control = this.controller.get(DimensionManager.getWorld(dim));
        if (control != null) {
            control.getStorage().clearAll(type);
        }
    }
    
    public void clearAllZones() {
        for (final StructureController control : this.controller.values()) {
            control.getStorage().clearAll();
        }
    }
    
    public MapGenStructureDataPregen getStructure(final int dim, final String type) {
        final StructureController control = this.controller.get(DimensionManager.getWorld(dim));
        if (control != null) {
            return control.getStructure(type);
        }
        return null;
    }
    
    public List<MapGenStructureDataPregen> getStructureHolders(final int dim) {
        final StructureController control = this.controller.get(DimensionManager.getWorld(dim));
        if (control != null) {
            return control.getStructures();
        }
        return new ArrayList<MapGenStructureDataPregen>();
    }
    
    static {
        StructureManager.instance = new StructureManager();
    }
}
