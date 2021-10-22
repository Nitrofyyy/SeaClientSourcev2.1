// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.storage;

import java.nio.ByteBuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.world.World;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Iterator;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import java.util.HashMap;
import net.minecraft.nbt.NBTTagCompound;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.world.WorldSavedData;

public class TaskStorage extends WorldSavedData
{
    Set<PregenTask> commandsToRun;
    Set<INotifyListener> autoListeners;
    Map<PregenTask, PregenMemory> memory;
    boolean created;
    Set<Integer> alreadyRan;
    
    public TaskStorage(final String name) {
        super(name);
        this.commandsToRun = new LinkedHashSet<PregenTask>();
        this.autoListeners = new LinkedHashSet<INotifyListener>();
        this.memory = new LinkedHashMap<PregenTask, PregenMemory>();
        this.created = false;
        this.alreadyRan = new HashSet<Integer>();
    }
    
    public void func_76184_a(final NBTTagCompound nbt) {
        this.alreadyRan.clear();
        this.created = nbt.func_74767_n("Created");
        this.commandsToRun.clear();
        this.autoListeners.clear();
        final Map<Integer, PregenTask> tasks = new HashMap<Integer, PregenTask>();
        NBTTagList list = nbt.func_150295_c("SavedData", 11);
        for (int i = 0; i < list.func_74745_c(); ++i) {
            final int[] array = list.func_150306_c(i);
            if (array.length == 10) {
                final PregenTask task = new MassCircleTask(array);
                this.commandsToRun.add(task);
                tasks.put(task.hashCode(), task);
            }
            else if (array.length == 7) {
                final PregenTask task = new PregenTask(array);
                this.commandsToRun.add(task);
                tasks.put(task.hashCode(), task);
            }
        }
        list = nbt.func_150295_c("memory", 10);
        for (int i = 0; i < list.func_74745_c(); ++i) {
            final NBTTagCompound data = list.func_150305_b(i);
            final PregenTask task = tasks.get(data.func_74762_e("key"));
            if (task != null) {
                this.memory.put(task, new PregenMemory(data));
            }
        }
        list = nbt.func_150295_c("AutoListeners", 10);
        for (int i = 0; i < list.func_74745_c(); ++i) {
            final INotifyListener listener = fromNBT(list.func_150305_b(i));
            if (listener != null) {
                this.autoListeners.add(listener);
            }
        }
        for (final int data2 : nbt.func_74759_k("DimCreated")) {
            this.alreadyRan.add(data2);
        }
    }
    
    public void func_76187_b(final NBTTagCompound compound) {
        compound.func_74757_a("Created", this.created);
        NBTTagList list = new NBTTagList();
        for (final PregenTask command : this.commandsToRun) {
            list.func_74742_a((NBTBase)command.save());
        }
        compound.func_74782_a("SavedData", (NBTBase)list);
        list = new NBTTagList();
        for (final Map.Entry<PregenTask, PregenMemory> entry : this.memory.entrySet()) {
            final NBTTagCompound tag = entry.getValue().save();
            tag.func_74768_a("key", entry.getKey().hashCode());
            list.func_74742_a((NBTBase)tag);
        }
        compound.func_74782_a("memory", (NBTBase)list);
        list = new NBTTagList();
        for (final INotifyListener listener : this.autoListeners) {
            list.func_74742_a((NBTBase)listener.save());
        }
        compound.func_74782_a("AutoListeners", (NBTBase)list);
        final int[] dims = new int[this.alreadyRan.size()];
        int index = 0;
        for (final Integer data : this.alreadyRan) {
            dims[index] = data;
            ++index;
        }
        compound.func_74783_a("DimCreated", dims);
    }
    
    public void setCreated() {
        this.created = true;
        this.func_76185_a();
    }
    
    public boolean isCreated() {
        return this.created;
    }
    
    public boolean hasNotRanAlready(final int dimID) {
        return this.alreadyRan.add(dimID);
    }
    
    public void savePregenTask(final PregenTask command) {
        this.commandsToRun.add(command);
        this.func_76185_a();
    }
    
    public void savePregenTasks(final List<PregenTask> list) {
        this.commandsToRun.addAll(list);
        this.func_76185_a();
    }
    
    public List<PregenTask> getTasks() {
        return new ArrayList<PregenTask>(this.commandsToRun);
    }
    
    public void finishTask(final PregenTask command) {
        if (this.commandsToRun.remove(command)) {
            this.memory.remove(command);
            this.func_76185_a();
        }
    }
    
    public void clearAll() {
        this.commandsToRun.clear();
        this.memory.clear();
        this.func_76185_a();
    }
    
    public PregenTask clearOne() {
        final Iterator<PregenTask> com = this.commandsToRun.iterator();
        if (com.hasNext()) {
            final PregenTask result = com.next();
            this.memory.remove(result);
            com.remove();
            this.func_76185_a();
            return result;
        }
        return null;
    }
    
    public int clearLast() {
        return this.clearIndex(this.commandsToRun.size());
    }
    
    public int clearIndex(final int index) {
        if (index == 0) {
            return (this.clearOne() != null) ? 2 : 0;
        }
        final Iterator<PregenTask> com = this.commandsToRun.iterator();
        PregenTask lastTask = null;
        int done;
        for (done = 0; com.hasNext() && done < index; ++done) {
            lastTask = com.next();
        }
        if (done == index) {
            this.memory.remove(lastTask);
            com.remove();
            this.func_76185_a();
            return 1;
        }
        return 0;
    }
    
    public boolean hasTasks() {
        return this.commandsToRun.size() > 0;
    }
    
    public int getTaskCount() {
        return this.commandsToRun.size();
    }
    
    public PregenTask getNextTask() {
        return this.commandsToRun.iterator().next();
    }
    
    public PregenMemory getOrCreateMemory(final PregenTask task) {
        PregenMemory result = this.memory.get(task);
        if (result == null) {
            result = new PregenMemory();
            this.memory.put(task, result);
        }
        return result;
    }
    
    public static TaskStorage getStorage() {
        return getFromServer(FMLCommonHandler.instance().getMinecraftServerInstance());
    }
    
    public static TaskStorage getFromServer(final MinecraftServer server) {
        return getFromWorld(server.func_130014_f_());
    }
    
    public static TaskStorage getFromWorld(final World world) {
        TaskStorage storage = (TaskStorage)world.func_72943_a((Class)TaskStorage.class, "PregenTaskStorage");
        if (storage == null) {
            storage = new TaskStorage("PregenTaskStorage");
            world.func_72823_a("PregenTaskStorage", (WorldSavedData)storage);
        }
        return storage;
    }
    
    @Override
    public String toString() {
        return this.commandsToRun.toString();
    }
    
    public void addListenState(final ICommandSender sender, final boolean state) {
        final INotifyListener notitfy = fromSender(sender, state);
        this.autoListeners.remove(notitfy);
        this.autoListeners.add(notitfy);
    }
    
    public void removeSender(final ICommandSender sender) {
        this.autoListeners.remove(fromSender(sender, false));
    }
    
    public int getState(final ICommandSender sender) {
        for (final INotifyListener listen : this.autoListeners) {
            if (listen.matches(sender)) {
                return listen.isListening() ? 1 : 2;
            }
        }
        return 0;
    }
    
    public boolean autoListens(final ICommandSender sender) {
        for (final INotifyListener listen : this.autoListeners) {
            if (listen.matches(sender)) {
                return listen.isListening();
            }
        }
        return true;
    }
    
    public void addListeners(final GlobalListeners listen, final MinecraftServer server) {
        for (final INotifyListener listener : this.autoListeners) {
            if (listener.isListening()) {
                final ICommandSender sender = listener.getSender(server);
                if (sender == null) {
                    continue;
                }
                listen.addListener(sender);
            }
        }
    }
    
    public static INotifyListener fromNBT(final NBTTagCompound nbt) {
        switch (nbt.func_74762_e("Type")) {
            case 0: {
                return new INotifyListener.ServerListener(nbt);
            }
            case 1: {
                return new INotifyListener.PlayerListener(nbt);
            }
            default: {
                return null;
            }
        }
    }
    
    public static INotifyListener fromSender(final ICommandSender sender, final boolean listens) {
        if (sender instanceof EntityPlayer) {
            return new INotifyListener.PlayerListener((EntityPlayer)sender, listens);
        }
        if (sender instanceof MinecraftServer) {
            return new INotifyListener.ServerListener(listens);
        }
        return null;
    }
    
    public class PregenMemory
    {
        Map<Long, Byte> chunkState;
        
        public PregenMemory() {
            this.chunkState = new LinkedHashMap<Long, Byte>();
        }
        
        public PregenMemory(final NBTTagCompound nbt) {
            this.chunkState = new LinkedHashMap<Long, Byte>();
            final int size = nbt.func_74762_e("size");
            final ByteBuffer buffer = ByteBuffer.wrap(nbt.func_74770_j("data"));
            for (int i = 0; i < size; ++i) {
                this.chunkState.put(buffer.getLong(), buffer.get());
            }
        }
        
        public void setState(final long key, final byte value) {
            if (value == 0) {
                this.chunkState.remove(key);
                TaskStorage.this.func_76185_a();
                return;
            }
            this.chunkState.put(key, value);
            TaskStorage.this.func_76185_a();
        }
        
        public int getState(final long key) {
            final Byte value = this.chunkState.get(key);
            return (int)((value == null) ? 0 : value);
        }
        
        public NBTTagCompound save() {
            final ByteBuffer buffer = ByteBuffer.allocate(this.chunkState.size() * 9);
            for (final Map.Entry<Long, Byte> state : this.chunkState.entrySet()) {
                buffer.putLong(state.getKey());
                buffer.put(state.getValue());
            }
            final NBTTagCompound nbt = new NBTTagCompound();
            nbt.func_74773_a("data", buffer.array());
            nbt.func_74768_a("size", this.chunkState.size());
            return nbt;
        }
    }
}
