// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.info;

import java.util.ArrayList;
import java.util.List;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class UnlistenSubCommand extends BasePregenCommand
{
    public UnlistenSubCommand() {
        super(0);
        this.addSuggestion("unlisten", "Removes the Player from the Running Pregenerator/Deleter Logger");
    }
    
    @Override
    public String getName() {
        return "unlisten";
    }
    
    @Override
    public String getDescription() {
        return "Removes the Sender from Pregenerator/Deleter info";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 0;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (!container.processorRunning()) {
            container.sendChatMessage("Nothing is running");
            return;
        }
        if (container.getListener().removeListener(container.getSender())) {
            container.sendChatMessage("Stopped Listening the Processor");
        }
        else {
            container.sendChatMessage("Already no longer listening the Current Processor");
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        return new ArrayList<String>();
    }
}
