// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.utils;

import java.util.ArrayList;
import java.util.List;
import pregenerator.ConfigManager;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class SetSkipMarkerSubCommand extends BasePregenCommand
{
    public SetSkipMarkerSubCommand() {
        super(0);
        this.addSuggestion("SetSkipMarker", "Sets a Marker that allows you to skip a certain amount of progress");
    }
    
    @Override
    public String getName() {
        return "SetSkipMarker";
    }
    
    @Override
    public String getDescription() {
        return "Sets a Marker for Skipping already Generated Chunks";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 0;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (container.getProcessor().isStopped()) {
            container.sendChatMessage("Processor needs to run to set a Skip Marker");
            return;
        }
        final int amount = container.getProcessor().getCurrentProcessed();
        ConfigManager.setSkipMarker(amount);
        container.sendChatMessage("Set Skip Marker to " + amount + " Chunks");
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        return new ArrayList<String>();
    }
}
