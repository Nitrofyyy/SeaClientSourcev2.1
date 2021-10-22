// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.structure;

import java.util.Iterator;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import java.util.ArrayList;
import net.minecraft.nbt.NBTTagCompound;
import java.util.LinkedHashMap;
import pregenerator.impl.misc.BoundingBox;
import java.util.List;
import java.util.Map;
import net.minecraft.world.WorldSavedData;

public class SaveZoneData extends WorldSavedData
{
    Map<String, List<BoundingBox>> saveZones;
    
    public SaveZoneData(final String name) {
        super(name);
        this.saveZones = new LinkedHashMap<String, List<BoundingBox>>();
    }
    
    public void func_76184_a(final NBTTagCompound nbt) {
        this.saveZones.clear();
        final NBTTagList list = nbt.func_150295_c("Data", 10);
        for (int i = 0; i < list.func_74745_c(); ++i) {
            final NBTTagCompound data = list.func_150305_b(i);
            final NBTTagList dataList = data.func_150295_c("Value", 11);
            final List<BoundingBox> boxes = new ArrayList<BoundingBox>();
            for (int x = 0; x < dataList.func_74745_c(); ++x) {
                boxes.add(new BoundingBox(dataList.func_150306_c(x)));
            }
            this.saveZones.put(data.func_74779_i("ID"), boxes);
        }
    }
    
    public void func_76187_b(final NBTTagCompound nbt) {
        final NBTTagList list = new NBTTagList();
        for (final Map.Entry<String, List<BoundingBox>> entry : this.saveZones.entrySet()) {
            final NBTTagCompound data = new NBTTagCompound();
            data.func_74778_a("ID", (String)entry.getKey());
            final NBTTagList dataList = new NBTTagList();
            for (final BoundingBox box : entry.getValue()) {
                dataList.func_74742_a((NBTBase)box.write());
            }
            data.func_74782_a("Value", (NBTBase)dataList);
            list.func_74742_a((NBTBase)data);
        }
        nbt.func_74782_a("Data", (NBTBase)list);
    }
    
    public void addSaveZone(final String type, final BoundingBox box) {
        List<BoundingBox> boxes = this.saveZones.get(type);
        if (boxes == null) {
            boxes = new ArrayList<BoundingBox>();
            this.saveZones.put(type, boxes);
        }
        boxes.add(box);
        this.func_76185_a();
    }
    
    public boolean contains(final String type, final int x, final int z) {
        final List<BoundingBox> boxes = this.saveZones.get(type);
        if (boxes == null) {
            return false;
        }
        for (final BoundingBox box : boxes) {
            if (box.isInsideBox(x, z)) {
                return true;
            }
        }
        return false;
    }
    
    public Map<String, List<BoundingBox>> getBoxes() {
        return this.saveZones;
    }
    
    public List<BoundingBox> getBoxes(final String type) {
        List<BoundingBox> box = this.saveZones.get(type);
        if (box == null) {
            box = new ArrayList<BoundingBox>();
        }
        return box;
    }
    
    public boolean removeAt(final int index, final String type) {
        final List<BoundingBox> box = this.saveZones.get(type);
        if (box == null || index < 0 || index >= box.size()) {
            return false;
        }
        box.remove(index);
        this.func_76185_a();
        return true;
    }
    
    public boolean removeLast(final String type) {
        final List<BoundingBox> box = this.saveZones.get(type);
        if (box == null || box.isEmpty()) {
            return false;
        }
        box.remove(box.size() - 1);
        this.func_76185_a();
        return true;
    }
    
    public void clearAll() {
        this.saveZones.clear();
        this.func_76185_a();
    }
    
    public void clearAll(final String type) {
        if (type.equalsIgnoreCase("All")) {
            this.clearAll();
        }
        else if (this.saveZones.remove(type) != null) {
            this.func_76185_a();
        }
    }
}
