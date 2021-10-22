// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.infos;

import pregenerator.impl.processor.generator.ChunkProcessor;
import pregenerator.base.api.misc.IRenderHelper;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.base.api.misc.IConfig;
import pregenerator.impl.client.ClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;

public abstract class InfoEntry
{
    public static final DecimalFormat FORMAT;
    static List<InfoEntry> REGISTRY;
    int id;
    boolean isActive;
    
    public InfoEntry() {
        this.id = -1;
        this.isActive = false;
    }
    
    public static List<InfoEntry> getRegistry() {
        return new ArrayList<InfoEntry>(InfoEntry.REGISTRY);
    }
    
    public static InfoEntry getByID(final int id) {
        if (InfoEntry.REGISTRY.size() > id && id >= 0) {
            return InfoEntry.REGISTRY.get(id);
        }
        return null;
    }
    
    public static void init() {
        new TaskEntry();
        new RadiusEntry();
        new CenterEntry();
        new GenerationType();
        new PrepaireEntry();
        new LoadedChunksEntry();
        new RunningTimeEntry();
        new LagBarEntry();
        new ServerRamEntry();
        new LoadedFilesEntry();
        new PlayerLimitEntry();
        new ProgressBarEntry();
    }
    
    protected final void register() {
        for (final InfoEntry entry : InfoEntry.REGISTRY) {
            if (entry.getName().equals(this.getName())) {
                throw new RuntimeException("Duplicated is now allowed");
            }
        }
        this.id = InfoEntry.REGISTRY.size();
        InfoEntry.REGISTRY.add(this);
    }
    
    @SideOnly(Side.CLIENT)
    public final void setActive(final boolean state) {
        this.isActive = state;
        this.onValueChanged();
    }
    
    @SideOnly(Side.CLIENT)
    public final boolean isActive() {
        return this.isActive;
    }
    
    @SideOnly(Side.CLIENT)
    private final void onValueChanged() {
        ClientHandler.INSTANCE.info.saveEntry(this);
    }
    
    @SideOnly(Side.CLIENT)
    public final void readFromConfig(final IConfig config) {
        this.isActive = config.getBoolean("general", this.getName(), true);
    }
    
    public abstract String getName();
    
    public boolean shouldRender() {
        return true;
    }
    
    public int getYOffset() {
        return 6;
    }
    
    public final int getID() {
        return this.id;
    }
    
    public abstract void write(final IWriteableBuffer p0);
    
    public abstract void read(final IReadableBuffer p0);
    
    public abstract int currentValue();
    
    public abstract int maxValue();
    
    @SideOnly(Side.CLIENT)
    public abstract void render(final int p0, final int p1, final float p2, final int p3, final IRenderHelper p4);
    
    public ChunkProcessor getProcessor() {
        return ChunkProcessor.INSTANCE;
    }
    
    static {
        FORMAT = new DecimalFormat("###,###");
        InfoEntry.REGISTRY = new ArrayList<InfoEntry>();
    }
}
