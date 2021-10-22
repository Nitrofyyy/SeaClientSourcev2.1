// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.utils;

import java.util.ArrayList;
import java.util.List;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class GCSubCommand extends BasePregenCommand
{
    public GCSubCommand() {
        super(0);
    }
    
    @Override
    public String getName() {
        return "gc";
    }
    
    @Override
    public String getDescription() {
        return "Calls Java GC to clear up Memory";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 0;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        System.gc();
        container.sendChatMessage("Cleaned up MCs Memory Usage");
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        return new ArrayList<String>();
    }
}
