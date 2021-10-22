// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.processor.generator;

import java.util.LinkedHashMap;
import pregenerator.impl.misc.FilePos;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public enum ChunkLogger
{
    Chunks("TaskList", true) {
        @Override
        public void addPreLog(final StringBuilder builder, final ChunkProcessor process, final FilePos pos) {
            builder.append(process.processed + "/" + process.currentTask.getTotalWorkList() + " Chunks");
        }
    }, 
    Position("CurrentPosition", true) {
        @Override
        public void addPreLog(final StringBuilder builder, final ChunkProcessor process, final FilePos pos) {
            builder.append("<Pos: " + pos.x + ", " + pos.z + ">");
        }
    }, 
    AverageGen("Generation-Speed", true) {
        @Override
        public void addPreLog(final StringBuilder builder, final ChunkProcessor process, final FilePos pos) {
            builder.append("<Average/t: " + ChunkProcessor.format.format(process.counter.getAverage()) + ">");
        }
    }, 
    AverageLag("CPU-Usage", false) {
        @Override
        public void addPreLog(final StringBuilder builder, final ChunkProcessor process, final FilePos pos) {
            builder.append("<Average Lag: " + ChunkProcessor.format.format(process.timer.getAverageDelta()) + "MS>");
        }
    }, 
    LoadedChunks("Loaded-Chunks", false) {
        @Override
        public void addPreLog(final StringBuilder builder, final ChunkProcessor process, final FilePos pos) {
            builder.append("<Loaded Chunks: " + process.getLoadedChunks() + ">");
        }
    }, 
    LoadedRegionFiles("Loaded-Files", false) {
        @Override
        public void addPreLog(final StringBuilder builder, final ChunkProcessor process, final FilePos pos) {
            builder.append("<Loaded Files: " + ChunkThread.getLoadedFiles() + ">");
        }
    }, 
    ExpectedTime("ExpectedTime", false) {
        @Override
        public void addPreLog(final StringBuilder builder, final ChunkProcessor process, final FilePos pos) {
            final int total = process.currentTask.getTotalWorkList();
            final long left = (long)((total - process.processed) / process.counter.getAverage());
            final long expected = left * process.timer.getAverageDelta();
            builder.append("<Expected Time: " + process.formatIntoTime(expected) + ">");
        }
    }, 
    Ram("RamUsage", true) {
        @Override
        public void addPreLog(final StringBuilder builder, final ChunkProcessor process, final FilePos pos) {
            builder.append("Ram: " + process.getRamUsage() + "MB");
        }
    };
    
    boolean theDefault;
    String ID;
    static Map<String, ChunkLogger> loggerInfo;
    
    private ChunkLogger(final String visibleName, final boolean pre) {
        this.ID = visibleName;
        this.theDefault = pre;
    }
    
    public String getName() {
        return this.ID;
    }
    
    public boolean getDefault() {
        return this.theDefault;
    }
    
    public static ChunkLogger byID(final String id) {
        return ChunkLogger.loggerInfo.get(id);
    }
    
    public static List<String> getNames() {
        return new ArrayList<String>(ChunkLogger.loggerInfo.keySet());
    }
    
    public static List<ChunkLogger> getLoggers() {
        return new ArrayList<ChunkLogger>(ChunkLogger.loggerInfo.values());
    }
    
    public void addPreLog(final StringBuilder builder, final ChunkProcessor process, final FilePos pos) {
    }
    
    static {
        ChunkLogger.loggerInfo = new LinkedHashMap<String, ChunkLogger>();
        for (final ChunkLogger info : values()) {
            ChunkLogger.loggerInfo.put(info.getName(), info);
        }
    }
}
