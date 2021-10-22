// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.retrogen;

import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.world.World;
import java.util.Random;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import net.minecraftforge.common.MinecraftForge;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import pregenerator.impl.misc.FilePos;
import pregenerator.base.api.misc.IConfig;
import java.util.Map;
import net.minecraftforge.fml.common.IWorldGenerator;
import java.util.Set;

public final class RetrogenHandler
{
    public static final RetrogenHandler INSTANCE;
    Set<IWorldGenerator> generators;
    Set<String> worldGenerators;
    Map<String, IWorldGenerator> activeGenerators;
    IConfig configuration;
    Set<String> toLoad;
    boolean ignoreChanges;
    Set<FilePos> chunksToIgnore;
    
    private RetrogenHandler() {
        this.worldGenerators = new LinkedHashSet<String>();
        this.activeGenerators = new LinkedHashMap<String, IWorldGenerator>();
        this.toLoad = new LinkedHashSet<String>();
        this.ignoreChanges = false;
        this.chunksToIgnore = new HashSet<FilePos>();
    }
    
    public void preInit(final IConfig config) {
        this.configuration = config;
        final String data = config.getString("retrogen", "activeworldgenerators", "", "List of Currently active WorldGenerators");
        final String[] split = data.split(";");
        if (split != null && split.length > 0) {
            for (final String s : split) {
                this.toLoad.add(s);
            }
        }
    }
    
    public void init() {
        this.generators = this.getGenerators();
        this.ignoreChanges = true;
        final List<String> toAdd = new ArrayList<String>(this.toLoad);
        this.toLoad.clear();
        for (final String s : toAdd) {
            this.enableGenerator(s);
        }
        this.ignoreChanges = false;
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    private void updateGenerators() {
        this.worldGenerators.clear();
        for (final IWorldGenerator gen : this.generators) {
            this.worldGenerators.add(gen.getClass().getName());
        }
    }
    
    public List<String> getAllGenerators() {
        if (this.generators.size() != this.worldGenerators.size()) {
            this.updateGenerators();
        }
        return new ArrayList<String>(this.worldGenerators);
    }
    
    public List<String> getInactiveGenerators() {
        if (this.generators.size() != this.worldGenerators.size()) {
            this.updateGenerators();
        }
        final Set<String> result = new LinkedHashSet<String>(this.worldGenerators);
        result.removeAll(this.activeGenerators.keySet());
        return new ArrayList<String>(result);
    }
    
    public boolean isValidGenerator(final String name) {
        if (this.generators.size() != this.worldGenerators.size()) {
            this.updateGenerators();
        }
        return this.worldGenerators.contains(name);
    }
    
    public Set<String> getActiveGenerators() {
        return new LinkedHashSet<String>(this.activeGenerators.keySet());
    }
    
    @SideOnly(Side.CLIENT)
    public void updateActiveGenerators(final List<String> list) {
        this.ignoreChanges = true;
        this.activeGenerators.clear();
        this.toLoad.clear();
        for (final String s : list) {
            this.enableGenerator(s);
        }
        this.ignoreChanges = false;
        this.onConfigChanged();
    }
    
    public boolean isGeneratorActive(final String name) {
        return this.activeGenerators.containsKey(name);
    }
    
    public void enableGenerator(final String name) {
        boolean found = false;
        for (final IWorldGenerator gen : this.generators) {
            if (gen.getClass().getName().equalsIgnoreCase(name)) {
                this.activeGenerators.put(name, gen);
                found = true;
                break;
            }
        }
        if (found) {
            this.toLoad.add(name);
            this.onConfigChanged();
        }
    }
    
    public void disableGenerator(final String name) {
        this.activeGenerators.remove(name);
        this.toLoad.remove(name);
        this.onConfigChanged();
    }
    
    private void onConfigChanged() {
        if (this.ignoreChanges) {
            return;
        }
        final StringBuilder builder = new StringBuilder();
        for (final String s : this.toLoad) {
            builder.append(s).append(";");
        }
        this.configuration.setString("retrogen", "activeworldgenerators", builder.toString());
    }
    
    public boolean retrogenChunk(final Chunk chunk, final IChunkProvider chunkGenerator, final IChunkProvider chunkProvider) {
        final int chunkX = chunk.field_76635_g;
        final int chunkZ = chunk.field_76647_h;
        if (this.chunksToIgnore.contains(new FilePos(chunkX, chunkZ))) {
            return false;
        }
        final World world = chunk.func_177412_p();
        final long worldSeed = world.func_72905_C();
        final Random fmlRandom = new Random(worldSeed);
        final long xSeed = fmlRandom.nextLong() >> 3;
        final long zSeed = fmlRandom.nextLong() >> 3;
        final long chunkSeed = xSeed * chunkX + zSeed * chunkZ ^ worldSeed;
        for (final IWorldGenerator generator : this.activeGenerators.values()) {
            try {
                fmlRandom.setSeed(chunkSeed);
                generator.generate(fmlRandom, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.chunksToIgnore.add(new FilePos(chunkX, chunkZ));
        return true;
    }
    
    private Set<IWorldGenerator> getGenerators() {
        try {
            return (Set<IWorldGenerator>)ReflectionHelper.getPrivateValue((Class)GameRegistry.class, (Object)null, new String[] { "worldGenerators" });
        }
        catch (Exception e) {
            return new HashSet<IWorldGenerator>();
        }
    }
    
    private String getCurrentType() {
        final StringBuilder builder = new StringBuilder();
        for (final String s : this.toLoad) {
            builder.append(s).append(";");
        }
        return builder.toString();
    }
    
    @SubscribeEvent
    public void onChunkLoad(final ChunkDataEvent.Load evt) {
        if (this.toLoad.size() > 0 && evt.getData().func_74764_b(this.getCurrentType())) {
            final Chunk chunk = evt.getChunk();
            this.chunksToIgnore.add(new FilePos(chunk.field_76635_g, chunk.field_76647_h));
        }
    }
    
    @SubscribeEvent
    public void onChunkSave(final ChunkDataEvent.Save evt) {
        final Chunk chunk = evt.getChunk();
        if (this.chunksToIgnore.contains(new FilePos(chunk.field_76635_g, chunk.field_76647_h))) {
            evt.getData().func_74757_a(this.getCurrentType(), true);
        }
    }
    
    @SubscribeEvent
    public void onChunkUnload(final ChunkEvent.Unload evt) {
        final Chunk chunk = evt.getChunk();
        this.chunksToIgnore.remove(new FilePos(chunk.field_76635_g, chunk.field_76647_h));
    }
    
    public void onServerStopped() {
        this.chunksToIgnore.clear();
    }
    
    static {
        INSTANCE = new RetrogenHandler();
    }
}
