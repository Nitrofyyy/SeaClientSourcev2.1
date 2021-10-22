// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.utils;

import pregenerator.impl.command.base.PregenCommand;
import pregenerator.impl.command.CompleterHelper;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import net.minecraftforge.common.DimensionManager;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class UnloadDimensionRangeSubCommand extends BasePregenCommand
{
    public UnloadDimensionRangeSubCommand() {
        super(2);
        this.addDescription(0, "Min Range: The Lowest Value of Dimension that should be unloaded");
        this.addDescription(1, "Max Value: The Highest Value of Dimension that should be unloaded");
        this.addSuggestion("unloadDimensionRange -1 1", "Unloads the End & The Nether");
        this.addSuggestion("unloadDimensionRange -1100 -1000", "Unloads all the GalacticCraft Dimensions");
    }
    
    @Override
    public String getName() {
        return "unloadDimensionRange";
    }
    
    @Override
    public String getDescription() {
        return "Unloads a Range of Loaded-Dimensions";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 2;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (args.length >= 2) {
            final int first = BasePregenCommand.parseNumber(args[0], 0);
            final int second = BasePregenCommand.parseNumber(args[1], 0);
            if (first == second) {
                container.sendChatMessage("Error First & Second Numbers can not be the same");
                return;
            }
            final int min = Math.min(first, second);
            final int max = Math.max(first, second);
            final Set<Integer> allDims = new HashSet<Integer>(Arrays.asList(DimensionManager.getIDs()));
            allDims.remove(0);
            final List<Integer> unloadedDims = new ArrayList<Integer>();
            for (int i = min; i <= max; ++i) {
                if (allDims.contains(i)) {
                    DimensionManager.unloadWorld(i);
                    unloadedDims.add(i);
                }
            }
            container.sendChatMessage("Unloaded Dimensions: " + unloadedDims);
        }
        else {
            this.throwErrors(container, args.length);
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0 || commandIndex == 1) {
            final CompleterHelper helper = UnloadDimensionRangeSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.DIMENSION);
        }
        return new ArrayList<String>();
    }
}
