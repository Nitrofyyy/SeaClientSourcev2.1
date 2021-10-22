// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.utils;

import java.util.ArrayList;
import java.util.List;
import pregenerator.impl.tracking.ServerTracker;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class DisableTrackingSubCommand extends BasePregenCommand
{
    public DisableTrackingSubCommand() {
        super(0);
        this.addSuggestion("disableTracking", "Disables the ServerProfiler");
    }
    
    @Override
    public String getName() {
        return "disableTracking";
    }
    
    @Override
    public String getDescription() {
        return "Disables the ServerTracker";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 0;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (!ServerTracker.INSTANCE.isEnabled()) {
            container.sendChatMessage("ServerTracker is already disabled!");
            return;
        }
        ServerTracker.INSTANCE.toggle();
        container.sendChatMessage("Server Tracker is no disabled");
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        return new ArrayList<String>();
    }
}
