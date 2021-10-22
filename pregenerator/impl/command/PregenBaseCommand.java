// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.command.CommandException;
import pregenerator.impl.command.base.CommandContainer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import java.util.Iterator;
import pregenerator.impl.command.nocat.ClearSubCommand;
import pregenerator.impl.command.nocat.TimePerTickSubCommand;
import pregenerator.impl.command.nocat.StopSubCommand;
import pregenerator.impl.command.nocat.ContinueSubCommand;
import pregenerator.impl.command.nocat.SuggestionsSubCommand;
import pregenerator.impl.command.nocat.HelpSubCommand;
import pregenerator.impl.command.utils.GCSubCommand;
import pregenerator.impl.command.utils.DisableTrackingSubCommand;
import pregenerator.impl.command.utils.EnableTrackingSubCommand;
import pregenerator.impl.command.utils.LoadSkipMarkerSubCommand;
import pregenerator.impl.command.utils.SetSkipMarkerSubCommand;
import pregenerator.impl.command.utils.SkipChunksSubCommand;
import pregenerator.impl.command.utils.FindSpawnSubCommand;
import pregenerator.impl.command.utils.PlayerLimitSubCommand;
import pregenerator.impl.command.utils.UnloadDimensionRangeSubCommand;
import pregenerator.impl.command.utils.UnloadDimensionSubCommand;
import pregenerator.impl.command.utils.SetPrioritySubCommand;
import pregenerator.impl.command.structure.WhipeStructuresSubCommand;
import pregenerator.impl.command.structure.DeleteStructureSubCommand;
import pregenerator.impl.command.structure.ListStructuresSubCommand;
import pregenerator.impl.command.structure.ListSaveZonesSubCommand;
import pregenerator.impl.command.structure.ClearSaveZoneSubCommand;
import pregenerator.impl.command.structure.CreateSaveZoneSubCommand;
import pregenerator.impl.command.info.ShowChunkFileSubCommand;
import pregenerator.impl.command.info.ShowRunTimeSubCommand;
import pregenerator.impl.command.info.ShowTaskListSubCommand;
import pregenerator.impl.command.info.AutoListenSubCommand;
import pregenerator.impl.command.info.EditInfoSubCommand;
import pregenerator.impl.command.info.UnlistenSubCommand;
import pregenerator.impl.command.info.ListenSubCommand;
import pregenerator.impl.command.delete.DeleteMassSubCommand;
import pregenerator.impl.command.delete.DeleteDimensionSubCommand;
import pregenerator.impl.command.delete.DeleteRegionSubCommand;
import pregenerator.impl.command.delete.DeleteExpansionSubCommand;
import pregenerator.impl.command.delete.DeleteRadiusSubCommand;
import pregenerator.impl.command.delete.DeleteChunkSubCommand;
import pregenerator.impl.command.gen.BenchmarkSubCommand;
import pregenerator.impl.command.gen.SelectRetrogenSubCommand;
import pregenerator.impl.command.gen.LoadFromFileSubCommand;
import pregenerator.impl.command.gen.StartWorldBorderSubCommand;
import pregenerator.impl.command.gen.StartRegionSubCommand;
import pregenerator.impl.command.gen.StartAreaSubCommand;
import pregenerator.impl.command.gen.StartMassRadiusSubCommand;
import pregenerator.impl.command.gen.StartExpansionSubCommand;
import pregenerator.impl.command.gen.StartRadiusSubCommand;
import java.util.HashMap;
import java.util.LinkedHashMap;
import pregenerator.impl.command.base.CommandCategory;
import pregenerator.impl.command.base.PregenCommand;
import java.util.Map;
import net.minecraft.command.CommandBase;

public class PregenBaseCommand extends CommandBase
{
    Map<String, PregenCommand> allCommands;
    Map<String, PregenCommand> noCategoryCommands;
    Map<String, CommandCategory> categories;
    Map<String, CommandCategory> commandsToCategory;
    
    public PregenBaseCommand() {
        this.allCommands = new LinkedHashMap<String, PregenCommand>();
        this.noCategoryCommands = new LinkedHashMap<String, PregenCommand>();
        this.categories = new HashMap<String, CommandCategory>();
        this.commandsToCategory = new HashMap<String, CommandCategory>();
        final CommandCategory gen = new CommandCategory("gen", "Category for Generation Commands");
        gen.addSubCommand(new StartRadiusSubCommand());
        gen.addSubCommand(new StartExpansionSubCommand());
        gen.addSubCommand(new StartMassRadiusSubCommand());
        gen.addSubCommand(new StartAreaSubCommand());
        gen.addSubCommand(new StartRegionSubCommand());
        gen.addSubCommand(new StartWorldBorderSubCommand());
        gen.addSubCommand(new LoadFromFileSubCommand());
        gen.addSubCommand(new SelectRetrogenSubCommand());
        gen.addSubCommand(new BenchmarkSubCommand());
        this.addCommandCategory(gen);
        final CommandCategory delete = new CommandCategory("delete", "Category for ChunkDeletion Commands");
        delete.addSubCommand(new DeleteChunkSubCommand());
        delete.addSubCommand(new DeleteRadiusSubCommand());
        delete.addSubCommand(new DeleteExpansionSubCommand());
        delete.addSubCommand(new DeleteRegionSubCommand());
        delete.addSubCommand(new DeleteDimensionSubCommand());
        delete.addSubCommand(new DeleteMassSubCommand());
        this.addCommandCategory(delete);
        final CommandCategory info = new CommandCategory("info", "Category for Info Commands");
        info.addSubCommand(new ListenSubCommand());
        info.addSubCommand(new UnlistenSubCommand());
        info.addSubCommand(new EditInfoSubCommand());
        info.addSubCommand(new AutoListenSubCommand());
        info.addSubCommand(new ShowTaskListSubCommand());
        info.addSubCommand(new ShowRunTimeSubCommand());
        info.addSubCommand(new ShowChunkFileSubCommand());
        this.addCommandCategory(info);
        final CommandCategory structure = new CommandCategory("structure", "Category for Structure Commands");
        structure.addSubCommand(new CreateSaveZoneSubCommand());
        structure.addSubCommand(new ClearSaveZoneSubCommand());
        structure.addSubCommand(new ListSaveZonesSubCommand());
        structure.addSubCommand(new ListStructuresSubCommand());
        structure.addSubCommand(new DeleteStructureSubCommand());
        structure.addSubCommand(new WhipeStructuresSubCommand());
        this.addCommandCategory(structure);
        final CommandCategory utils = new CommandCategory("utils", "Category for utility Commands");
        utils.addSubCommand(new SetPrioritySubCommand());
        utils.addSubCommand(new UnloadDimensionSubCommand());
        utils.addSubCommand(new UnloadDimensionRangeSubCommand());
        utils.addSubCommand(new PlayerLimitSubCommand());
        utils.addSubCommand(new FindSpawnSubCommand());
        utils.addSubCommand(new SkipChunksSubCommand());
        utils.addSubCommand(new SetSkipMarkerSubCommand());
        utils.addSubCommand(new LoadSkipMarkerSubCommand());
        utils.addSubCommand(new EnableTrackingSubCommand());
        utils.addSubCommand(new DisableTrackingSubCommand());
        utils.addSubCommand(new GCSubCommand());
        this.addCommandCategory(utils);
        this.addSubCommand(new HelpSubCommand(this));
        this.addSubCommand(new SuggestionsSubCommand(this));
        this.addSubCommand(new ContinueSubCommand());
        this.addSubCommand(new StopSubCommand());
        this.addSubCommand(new TimePerTickSubCommand());
        this.addSubCommand(new ClearSubCommand());
    }
    
    public void addSubCommand(final PregenCommand command) {
        this.allCommands.put(command.getName().toLowerCase(), command);
        this.noCategoryCommands.put(command.getName().toLowerCase(), command);
    }
    
    public void addCommandCategory(final CommandCategory category) {
        this.categories.put(category.getName().toLowerCase(), category);
        this.allCommands.putAll(category.getSubCommands());
        for (final String key : category.getSubCommandNames()) {
            this.commandsToCategory.put(key, category);
        }
    }
    
    public String func_71517_b() {
        return "pregen";
    }
    
    public String func_71518_a(final ICommandSender sender) {
        return "/pregen Allows you to Pregenerate Worlds, Control Structures, Delete Chunks and other things";
    }
    
    public void func_71515_b(final ICommandSender sender, final String[] args) throws CommandException {
        final CommandContainer container = new CommandContainer(MinecraftServer.func_71276_C(), sender);
        if (args.length >= 1) {
            CommandCategory cat = this.categories.get(args[0].toLowerCase());
            if (cat != null) {
                if (args.length >= 2) {
                    final PregenCommand command = cat.getSubCommand(args[1]);
                    if (command != null) {
                        command.execute(container, shiftArguments(args, 2));
                    }
                    else {
                        container.sendChatMessage("Command not Found!");
                        this.printHelp(container, cat);
                    }
                }
                else {
                    container.sendChatMessage("Error: Command ends in Command Category. Here are the subCommands!");
                    this.printHelp(container, cat);
                }
            }
            else {
                PregenCommand command = this.noCategoryCommands.get(args[0].toLowerCase());
                if (command != null) {
                    command.execute(container, shiftArguments(args, 1));
                }
                else {
                    command = this.allCommands.get(args[0].toLowerCase());
                    if (command != null) {
                        cat = this.commandsToCategory.get(command.getName());
                        if (cat != null) {
                            container.sendChatMessage("Command " + args[0] + " exists but it is in a Category. Please use the Category '" + cat.getName() + "' before the SubCommand Name!");
                        }
                        else {
                            container.sendChatMessage("You are using a bugged out command. Please Report to mod Dev!");
                        }
                    }
                    else {
                        container.sendChatMessage("Command not Found!");
                        this.printHelp(container, (String)null);
                    }
                }
            }
        }
    }
    
    public List<String> func_180525_a(final ICommandSender sender, final String[] args, final BlockPos pos) {
        if (args.length == 1) {
            return PregenCommand.getBestMatch(args, this.createSubList(this.getCommandCategories(), this.getNoCommandsOption()));
        }
        if (args.length > 1) {
            final CommandCategory cat = this.categories.get(args[0].toLowerCase());
            if (cat != null) {
                if (args.length <= 2) {
                    return PregenCommand.getBestMatch(args, cat.getSubCommandNames());
                }
                final PregenCommand command = cat.getSubCommand(args[1]);
                if (command != null) {
                    final int layer = args.length - 1;
                    return command.getAutoCompleteOption(args, layer, layer - 2);
                }
            }
            else {
                final PregenCommand command = this.noCategoryCommands.get(args[0].toLowerCase());
                if (command != null) {
                    final int layer = args.length - 1;
                    return command.getAutoCompleteOption(args, layer, layer - 1);
                }
            }
        }
        return new ArrayList<String>();
    }
    
    public List<String> createSubList(final Collection<String>... lists) {
        final List<String> list = new ArrayList<String>();
        for (final Collection<String> entry : lists) {
            list.addAll(entry);
        }
        return list;
    }
    
    public List<String> getNoCommandsOption() {
        final List<String> list = new ArrayList<String>();
        for (final PregenCommand command : this.noCategoryCommands.values()) {
            list.add(command.getName());
        }
        return list;
    }
    
    public List<String> getCommandCategories() {
        final List<String> list = new ArrayList<String>();
        for (final CommandCategory command : this.categories.values()) {
            list.add(command.getName());
        }
        return list;
    }
    
    public List<String> getAllSubCommands() {
        final List<String> list = new ArrayList<String>();
        for (final PregenCommand command : this.allCommands.values()) {
            list.add(command.getName());
        }
        return list;
    }
    
    public PregenCommand getCommand(final String name) {
        return this.allCommands.get(name.toLowerCase());
    }
    
    public boolean hasCategory(final PregenCommand command) {
        return this.commandsToCategory.containsKey(command.getName());
    }
    
    public String getCategoryForCommand(final PregenCommand command) {
        return this.commandsToCategory.get(command.getName()).getName();
    }
    
    public void printHelp(final CommandContainer container, final String commandName) {
        if (commandName == null) {
            container.sendChatMessage("/pregen subCommands");
            for (final CommandCategory cat : this.categories.values()) {
                this.printHelp(container, cat);
                container.sendChatMessage("");
            }
            for (final PregenCommand com : this.noCategoryCommands.values()) {
                this.printHelp(container, com, false);
            }
        }
        else {
            final CommandCategory cat2 = this.categories.get(commandName);
            if (cat2 != null) {
                this.printHelp(container, cat2);
            }
            else {
                final PregenCommand command = this.allCommands.get(commandName);
                if (command != null) {
                    this.printHelp(container, command, true);
                }
                else {
                    container.sendChatMessage("No Command or Command Category found!");
                }
            }
        }
    }
    
    public void printHelp(final CommandContainer container, final CommandCategory cat) {
        container.sendChatMessage("Category " + cat.getName() + ": " + cat.getDescription());
        container.sendChatMessage("Category Commands: ");
        for (final PregenCommand pregen : cat.getSubCommands().values()) {
            container.sendChatMessage("§l" + pregen.getName() + "§r: " + pregen.getDescription());
        }
    }
    
    public void printHelp(final CommandContainer container, final PregenCommand command, final boolean args) {
        if (args) {
            container.sendChatMessage("");
        }
        container.sendChatMessage((args ? "Command " : "") + "§l" + command.getName() + "§r: " + command.getDescription());
        if (args) {
            container.sendChatMessage("Command Arguments: ");
            int i = 0;
            for (final String s : command.getArgumentDescriptions()) {
                container.sendChatMessage("[" + i + "]: " + s);
                ++i;
            }
        }
    }
    
    public static String[] shiftArguments(final String[] s, final int amount) {
        if (s == null || s.length - amount <= 0) {
            return new String[0];
        }
        final String[] s2 = new String[s.length - amount];
        System.arraycopy(s, amount, s2, 0, s2.length);
        return s2;
    }
}
