// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.tracking;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.util.Iterator;
import pregenerator.ConfigManager;
import pregenerator.ChunkPregenerator;
import java.util.LinkedHashMap;
import java.util.concurrent.FutureTask;
import java.util.Queue;
import net.minecraft.world.World;
import java.util.Map;

public class ServerTracker
{
    public static final ServerTracker INSTANCE;
    Map<World, WorldTracker> mapping;
    private boolean enabled;
    TimeTracker tracker;
    AverageCounter packets;
    Queue<FutureTask<?>> packetList;
    
    public ServerTracker() {
        this.mapping = new LinkedHashMap<World, WorldTracker>();
        this.enabled = false;
        this.tracker = new TimeTracker(40);
        this.packets = new AverageCounter(40);
        this.packetList = null;
    }
    
    public void init() {
        ChunkPregenerator.pregenBase.registerTickEvent(this);
        this.enabled = ConfigManager.tracking;
    }
    
    public void toggle() {
        ConfigManager.changeTracking(this.enabled = !this.enabled);
        for (final WorldTracker entry : this.mapping.values()) {
            entry.setState(this.enabled);
        }
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    @SubscribeEvent
    public void onServerTick(final TickEvent.ServerTickEvent event) {
        if (!this.enabled) {
            return;
        }
        if (event.phase == TickEvent.Phase.START) {
            this.tracker.setStart();
            if (this.packetList == null) {
                this.packetList = this.getServerList();
            }
            this.packets.addMore(this.packetList.size());
            this.packets.onFinished();
        }
        else {
            this.tracker.onFinished();
        }
    }
    
    @SubscribeEvent
    public void onWorldTick(final TickEvent.WorldTickEvent event) {
        if (!this.enabled) {
            return;
        }
        final WorldTracker tracker = this.mapping.get(event.world);
        if (tracker != null) {
            if (event.phase == TickEvent.Phase.START) {
                tracker.onTickStart();
            }
            else {
                tracker.onTickEnd();
            }
        }
    }
    
    @SubscribeEvent
    public void onWorldLoad(final WorldEvent.Load load) {
        final World world = load.world;
        if (world instanceof WorldServer) {
            this.mapping.put(world, new WorldTracker((WorldServer)world, this.enabled));
        }
    }
    
    @SubscribeEvent
    public void onWorldUnload(final WorldEvent.Unload unload) {
        this.mapping.remove(unload.world);
    }
    
    public void onServerStopped() {
        this.mapping.clear();
    }
    
    public MinecraftServer getServer() {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }
    
    public WorldTracker getWorld(final int dim) {
        return this.mapping.get(DimensionManager.getWorld(dim));
    }
    
    public WorldTracker getWorld(final World world) {
        return this.mapping.get(world);
    }
    
    public long getAverage() {
        return this.tracker.getAverage();
    }
    
    public int getPackets() {
        return this.packets.getAverage();
    }
    
    public List<WorldTracker> getTracker() {
        return new ArrayList<WorldTracker>(this.mapping.values());
    }
    
    public Queue<FutureTask<?>> getServerList() {
        try {
            final Class toCast = Queue.class;
            final Class clz = MinecraftServer.class;
            final Field[] data = clz.getDeclaredFields();
            for (int i = 0; i < data.length; ++i) {
                final Field field = data[i];
                if (toCast.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    return (Queue<FutureTask<?>>)field.get(this.getServer());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayDeque<FutureTask<?>>();
    }
    
    static {
        INSTANCE = new ServerTracker();
    }
}
