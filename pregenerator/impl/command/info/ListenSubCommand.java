// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.info;

import java.util.ArrayList;
import java.util.List;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class ListenSubCommand extends BasePregenCommand
{
    public ListenSubCommand() {
        super(0);
        this.addSuggestion("listen", "Adds the Player to the Running Pregenerator/Deleter Logger");
    }
    
    @Override
    public String getName() {
        return "listen";
    }
    
    @Override
    public String getDescription() {
        return "Adds the Sender to Pregenerator/Deleter info";
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
        if (container.getListener().addListener(container.getSender())) {
            container.sendChatMessage("You are now Listening to the Current Processor");
        }
        else {
            container.sendChatMessage("Already Listening to the Processor");
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        return new ArrayList<String>();
    }
}
