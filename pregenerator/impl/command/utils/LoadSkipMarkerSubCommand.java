// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.utils;

import java.util.ArrayList;
import java.util.List;
import pregenerator.ConfigManager;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class LoadSkipMarkerSubCommand extends BasePregenCommand
{
    public LoadSkipMarkerSubCommand() {
        super(0);
        this.addSuggestion("LoadSkipMarker", "Loads a Skip Marker and Applies it to the Current Pregeneration");
    }
    
    @Override
    public String getName() {
        return "LoadSkipMarker";
    }
    
    @Override
    public String getDescription() {
        return "Loads the SkipMarker and Applies it to the current Task. Skip Marker gets resets";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 0;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (container.getProcessor().isStopped()) {
            container.sendChatMessage("Pregenerator needs to run to apply a SkipMarker");
            return;
        }
        final int skipMarker = ConfigManager.skippingAmount;
        if (skipMarker == -1) {
            container.sendChatMessage("No Skip Marker found!");
            return;
        }
        ConfigManager.setSkipMarker(-1);
        final boolean has = container.getListener().addListener(container.getSender());
        container.getProcessor().skipChunks(skipMarker);
        if (has) {
            container.getListener().removeListener(container.getSender());
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        return new ArrayList<String>();
    }
}
