// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.processor.generator;

import net.minecraft.server.MinecraftServer;
import java.util.Iterator;
import pregenerator.impl.misc.FilePos;
import net.minecraft.world.World;
import pregenerator.impl.storage.TaskStorage;
import pregenerator.impl.storage.GlobalListeners;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import pregenerator.ChunkPregenerator;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraft.util.IProgressUpdate;
import pregenerator.ConfigManager;
import pregenerator.impl.misc.ProcessResult;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.util.concurrent.atomic.AtomicLong;
import pregenerator.impl.storage.PregenTask;
import java.util.concurrent.Future;
import pregenerator.impl.processor.PrepaireProgress;
import java.util.EnumSet;
import pregenerator.impl.misc.FileCounter;
import pregenerator.impl.misc.ChunkTimer;
import pregenerator.impl.misc.DeltaTimer;
import java.text.DecimalFormat;

public class ChunkProcessor
{
    public static final DecimalFormat format;
    public static ChunkProcessor INSTANCE;
    public static final int IDLE_MODE = 0;
    public static final int PROCESSING_MODE = 1;
    public static final int POST_PROCESSING_MODE = 2;
    int ticker;
    DeltaTimer timer;
    ChunkTimer chunkTimer;
    FileCounter counter;
    FileCounter memoryAverage;
    int maxTimePerTick;
    EnumSet<ChunkLogger> log;
    public PrepaireProgress progress;
    Future<ChunkProcess> future;
    PregenTask task;
    ChunkProcess currentTask;
    int mode;
    int processed;
    int skipped;
    int failed;
    long startTime;
    boolean priority;
    boolean working;
    AtomicLong threadID;
    
    public ChunkProcessor() {
        this.ticker = 0;
        this.timer = new DeltaTimer();
        this.chunkTimer = new ChunkTimer();
        this.counter = new FileCounter();
        this.memoryAverage = new FileCounter();
        this.maxTimePerTick = 40;
        this.log = EnumSet.noneOf(ChunkLogger.class);
        this.progress = new PrepaireProgress();
        this.future = null;
        this.task = null;
        this.currentTask = null;
        this.mode = 0;
        this.processed = 0;
        this.skipped = 0;
        this.failed = 0;
        this.startTime = -1L;
        this.priority = false;
        this.working = false;
        this.threadID = new AtomicLong(-1L);
    }
    
    @SubscribeEvent
    public void onServerTickEvent(final TickEvent.ServerTickEvent event) {
        if (this.isStopped() || this.task == null) {
            return;
        }
        if (this.shouldDisable()) {
            return;
        }
        if (this.future != null) {
            final int newTime = (int)((System.currentTimeMillis() - this.startTime) / 1000L);
            if (this.ticker != newTime) {
                this.ticker = newTime;
                this.sendChatMessage("Prepaire Progress: " + this.progress.getCurrent() + " / " + this.progress.getMax());
            }
            if (this.future.isDone()) {
                try {
                    this.currentTask = this.future.get();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                this.future = null;
                if (this.currentTask != null) {
                    this.currentTask.setStartMemory();
                }
                this.progress.reset();
                if (this.mode == 1) {
                    if (this.task.isPostProcessingTask()) {
                        this.sendChatMessage("Processing: " + this.currentTask.getTotalWorkList() + " Chunks, with a possible " + this.currentTask.getTotalWorkList() + " Chunks to Post Generate");
                    }
                    else {
                        this.sendChatMessage("Processing: " + this.currentTask.getTotalWorkList() + " Chunks");
                    }
                }
            }
            return;
        }
        if (this.currentTask == null) {
            return;
        }
        if (event.phase == TickEvent.Phase.START) {
            this.timer.start();
        }
        else {
            long deltaTime = this.timer.averageDelta();
            if (deltaTime >= this.maxTimePerTick) {
                this.working = false;
                return;
            }
            this.working = true;
            if (this.priority) {
                deltaTime = 0L;
            }
            this.chunkTimer.startTime();
            this.currentTask.onTickStart();
            try {
                while (deltaTime + this.timer.getDeltaTime() < this.maxTimePerTick && this.currentTask.hasWork()) {
                    final ProcessResult result = this.currentTask.tick();
                    if (this.currentTask.hasLight()) {
                        this.currentTask.checkLight();
                    }
                    ++this.processed;
                    this.counter.onChunkProcessed();
                    this.chunkTimer.onChunkFinished();
                    if (result == ProcessResult.CRASH) {
                        ++this.failed;
                    }
                    else {
                        if (result != ProcessResult.MISSING) {
                            continue;
                        }
                        ++this.skipped;
                    }
                }
                final int newTime2 = (int)((System.currentTimeMillis() - this.startTime) / 1000L);
                if (newTime2 != this.ticker) {
                    this.ticker = newTime2;
                    this.buildPreText(this.currentTask.getPosition());
                }
                if (!this.currentTask.hasWork()) {
                    if (this.currentTask.hasLight()) {
                        this.currentTask.checkLight();
                    }
                    else {
                        this.onFinished();
                    }
                }
                else {
                    this.getCounter().onTickEnded();
                    this.timer.finishDeltaTime();
                    this.memoryAverage.add(this.freeMemory());
                    if (ConfigManager.autoRestart && ConfigManager.restartMemory > this.memoryAverage.getIntAverage()) {
                        try {
                            for (final WorldServer world : this.getServer().field_71305_c) {
                                if (world != null) {
                                    final boolean flag = world.field_73058_d;
                                    world.field_73058_d = false;
                                    world.func_73044_a(true, (IProgressUpdate)null);
                                    world.field_73058_d = flag;
                                }
                            }
                            ThreadedFileIOBase.func_178779_a().func_75734_a();
                        }
                        catch (Exception e2) {
                            e2.printStackTrace();
                        }
                        ChunkPregenerator.LOGGER.info("Auto Restart Enabled (Can be disabled at own Risk)");
                        ChunkPregenerator.LOGGER.info("Average Free RAM [" + this.memoryAverage.getAverage() + " MB] is below the suggested amount [" + ConfigManager.restartMemory + " MB]");
                        ChunkPregenerator.LOGGER.info("Risk of world corruption because of low RAM");
                        ChunkPregenerator.LOGGER.info("Forcing Restart of the Game now to ensure enough RAM");
                        ChunkPregenerator.LOGGER.info("Pregenerator Progress is saved!");
                        ChunkPregenerator.LOGGER.info("All Worlds are saved");
                        ChunkPregenerator.LOGGER.info("Enforing Shutdown!");
                        FMLCommonHandler.instance().exitJava(0, true);
                    }
                }
            }
            catch (Exception e3) {
                e3.printStackTrace();
                throw new RuntimeException(e3);
            }
        }
    }
    
    public void skipChunks(final int amount) {
        if (this.currentTask == null) {
            return;
        }
        int current;
        for (current = 0; current < amount && this.currentTask.hasWork(); ++current) {
            this.currentTask.tick();
            ++this.processed;
            ++this.skipped;
        }
        this.sendChatMessage(current + " Chunks were Skipped!");
    }
    
    public void startTask(final PregenTask task) {
        this.task = task;
        this.mode = task.startTask(this.getServer());
        this.future = task.createTask(this.progress);
        this.startTime = System.currentTimeMillis();
        System.gc();
        this.threadID.incrementAndGet();
        ConfigManager.saveStart(true);
        final Thread thread = new Thread(new ChunkThread(this), "Chunk Processor Thread");
        thread.setDaemon(true);
        thread.start();
    }
    
    public void interruptTask(final boolean notify) {
        this.interruptTask(notify, true);
    }
    
    public void interruptTask(final boolean notify, final boolean clearListeners) {
        this.interruptTask(notify, clearListeners, true);
    }
    
    public void interruptTask(final boolean notify, final boolean clearListeners, final boolean cleanup) {
        if (this.mode == 0) {
            return;
        }
        this.mode = 0;
        this.cleanup(cleanup);
        if (notify) {
            this.sendChatMessage("Interrupted Current Pregeneration Task");
        }
        if (clearListeners) {
            GlobalListeners.INSTANCE.clearListeners();
        }
        if (BenchmarkManager.INSTANCE.isBenchmarkRunning()) {
            BenchmarkManager.INSTANCE.interruptBenchmark();
        }
    }
    
    private void onFinished() {
        final World world = this.currentTask.world;
        final long processed = this.processed - this.skipped;
        final long startTime = this.startTime;
        PregenTask task = this.currentTask.getTask();
        this.sendChatMessage("Pregenerated: " + processed + " Chunks in " + this.formatIntoTime(this.getWorkTime()) + ", " + this.skipped + " Chunks Skipped, " + this.failed + " Failed");
        final TaskStorage storage = TaskStorage.getStorage();
        storage.finishTask(task);
        this.cleanup(true);
        if (task.isBenchmarkTask()) {
            BenchmarkManager.INSTANCE.addBenchmarkResult(task.getTaskSize(), processed, System.currentTimeMillis() - startTime, !task.isLargeTask(), world);
        }
        if (storage.hasTasks()) {
            task = storage.getNextTask();
            this.sendChatMessage("Starting next task: " + task.toString());
            this.startTask(task);
        }
        else {
            if (BenchmarkManager.INSTANCE.isBenchmarkRunning()) {
                BenchmarkManager.INSTANCE.onBenchmarkFinished();
            }
            GlobalListeners.INSTANCE.clearListeners();
        }
    }
    
    private void cleanup(final boolean cleanup) {
        if (this.currentTask != null) {
            this.currentTask.onRemove();
        }
        if (this.future != null) {
            this.future.cancel(false);
            this.future = null;
        }
        this.currentTask = null;
        this.mode = 0;
        this.failed = 0;
        this.processed = 0;
        this.skipped = 0;
        this.startTime = -1L;
        this.chunkTimer.cleanUp();
        this.counter.reset();
        this.timer.reset();
        if (cleanup) {
            try {
                for (final WorldServer world : this.getServer().field_71305_c) {
                    if (world != null) {
                        final boolean flag = world.field_73058_d;
                        world.field_73058_d = false;
                        world.func_73044_a(true, (IProgressUpdate)null);
                        world.field_73058_d = flag;
                    }
                }
                ThreadedFileIOBase.func_178779_a().func_75734_a();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            ConfigManager.saveStart(false);
        }
        System.gc();
    }
    
    public void buildPreText(final FilePos pos) {
        if (this.log.isEmpty()) {
            return;
        }
        final StringBuilder builder = new StringBuilder();
        for (final ChunkLogger log : this.log) {
            log.addPreLog(builder, this, pos);
            builder.append(" ");
        }
        this.sendChatMessage(builder.toString());
    }
    
    public void sendChatMessage(final String text) {
        GlobalListeners.INSTANCE.sendChatMessage(text);
    }
    
    public void setMaxTime(final int time) {
        this.maxTimePerTick = time;
        ConfigManager.msPerTick = time;
    }
    
    public int getMaxTime() {
        return this.maxTimePerTick;
    }
    
    public boolean isWorking() {
        return this.working;
    }
    
    public PregenTask getTask() {
        return (this.currentTask != null) ? this.currentTask.getTask() : null;
    }
    
    public ChunkProcess getCurrentTask() {
        return this.currentTask;
    }
    
    public boolean shouldDisable() {
        return ConfigManager.playerDeactivation != -1 && this.getServer().func_71233_x() >= ConfigManager.playerDeactivation;
    }
    
    public boolean isPriority() {
        return this.priority;
    }
    
    public void setPriority(final boolean priority) {
        this.priority = priority;
    }
    
    public FileCounter getCounter() {
        return this.counter;
    }
    
    public boolean isStopped() {
        return this.mode == 0;
    }
    
    public boolean isRunning() {
        return this.mode != 0;
    }
    
    public boolean isProcessing() {
        return this.mode == 1;
    }
    
    public int getLoadedChunks() {
        return (this.currentTask != null) ? this.currentTask.getProvider().func_73152_e() : 0;
    }
    
    public MinecraftServer getServer() {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }
    
    long getRamUsage() {
        final long l = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        return l / 1024L / 1024L;
    }
    
    int freeMemory() {
        final long max = Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory();
        final long l = (Runtime.getRuntime().freeMemory() + max) / 1024L / 1024L;
        return (int)l;
    }
    
    public String getRunningTime() {
        return (this.currentTask != null) ? this.formatIntoTime(this.getWorkTime()) : "Not Running";
    }
    
    String formatIntoTime(long time) {
        time /= 1000L;
        final int sec = (int)time % 60;
        time /= 60L;
        final int min = (int)time % 60;
        time /= 60L;
        final int hour = (int)time % 24;
        time /= 24L;
        return String.format("%02d:%02d:%02d:%02d", time, hour, min, sec);
    }
    
    public long getWorkTime() {
        return System.currentTimeMillis() - this.startTime;
    }
    
    public EnumSet<ChunkLogger> getLoggerInfo() {
        return this.log;
    }
    
    public int getCurrentProcessed() {
        return this.processed;
    }
    
    public int getMaxProcess() {
        return (this.currentTask != null) ? this.currentTask.getTotalWorkList() : 0;
    }
    
    public int getAverageCPUTime() {
        return this.timer.hasValues() ? ((int)this.timer.getAverageDelta()) : 0;
    }
    
    public int averageLagPerChunk() {
        return this.chunkTimer.hasValues() ? ((int)this.chunkTimer.getAverage()) : 0;
    }
    
    static {
        format = new DecimalFormat("#.#");
        ChunkProcessor.INSTANCE = new ChunkProcessor();
    }
}
