// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.info;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import pregenerator.impl.storage.TaskStorage;
import pregenerator.impl.storage.PregenTask;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class ShowTaskListSubCommand extends BasePregenCommand
{
    public ShowTaskListSubCommand() {
        super(0);
        this.addSuggestion("ShowTaskList", "Shows all the Tasks that are stored");
    }
    
    @Override
    public String getName() {
        return "ShowTaskList";
    }
    
    @Override
    public String getDescription() {
        return "Shows all the Tasks of the Pregenerator";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 0;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        final TaskStorage storage = container.getStorage();
        if (storage.hasTasks()) {
            container.sendChatMessage("Pregenerator TaskList: " + storage.getTaskCount() + " Tasks");
            int i = 1;
            for (final PregenTask task : container.getStorage().getTasks()) {
                container.sendChatMessage("[" + i + "] " + task.toString());
                ++i;
            }
            container.sendChatMessage("End of TaskList");
        }
        else {
            container.sendChatMessage("Pregenerator TaskList: Empty");
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        return new ArrayList<String>();
    }
}
