// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.processor.generator;

import net.minecraft.world.MinecraftException;
import java.io.IOException;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import pregenerator.impl.misc.FilePos;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraft.world.chunk.Chunk;
import pregenerator.impl.tracking.WorldTracker;
import java.util.ArrayList;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import java.util.HashSet;
import net.minecraft.world.World;
import pregenerator.impl.tracking.ServerTracker;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.world.NextTickListEntry;
import java.util.Set;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.ChunkProviderServer;

public class ChunkHelper
{
    ChunkProviderServer server;
    WorldServer world;
    Set<NextTickListEntry> set;
    List<Entity> entities;
    
    public ChunkHelper(final ChunkProviderServer data, final WorldServer worlding) {
        this.server = data;
        this.world = worlding;
        final WorldTracker tracker = ServerTracker.INSTANCE.getWorld(worlding);
        if (tracker != null) {
            this.set = tracker.tickUpdates;
        }
        else {
            this.set = new HashSet<NextTickListEntry>();
        }
        try {
            this.entities = (List<Entity>)ReflectionHelper.getPrivateValue((Class)World.class, (Object)this.world, 4);
        }
        catch (Exception e) {
            this.entities = new ArrayList<Entity>();
            e.printStackTrace();
        }
    }
    
    public boolean containsChunk(final long pos) {
        return this.server.field_73244_f.func_76161_b(pos);
    }
    
    public void storeChunk(final long pos, final Chunk chunk) {
        this.server.field_73244_f.func_76163_a(pos, (Object)chunk);
        this.server.field_73245_g.add(chunk);
    }
    
    public int getChunkCount() {
        return this.server.field_73244_f.func_76162_a();
    }
    
    public void clearUnloadedEntities() {
        this.entities.clear();
    }
    
    public void unloadChunk(final Chunk chunk) {
        this.server.func_73241_b(chunk.field_76635_g, chunk.field_76647_h);
    }
    
    public void cleanUp() {
        if (this.set.size() > 1000) {
            for (int count = 1 + this.set.size() / 1000; count > 0; --count) {
                try {
                    this.world.func_72955_a(true);
                }
                catch (Exception e) {
                    FMLLog.getLogger().info("Prevented Server Crash of Random Tick Cleanup");
                    e.printStackTrace();
                }
            }
        }
    }
    
    public Chunk createChunk(final int x, final int z, final boolean disablePost, final boolean post) {
        try {
            final Chunk chunk = this.server.field_73246_d.func_73154_d(x, z);
            if (disablePost) {
                chunk.func_177446_d(true);
            }
            if (post) {
                this.storeChunk(FilePos.asLong(x, z), chunk);
            }
            else {
                this.saveChunk(chunk);
            }
            return chunk;
        }
        catch (Throwable throwable) {
            final long i = FilePos.asLong(x, z);
            final CrashReport crashreport = CrashReport.func_85055_a(throwable, "Exception generating new chunk");
            final CrashReportCategory crashreportcategory = crashreport.func_85058_a("Chunk to be generated");
            crashreportcategory.func_71507_a("Location", (Object)String.format("%d,%d", x, z));
            crashreportcategory.func_71507_a("Position hash", (Object)i);
            crashreportcategory.func_71507_a("Generator", (Object)this.server.field_73246_d);
            throw new ReportedException(crashreport);
        }
    }
    
    public Chunk loadChunk(final int x, final int z) {
        Chunk chunk = (Chunk)this.server.field_73244_f.func_76164_a(FilePos.asLong(x, z));
        if (chunk == null) {
            final IChunkLoader loader = this.server.field_73247_e;
            if (loader instanceof AnvilChunkLoader) {
                final AnvilChunkLoader anvil = (AnvilChunkLoader)loader;
                try {
                    final Object[] data = anvil.loadChunk__Async((World)this.world, x, z);
                    if (data == null) {
                        FMLLog.getLogger().info("Tried to Load a Chunk that doesn't exists! at [x=" + x + ", y=" + z + "] Please make sure that that one is deleted!");
                        return null;
                    }
                    chunk = (Chunk)data[0];
                    final NBTTagCompound nbt = (NBTTagCompound)data[1];
                    anvil.loadEntities((World)this.world, nbt.func_74775_l("Level"), chunk);
                    MinecraftForge.EVENT_BUS.post((Event)new ChunkDataEvent.Load(chunk, nbt));
                    chunk.func_177432_b(this.world.func_82737_E());
                    this.server.field_73246_d.func_180514_a(chunk, x, z);
                    this.storeChunk(FilePos.asLong(x, z), chunk);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    chunk = loader.func_75815_a((World)this.world, x, z);
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return chunk;
    }
    
    public void saveChunk(final Chunk chunk) {
        try {
            chunk.func_177432_b(this.world.func_82737_E());
            this.server.field_73247_e.func_75816_a((World)this.world, chunk);
            chunk.func_177427_f(false);
        }
        catch (IOException ioexception) {
            FMLLog.getLogger().error("Couldn't save chunk", (Throwable)ioexception);
        }
        catch (MinecraftException minecraftexception) {
            FMLLog.getLogger().error("Couldn't save chunk; already in use by another instance of Minecraft?", (Throwable)minecraftexception);
        }
    }
}
