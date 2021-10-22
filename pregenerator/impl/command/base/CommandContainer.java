// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.base;

import pregenerator.impl.processor.generator.BenchmarkManager;
import pregenerator.impl.storage.PregenTask;
import pregenerator.ChunkPregenerator;
import pregenerator.impl.storage.TaskStorage;
import net.minecraft.world.WorldServer;
import net.minecraft.util.BlockPos;
import pregenerator.impl.misc.FilePos;
import pregenerator.impl.storage.GlobalListeners;
import pregenerator.impl.processor.deleter.DeleteProcessor;
import pregenerator.impl.processor.generator.ChunkProcessor;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandContainer
{
    MinecraftServer server;
    ICommandSender sender;
    ChunkProcessor processor;
    DeleteProcessor delete;
    GlobalListeners listener;
    
    public CommandContainer(final MinecraftServer mc, final ICommandSender com) {
        this.server = mc;
        this.sender = com;
        this.processor = ChunkProcessor.INSTANCE;
        this.delete = DeleteProcessor.INSTANCE;
        this.listener = GlobalListeners.INSTANCE;
    }
    
    public ICommandSender getSender() {
        return this.sender;
    }
    
    public FilePos getPlayerPos() {
        final BlockPos pos = this.sender.func_180425_c();
        return new FilePos(pos.func_177958_n(), pos.func_177952_p());
    }
    
    public FilePos getWorldSpawn(final int dim) {
        if (BasePregenCommand.isDimensionValid(dim)) {
            final BlockPos pos = this.getWorld(dim).func_175694_M();
            return new FilePos(pos.func_177958_n(), pos.func_177952_p()).toChunkPos();
        }
        return new FilePos(0, 0);
    }
    
    public int getPlayerDimension() {
        return this.sender.func_130014_f_().field_73011_w.func_177502_q();
    }
    
    public MinecraftServer getServer() {
        return this.server;
    }
    
    public WorldServer getWorld(final int dim) {
        return this.server.func_71218_a(dim);
    }
    
    public TaskStorage getStorage() {
        return TaskStorage.getFromServer(this.server);
    }
    
    public ChunkProcessor getProcessor() {
        return this.processor;
    }
    
    public DeleteProcessor getDeleter() {
        return this.delete;
    }
    
    public GlobalListeners getListener() {
        return this.listener;
    }
    
    public boolean shouldLog() {
        return ChunkPregenerator.proxy.shouldLog() && this.getStorage().autoListens(this.sender);
    }
    
    public void onStarted() {
        if (!this.getStorage().autoListens(this.server)) {
            return;
        }
        this.getStorage().addListeners(this.listener, this.server);
    }
    
    public boolean onProcessStarted(final PregenTask task) {
        if (BenchmarkManager.INSTANCE.isBenchmarkRunning()) {
            this.sendChatMessage("Benchmark is running, No new Tasks are allowed to be added, Task is not saved in Task Storage");
            return true;
        }
        final TaskStorage storage = this.getStorage();
        if (storage.autoListens(this.server)) {
            storage.addListeners(this.listener, this.server);
        }
        if (storage.autoListens(this.sender) && ChunkPregenerator.proxy.shouldLog()) {
            this.listener.addListener(this.sender);
        }
        storage.savePregenTask(task);
        return this.processor.isRunning();
    }
    
    public void onProcessStarted() {
        final TaskStorage storage = this.getStorage();
        if (storage.autoListens(this.server)) {
            storage.addListeners(this.listener, this.server);
        }
        if (storage.autoListens(this.sender)) {
            this.listener.addListener(this.sender);
        }
    }
    
    public boolean processorRunning() {
        return this.processor.isRunning() || this.delete.isRunning();
    }
    
    public void sendChatMessage(final String text) {
        ChunkPregenerator.pregenBase.sendChatMessage(this.sender, text);
    }
    
    public static class PerWorldContainer extends CommandContainer
    {
        int dim;
        
        public PerWorldContainer(final MinecraftServer mc, final ICommandSender com, final int dim) {
            super(mc, com);
            this.dim = dim;
        }
        
        @Override
        public int getPlayerDimension() {
            return this.dim;
        }
    }
}
