// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.trackerInfo;

import pregenerator.impl.tracking.ServerTracker;
import pregenerator.base.api.misc.IRenderHelper;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.base.api.misc.IConfig;
import pregenerator.impl.client.ClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.util.Iterator;
import pregenerator.impl.client.trackerInfo.detailed.EntitiesEntry;
import pregenerator.impl.client.trackerInfo.detailed.TileEntities;
import pregenerator.impl.client.trackerInfo.detailed.BlockTicksEntry;
import pregenerator.impl.client.trackerInfo.detailed.BlockUpdatesEntry;
import pregenerator.impl.client.trackerInfo.detailed.LoadedChunks;
import pregenerator.impl.client.trackerInfo.detailed.DimensionEntry;
import pregenerator.impl.client.trackerInfo.detailed.DetailedInfoEntry;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public abstract class TrackerEntry
{
    static List<TrackerEntry> REGISTRY;
    int id;
    boolean isActive;
    
    public TrackerEntry() {
        this.id = -1;
        this.isActive = false;
    }
    
    public static List<TrackerEntry> getRegistry() {
        return new ArrayList<TrackerEntry>(TrackerEntry.REGISTRY);
    }
    
    public static TrackerEntry getByID(final int id) {
        if (TrackerEntry.REGISTRY.size() > id && id >= 0) {
            return TrackerEntry.REGISTRY.get(id);
        }
        return null;
    }
    
    public static void init() {
        new RamUsage();
        new PacketUsage();
        new ServerUsage();
        new WorldUsage();
        new DetailedInfoEntry();
        new DimensionEntry();
        new LoadedChunks();
        new BlockUpdatesEntry();
        new BlockTicksEntry();
        new TileEntities();
        new EntitiesEntry();
    }
    
    protected final void register() {
        for (final TrackerEntry entry : TrackerEntry.REGISTRY) {
            if (entry.getName().equals(this.getName())) {
                throw new RuntimeException("Duplicated is now allowed");
            }
        }
        this.id = TrackerEntry.REGISTRY.size();
        TrackerEntry.REGISTRY.add(this);
    }
    
    public boolean hasConfig() {
        return true;
    }
    
    public final int getID() {
        return this.id;
    }
    
    @SideOnly(Side.CLIENT)
    public final void setActive(final boolean state) {
        if (!this.hasConfig()) {
            this.isActive = true;
            return;
        }
        this.isActive = state;
        this.onValueChanged();
    }
    
    @SideOnly(Side.CLIENT)
    public final boolean isActive() {
        return this.isActive;
    }
    
    @SideOnly(Side.CLIENT)
    private final void onValueChanged() {
        ClientHandler.INSTANCE.tracker.saveEntry(this);
    }
    
    @SideOnly(Side.CLIENT)
    public final void readFromConfig(final IConfig config) {
        this.isActive = (!this.hasConfig() || config.getBoolean("tracking", this.getName(), true));
    }
    
    public abstract String getName();
    
    @SideOnly(Side.CLIENT)
    public boolean shouldRender() {
        return true;
    }
    
    public int getYOffset() {
        return 6;
    }
    
    public abstract void writeServer(final IWriteableBuffer p0);
    
    public abstract void readClient(final IReadableBuffer p0);
    
    @SideOnly(Side.CLIENT)
    public void writeClient(final IWriteableBuffer buf) {
    }
    
    public void readServer(final IReadableBuffer buf) {
    }
    
    public abstract int currentValue();
    
    public abstract int maxValue();
    
    @SideOnly(Side.CLIENT)
    public abstract void render(final int p0, final int p1, final float p2, final int p3, final IRenderHelper p4);
    
    public ServerTracker getTracker() {
        return ServerTracker.INSTANCE;
    }
    
    static {
        TrackerEntry.REGISTRY = new ArrayList<TrackerEntry>();
    }
}
