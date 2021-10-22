// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.info;

import java.util.ArrayList;
import java.util.List;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class ShowRunTimeSubCommand extends BasePregenCommand
{
    public ShowRunTimeSubCommand() {
        super(0);
        this.addSuggestion("ShowRunningTime", "Shows how long the Pregenerator is running");
    }
    
    @Override
    public String getName() {
        return "ShowRunningTime";
    }
    
    @Override
    public String getDescription() {
        return "Shows how long the Pregenerator is Running";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 0;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (container.getProcessor().isProcessing()) {
            container.sendChatMessage("Running time: " + container.getProcessor().getRunningTime());
        }
        else {
            container.sendChatMessage("Pregenerator is not running");
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        return new ArrayList<String>();
    }
}
