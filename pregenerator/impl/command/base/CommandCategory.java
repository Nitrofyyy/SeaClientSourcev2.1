// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.base;

import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map;

public class CommandCategory
{
    Map<String, PregenCommand> commandMap;
    Set<String> names;
    String categoryName;
    String categoryDescription;
    
    public CommandCategory(final String name, final String description) {
        this.commandMap = new LinkedHashMap<String, PregenCommand>();
        this.names = new LinkedHashSet<String>();
        this.categoryName = name;
        this.categoryDescription = description;
    }
    
    public void addSubCommand(final PregenCommand command) {
        this.commandMap.put(command.getName().toLowerCase(), command);
        this.names.add(command.getName());
    }
    
    public PregenCommand getSubCommand(final String id) {
        return this.commandMap.get(id.toLowerCase());
    }
    
    public Set<String> getSubCommandNames() {
        return this.names;
    }
    
    public Map<String, PregenCommand> getSubCommands() {
        return this.commandMap;
    }
    
    public String getName() {
        return this.categoryName;
    }
    
    public String getDescription() {
        return this.categoryDescription;
    }
}
