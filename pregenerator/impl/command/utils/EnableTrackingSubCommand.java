// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.utils;

import java.util.ArrayList;
import java.util.List;
import pregenerator.impl.tracking.ServerTracker;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class EnableTrackingSubCommand extends BasePregenCommand
{
    public EnableTrackingSubCommand() {
        super(0);
        this.addSuggestion("enableTracking", "Enables the ServerProfiler that can be seen via the UI");
    }
    
    @Override
    public String getName() {
        return "enableTracking";
    }
    
    @Override
    public String getDescription() {
        return "Enables the Pregenerators Profiler that shows a bit of more detailed info. (Requires a client to have the mod installed)";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 0;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (ServerTracker.INSTANCE.isEnabled()) {
            container.sendChatMessage("Server Profiler is already enabled!");
            return;
        }
        ServerTracker.INSTANCE.toggle();
        container.sendChatMessage("Enabled Server Tracker. Make sure the UI is activated!");
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        return new ArrayList<String>();
    }
}
