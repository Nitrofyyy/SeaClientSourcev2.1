// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.utils;

import java.util.ArrayList;
import pregenerator.impl.command.base.PregenCommand;
import pregenerator.impl.command.CompleterHelper;
import java.util.List;
import net.minecraftforge.common.DimensionManager;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class UnloadDimensionSubCommand extends BasePregenCommand
{
    public UnloadDimensionSubCommand() {
        super(1);
        this.addDescription(0, "Dimension: The Dimension that should be unloaded");
        this.addSuggestion("unloadDimension -1", "Unloads the Nether");
        this.addSuggestion("unloadDimension 1", "Unloads the End");
    }
    
    @Override
    public String getName() {
        return "unloadDimension";
    }
    
    @Override
    public String getDescription() {
        return "Unloads a Dimension";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 1;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (args.length >= 1) {
            final int dimension = BasePregenCommand.parseNumber(args[0], 0);
            if (dimension == 0) {
                container.sendChatMessage("Overworld can not be Unloaded");
                return;
            }
            if (!BasePregenCommand.isDimensionValid(dimension)) {
                container.sendChatMessage("Dimension " + dimension + " is not Registered!");
                return;
            }
            DimensionManager.unloadWorld(dimension);
            container.sendChatMessage("Unloaded Dimension " + dimension);
        }
        else {
            this.throwErrors(container, args.length);
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0) {
            final CompleterHelper helper = UnloadDimensionSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.DIMENSION);
        }
        return new ArrayList<String>();
    }
}
