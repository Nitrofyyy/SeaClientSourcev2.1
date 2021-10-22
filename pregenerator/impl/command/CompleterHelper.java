// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command;

import pregenerator.impl.retrogen.RetrogenHandler;
import pregenerator.impl.structure.StructureManager;
import pregenerator.impl.storage.TaskStorage;
import java.util.Iterator;
import pregenerator.impl.processor.generator.ChunkProcessor;
import java.util.EnumSet;
import pregenerator.impl.processor.generator.ChunkLogger;
import java.io.File;
import pregenerator.ChunkPregenerator;
import java.util.Collection;
import java.util.Arrays;
import net.minecraftforge.common.DimensionManager;
import java.util.ArrayList;
import java.util.List;

public class CompleterHelper
{
    public static final ICompleter GEN_TYPE;
    public static final ICompleter GEN_RADIUS_CHUNK;
    public static final ICompleter GEN_RADIUS_BLOCK;
    public static final ICompleter DIMENSION;
    public static final ICompleter GEN_PROCESS;
    public static final ICompleter GEN_DELAY;
    public static final ICompleter GEN_FILE;
    public static final ICompleter INFO_ADD;
    public static final ICompleter INFO_REMOVE;
    public static final ICompleter TASKLIST;
    public static final ICompleter RETROGEN_ADD;
    public static final ICompleter RETROGEN_REMOVE;
    public static final ICompleter STRUCTURE_DIMENSION;
    
    public static final ICompleter getStructures(final int dim, final boolean all) {
        return new StructureCompleter(dim, all);
    }
    
    static {
        GEN_TYPE = new ListCompleter(new String[] { "square", "circle" });
        GEN_RADIUS_CHUNK = new ListCompleter(new String[] { "10", "25", "50", "75", "100", "200", "250", "500", "750", "1000" });
        GEN_RADIUS_BLOCK = new ListCompleter(new String[] { "b10", "b25", "b50", "b75", "b100", "b200", "b250", "b500", "b750", "b1000" });
        DIMENSION = new DimCompleter();
        GEN_PROCESS = new ListCompleter(new String[] { "TerrainOnly", "PostProcessingOnly", "BlockingPostProcessing", "Retrogen" });
        GEN_DELAY = new ListCompleter(new String[] { "20", "200", "600", "1200", "3600", "6000", "12000" });
        GEN_FILE = new FileCompleter();
        INFO_ADD = new AddCompleter();
        INFO_REMOVE = new RemoveCompleter();
        TASKLIST = new TaskCompleter();
        RETROGEN_ADD = new RetrogenAdd();
        RETROGEN_REMOVE = new RetrogenRemove();
        STRUCTURE_DIMENSION = new StructureDims();
    }
    
    public static class DimCompleter implements ICompleter
    {
        List<String> list;
        int lastDim;
        
        public DimCompleter() {
            this.list = new ArrayList<String>();
            this.lastDim = -1;
        }
        
        @Override
        public List<String> getCompleter() {
            this.updateList();
            return this.list;
        }
        
        private void updateList() {
            final Integer[] data = DimensionManager.getIDs();
            if (data.length != this.lastDim) {
                this.lastDim = data.length;
                this.list.clear();
                for (final Integer entry : data) {
                    this.list.add(entry.toString());
                }
            }
        }
    }
    
    public static class ListCompleter implements ICompleter
    {
        public List<String> list;
        
        public ListCompleter(final String... s) {
            (this.list = new ArrayList<String>()).addAll(Arrays.asList(s));
        }
        
        @Override
        public List<String> getCompleter() {
            return this.list;
        }
    }
    
    public static class FileCompleter implements ICompleter
    {
        @Override
        public List<String> getCompleter() {
            final List<String> list = new ArrayList<String>();
            for (final File file : ChunkPregenerator.pregeneratorFolder.listFiles()) {
                if (file.getName().contains(".txt")) {
                    list.add(file.getName());
                }
            }
            return list;
        }
    }
    
    public static class AddCompleter implements ICompleter
    {
        @Override
        public List<String> getCompleter() {
            final EnumSet<ChunkLogger> logger = EnumSet.allOf(ChunkLogger.class);
            logger.removeAll(ChunkProcessor.INSTANCE.getLoggerInfo());
            final List<String> list = new ArrayList<String>();
            for (final ChunkLogger entry : logger) {
                list.add(entry.getName());
            }
            return list;
        }
    }
    
    public static class RemoveCompleter implements ICompleter
    {
        @Override
        public List<String> getCompleter() {
            final List<String> list = new ArrayList<String>();
            for (final ChunkLogger entry : ChunkProcessor.INSTANCE.getLoggerInfo()) {
                list.add(entry.getName());
            }
            return list;
        }
    }
    
    public static class TaskCompleter implements ICompleter
    {
        @Override
        public List<String> getCompleter() {
            final int count = TaskStorage.getStorage().getTaskCount();
            if (count == 0) {
                return new ArrayList<String>();
            }
            if (count == 1) {
                return Arrays.asList("first");
            }
            final List<String> list = new ArrayList<String>();
            list.add("first");
            list.add("last");
            for (int i = 0; i < count; ++i) {
                list.add(i + "");
            }
            return list;
        }
    }
    
    public static class StructureCompleter implements ICompleter
    {
        int world;
        boolean all;
        
        public StructureCompleter(final int world, final boolean all) {
            this.world = world;
            this.all = all;
        }
        
        @Override
        public List<String> getCompleter() {
            final List<String> list = StructureManager.instance.getAllTypes(this.world);
            if (this.all) {
                list.add(0, "All");
            }
            return list;
        }
    }
    
    public static class StructureDims implements ICompleter
    {
        @Override
        public List<String> getCompleter() {
            return StructureManager.instance.getDims();
        }
    }
    
    public static class RetrogenAdd implements ICompleter
    {
        @Override
        public List<String> getCompleter() {
            return RetrogenHandler.INSTANCE.getInactiveGenerators();
        }
    }
    
    public static class RetrogenRemove implements ICompleter
    {
        @Override
        public List<String> getCompleter() {
            return new ArrayList<String>(RetrogenHandler.INSTANCE.getActiveGenerators());
        }
    }
    
    public interface ICompleter
    {
        List<String> getCompleter();
    }
}
