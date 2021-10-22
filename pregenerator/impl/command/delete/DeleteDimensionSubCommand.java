// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.delete;

import java.util.ArrayList;
import pregenerator.impl.command.base.PregenCommand;
import pregenerator.impl.command.CompleterHelper;
import java.util.List;
import java.io.File;
import net.minecraft.world.chunk.storage.RegionFileCache;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraftforge.common.DimensionManager;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class DeleteDimensionSubCommand extends BasePregenCommand
{
    public DeleteDimensionSubCommand() {
        super(1);
        this.addDescription(0, "Dimension: The Dimension that should be deleted");
        this.addSuggestion("deleteDimension 1", "Deletes the End");
        this.addSuggestion("deleteDimension -1", "Deletes the Nether");
    }
    
    @Override
    public String getName() {
        return "deleteDimension";
    }
    
    @Override
    public String getDescription() {
        return "Deletes an Entire Unloaded Dimension";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 1;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (container.processorRunning()) {
            container.sendChatMessage("Processor is running. No dimension deletion until thats done!");
            return;
        }
        if (args.length >= 1) {
            final int dimension = BasePregenCommand.getDimension(container, args[0]);
            if (!BasePregenCommand.isDimensionValid(dimension)) {
                container.sendChatMessage("Dimension " + dimension + " is not Registered!");
                return;
            }
            if (DimensionManager.getWorld(dimension) != null) {
                container.sendChatMessage("Dimension " + dimension + " is loaded. It needs to be unloaded");
                return;
            }
            try {
                ThreadedFileIOBase.func_178779_a().func_75734_a();
                RegionFileCache.func_76551_a();
            }
            catch (Exception ex) {}
            if (dimension == 0) {
                final File file = new File(DimensionManager.getWorld(0).func_72860_G().func_75765_b(), "region");
                if (!file.exists()) {
                    container.sendChatMessage("Overworld Doesnt exists WTF are you doing!");
                    return;
                }
                try {
                    if (!this.deleteRecursively(file, true)) {
                        container.sendChatMessage("Deleting Overworld failed");
                        return;
                    }
                    this.deleteRecursively(new File(file.getParentFile(), "data"), true);
                    container.sendChatMessage("Deleting Overworld finished");
                }
                catch (Exception e) {
                    container.sendChatMessage("Error on Deletion: " + e.getMessage());
                }
            }
            else {
                final File file = new File(DimensionManager.getWorld(0).func_72860_G().func_75765_b(), "DIM" + dimension);
                if (!file.exists()) {
                    container.sendChatMessage("Dimension " + dimension + " is already deleted!");
                    return;
                }
                try {
                    if (!this.deleteRecursively(file, false)) {
                        container.sendChatMessage("Deleting Dimension " + dimension + " Failed");
                        return;
                    }
                    container.sendChatMessage("Dimension " + dimension + " Successfully Deleted");
                }
                catch (Exception e) {
                    container.sendChatMessage("Error on Deletion: " + e.getMessage());
                }
            }
        }
        else {
            this.throwErrors(container, args.length);
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0) {
            final CompleterHelper helper = DeleteDimensionSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.DIMENSION);
        }
        return new ArrayList<String>();
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
