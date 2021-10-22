// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.structure;

import java.util.ArrayList;
import pregenerator.impl.command.CompleterHelper;
import java.util.List;
import pregenerator.impl.structure.StructureManager;
import pregenerator.impl.command.base.PregenCommand;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class WhipeStructuresSubCommand extends BasePregenCommand
{
    public WhipeStructuresSubCommand() {
        super(1);
        this.addDescription(0, "(Optional) Dimension: Which dimension the Structures should be resetted in");
    }
    
    @Override
    public String getName() {
        return "resetAllStructures";
    }
    
    @Override
    public String getDescription() {
        return "Deletes all the Generated structures";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 0;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        final int dim = BasePregenCommand.getDimension(container, PregenCommand.getArg(args, 0));
        if (!BasePregenCommand.isDimensionValid(dim)) {
            container.sendChatMessage("Dimension [" + dim + "] is not registered");
            return;
        }
        StructureManager.instance.createSaveZone(0, 0, 60000000, "All", dim);
        StructureManager.instance.clearAllZones(dim, "All");
        container.sendChatMessage("Removed All Structures and all SaveZones so they will respawn properly in dim " + dim);
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0) {
            final CompleterHelper helper = WhipeStructuresSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.STRUCTURE_DIMENSION);
        }
        return new ArrayList<String>();
    }
}
