// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.info;

import java.util.ArrayList;
import pregenerator.impl.command.base.PregenCommand;
import java.util.List;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class AutoListenSubCommand extends BasePregenCommand
{
    public AutoListenSubCommand() {
        super(1);
        this.addDescription(0, "(Optional) State: The state the sender is setting it to");
        this.addSuggestion("setAutoListenState", "Prints out the senders Auto List");
        this.addSuggestion("setAutoListenState reset", "Resets the Listener State of the Sender");
        this.addSuggestion("setAutoListenState ignoring", "Sets the Sender to ignore any starting Processess infos. Even the senders ones");
        this.addSuggestion("setAutoListenState listening", "Sets the Sender to Automaticallly Listening to any Process info that apears");
    }
    
    @Override
    public String getName() {
        return "setAutoListenState";
    }
    
    @Override
    public String getDescription() {
        return "Reads or Sets the AutoListen state that is requested";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 0;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (args.length >= 1) {
            final int state = this.getState(args[0]);
            if (state == 0) {
                container.getStorage().removeSender(container.getSender());
                container.sendChatMessage("Reseted the ListenState");
            }
            else {
                container.getStorage().addListenState(container.getSender(), state == 1);
                container.sendChatMessage("Set Auto Listen State: " + this.getListenState(state));
            }
        }
        else {
            container.sendChatMessage("Your Auto Listen State: " + this.getListenState(container.getStorage().getState(container.getSender())));
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0) {
            return PregenCommand.getBestMatch(args, "reset", "listening", "ignoring");
        }
        return new ArrayList<String>();
    }
    
    public int getState(final String data) {
        if (data.equalsIgnoreCase("reset")) {
            return 0;
        }
        if (data.equalsIgnoreCase("listening")) {
            return 1;
        }
        if (data.equalsIgnoreCase("ignoring")) {
            return 2;
        }
        return 0;
    }
    
    public String getListenState(final int state) {
        if (state == 0) {
            return "No Auto State";
        }
        if (state == 1) {
            return "Auto Listening";
        }
        if (state == 2) {
            return "Auto Ignoring";
        }
        return "";
    }
}
