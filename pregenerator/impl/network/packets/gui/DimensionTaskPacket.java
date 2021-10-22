// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.network.packets.gui;

import java.io.File;
import net.minecraft.world.chunk.storage.RegionFileCache;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraftforge.common.DimensionManager;
import net.minecraft.command.ICommandSender;
import pregenerator.impl.command.base.BasePregenCommand;
import net.minecraft.server.MinecraftServer;
import pregenerator.ChunkPregenerator;
import net.minecraft.entity.player.EntityPlayer;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.PregenPacket;

public class DimensionTaskPacket extends PregenPacket
{
    boolean unload;
    int dimension;
    
    public DimensionTaskPacket() {
    }
    
    public DimensionTaskPacket(final boolean unload, final int dim) {
        this.unload = unload;
        this.dimension = dim;
    }
    
    @Override
    public void read(final IReadableBuffer buffer) {
        this.unload = buffer.readBoolean();
        this.dimension = buffer.readInt();
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        buffer.writeBoolean(this.unload);
        buffer.writeInt(this.dimension);
    }
    
    @Override
    public void handle(final EntityPlayer player) {
        final MinecraftServer server = ChunkPregenerator.getServer();
        if (!server.func_152345_ab()) {
            server.func_152344_a((Runnable)new Runnable() {
                @Override
                public void run() {
                    DimensionTaskPacket.this.handleServer(player);
                }
            });
            return;
        }
        this.handleServer(player);
    }
    
    public void handleServer(final EntityPlayer player) {
        if (this.unload) {
            if (!BasePregenCommand.isDimensionValid(this.dimension)) {
                ChunkPregenerator.pregenBase.sendChatMessage(player, "Dimension " + this.dimension + " is not Registered!");
                return;
            }
            DimensionManager.unloadWorld(this.dimension);
            ChunkPregenerator.pregenBase.sendChatMessage(player, "Unloaded Dimension " + this.dimension);
        }
        else {
            if (!BasePregenCommand.isDimensionValid(this.dimension)) {
                ChunkPregenerator.pregenBase.sendChatMessage(player, "Dimension " + this.dimension + " is not Registered!");
                return;
            }
            if (DimensionManager.getWorld(this.dimension) != null) {
                ChunkPregenerator.pregenBase.sendChatMessage(player, "Dimension " + this.dimension + " is loaded. It needs to be unloaded");
                return;
            }
            try {
                ThreadedFileIOBase.func_178779_a().func_75734_a();
                RegionFileCache.func_76551_a();
            }
            catch (Exception ex) {}
            if (this.dimension == 0) {
                final File file = new File(DimensionManager.getWorld(0).func_72860_G().func_75765_b(), "region");
                if (!file.exists()) {
                    ChunkPregenerator.pregenBase.sendChatMessage(player, "Overworld Doesnt exists WTF are you doing!");
                    return;
                }
                try {
                    if (!this.deleteRecursively(file, true)) {
                        ChunkPregenerator.pregenBase.sendChatMessage(player, "Deleting Overworld failed");
                        return;
                    }
                    this.deleteRecursively(new File(file.getParentFile(), "data"), true);
                    ChunkPregenerator.pregenBase.sendChatMessage(player, "Deleting Overworld finished");
                }
                catch (Exception e) {
                    ChunkPregenerator.pregenBase.sendChatMessage(player, "Error on Deletion: " + e.getMessage());
                }
            }
            else {
                final File file = new File(DimensionManager.getWorld(0).func_72860_G().func_75765_b(), "DIM" + this.dimension);
                if (!file.exists()) {
                    ChunkPregenerator.pregenBase.sendChatMessage(player, "Dimension " + this.dimension + " is already deleted!");
                    return;
                }
                try {
                    if (!this.deleteRecursively(file, false)) {
                        ChunkPregenerator.pregenBase.sendChatMessage(player, "Deleting Dimension " + this.dimension + " Failed");
                        return;
                    }
                    ChunkPregenerator.pregenBase.sendChatMessage(player, "Dimension " + this.dimension + " Successfully Deleted");
                }
                catch (Exception e) {
                    ChunkPregenerator.pregenBase.sendChatMessage(player, "Error on Deletion: " + e.getMessage());
                }
            }
        }
    }
    
    public boolean deleteRecursively(final File file, final boolean onlyFolder) {
        if (file.isDirectory()) {
            final File[] files = file.listFiles();
            if (files != null) {
                for (final File entry : files) {
                    if (!this.deleteRecursively(entry, false)) {
                        return false;
                    }
                }
            }
        }
        return onlyFolder || file.delete();
    }
}
