// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.processor.deleter;

import pregenerator.impl.storage.GlobalListeners;
import java.util.Collection;
import java.util.List;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pregenerator.impl.processor.generator.ChunkProcessor;
import net.minecraft.world.chunk.storage.RegionFileCache;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import pregenerator.impl.misc.DeltaTimer;
import java.util.concurrent.Future;
import pregenerator.impl.processor.PrepaireProgress;
import java.util.LinkedList;
import java.text.DecimalFormat;

public class DeleteProcessor
{
    public static final DecimalFormat format;
    public static DeleteProcessor INSTANCE;
    LinkedList<IDeletionTask> taskList;
    PrepaireProgress progress;
    IDeletionTask task;
    Future<DeleteProcess> future;
    DeleteProcess process;
    DeltaTimer timer;
    long start;
    int state;
    int deleted;
    int failed;
    int ticker;
    
    public DeleteProcessor() {
        this.taskList = new LinkedList<IDeletionTask>();
        this.progress = new PrepaireProgress();
        this.timer = new DeltaTimer();
        this.state = 0;
        this.deleted = 0;
        this.failed = 0;
        this.ticker = 0;
    }
    
    @SubscribeEvent
    public void onServerTickEvent(final TickEvent.ServerTickEvent event) {
        if (this.task == null || this.isStopped()) {
            return;
        }
        if (this.future != null) {
            final int newTime = (int)((System.currentTimeMillis() - this.start) / 1000L);
            if (this.ticker != newTime) {
                this.ticker = newTime;
                this.sendChatMessage("Prepaire Progress: " + this.progress.getCurrent() + " / " + this.progress.getMax());
            }
            if (this.future.isDone()) {
                try {
                    this.process = this.future.get();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                this.sendChatMessage("Starting Task with " + this.process.getTotalWork() + " Chunks to delete");
                this.future = null;
            }
            return;
        }
        if (event.phase == TickEvent.Phase.START) {
            this.timer.start();
        }
        else {
            try {
                ThreadedFileIOBase.func_178779_a().func_75734_a();
                RegionFileCache.func_76551_a();
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
            final int max = ChunkProcessor.INSTANCE.getMaxTime();
            final long deltaTime = this.timer.averageDelta();
            if (deltaTime >= max) {
                return;
            }
            while (deltaTime + this.timer.getDeltaTime() < max && this.process.hasWork()) {
                final DeleteProcess.DeleteFile file = this.process.getEntry();
                file.update();
                this.deleted += file.getCount();
                this.sendChatMessage(this.deleted + "/" + this.process.getTotalWork() + " Chunks Deleted, Ram: " + DeleteProcessor.format.format(this.getRamUsage()) + "MB");
            }
            if (!this.process.hasWork()) {
                this.onFinished();
                this.timer.reset();
            }
            else {
                this.timer.finishDeltaTime();
            }
        }
    }
    
    long getRamUsage() {
        final long l = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        return l / 1024L / 1024L;
    }
    
    public void startTasks(final List<IDeletionTask> tasks) {
        if (this.startTask(tasks.remove(0))) {
            this.sendChatMessage("Stored the rest of the Task into the TaskList");
        }
        this.taskList.addAll(tasks);
    }
    
    public boolean startTask(final IDeletionTask task) {
        if (this.state == 0) {
            this.state = 1;
            this.start = System.currentTimeMillis();
            this.future = task.createTask(this.progress);
            return true;
        }
        this.taskList.add(task);
        this.sendChatMessage("Stored the Task into the TaskList");
        return false;
    }
    
    public void interruptTask() {
        this.state = 0;
        this.process = null;
        this.cleanup();
        GlobalListeners.INSTANCE.clearListeners();
        this.taskList.clear();
    }
    
    public void onFinished() {
        this.sendChatMessage("Processed " + this.deleted + " Chunks. " + this.failed + " Chunks couldn't be deleted because they were loaded in " + this.formatIntoTime(this.getWorkTime()));
        this.state = 0;
        this.process = null;
        this.cleanup();
        if (this.taskList.size() > 0) {
            this.startTask(this.taskList.removeFirst());
        }
        else {
            GlobalListeners.INSTANCE.clearListeners();
        }
    }
    
    private void cleanup() {
        System.gc();
        this.deleted = 0;
        this.failed = 0;
    }
    
    public void sendChatMessage(final String s) {
        GlobalListeners.INSTANCE.sendChatMessage(s);
    }
    
    public boolean isStopped() {
        return this.state == 0;
    }
    
    public boolean isRunning() {
        return this.state == 1;
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
    
    private long getWorkTime() {
        return System.currentTimeMillis() - this.start;
    }
    
    static {
        format = new DecimalFormat("#.#");
        DeleteProcessor.INSTANCE = new DeleteProcessor();
    }
}
