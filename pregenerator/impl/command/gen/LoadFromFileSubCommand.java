// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.gen;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import pregenerator.impl.misc.FilePos;
import java.util.Arrays;
import pregenerator.impl.command.PregenBaseCommand;
import pregenerator.impl.command.base.PregenCommand;
import pregenerator.impl.command.CompleterHelper;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import pregenerator.impl.storage.PregenTask;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.io.File;
import pregenerator.ChunkPregenerator;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class LoadFromFileSubCommand extends BasePregenCommand
{
    public LoadFromFileSubCommand() {
        super(1);
        this.addDescription(0, "FileName: The Name of the file that should be loaded in the config folder from + extension. Example.txt");
        this.addSuggestion("loadFromFile Example.txt", "Loads the none existed Example file and reads the tasks");
    }
    
    @Override
    public String getName() {
        return "loadFromFile";
    }
    
    @Override
    public String getDescription() {
        return "Loads tasks from a File";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 1;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (args.length >= 1) {
            final List<String> list = this.getLines(new File(ChunkPregenerator.pregeneratorFolder, args[0]));
            if (list.isEmpty()) {
                container.sendChatMessage("No Lines found!");
                return;
            }
            container.sendChatMessage(list.size() + " valid Lines found!");
            final Map<Integer, String> errorMap = new LinkedHashMap<Integer, String>();
            final List<PregenTask> pregenTasks = new ArrayList<PregenTask>();
            for (int i = 0; i < list.size(); ++i) {
                final List<PregenTask> tasks = this.getTasks(container, list.get(i).split(" "), i, errorMap);
                if (tasks != null) {
                    pregenTasks.addAll(tasks);
                }
            }
            if (pregenTasks.isEmpty()) {
                container.sendChatMessage("No Tasks Could have been created");
                return;
            }
            if (errorMap.size() > 0) {
                container.sendChatMessage("Errors Found: ");
                for (final Map.Entry<Integer, String> entry : errorMap.entrySet()) {
                    container.sendChatMessage("Line: " + list.get(entry.getKey()) + " had the following Error: " + entry.getValue());
                }
                container.sendChatMessage("Task Loading Aborted. Fix the errors to create the tasks");
                return;
            }
            container.sendChatMessage("SuccessFully Created " + pregenTasks.size() + " Tasks");
            if (container.onProcessStarted(pregenTasks.get(0))) {
                container.getStorage().savePregenTasks(pregenTasks);
                container.sendChatMessage("Pregenerator is already running so added all the tasks to the TaskList");
                return;
            }
            container.getStorage().savePregenTasks(pregenTasks);
            container.getProcessor().startTask(pregenTasks.get(0));
        }
        else {
            this.throwErrors(container, args.length);
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0) {
            final CompleterHelper helper = LoadFromFileSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.GEN_FILE);
        }
        return new ArrayList<String>();
    }
    
    public List<PregenTask> getTasks(final CommandContainer container, String[] args, final int index, final Map<Integer, String> errors) {
        if (args == null || args.length == 0) {
            return null;
        }
        final String taskType = args[0];
        args = PregenBaseCommand.shiftArguments(args, 1);
        if (taskType.equalsIgnoreCase("startradius")) {
            if (args.length >= 4) {
                final int type = BasePregenCommand.getGenType(args[0]);
                FilePos center = BasePregenCommand.getChunkPos(args[1], args[2], container.getPlayerPos());
                final int radius = BasePregenCommand.getNumber(args[3], 0);
                final int dimension = BasePregenCommand.getDimension(container, PregenCommand.getArg(args, 4));
                final int postRule = BasePregenCommand.getProcessRule(PregenCommand.getArg(args, 5));
                if (!BasePregenCommand.isDimensionValid(dimension)) {
                    errors.put(index, "Dimension " + dimension + " is not Registered!");
                    return null;
                }
                if (radius > 25000) {
                    errors.put(index, "Radius " + radius + " Chunks is to big. Say below 25000 Chunks (400.000 Blocks each direction) or use startmassradius");
                    return null;
                }
                center = BasePregenCommand.applySpawn(args[1], args[2], center, container.getWorldSpawn(dimension));
                return Arrays.asList(new PregenTask(type, dimension, center.x, center.z, radius, 0, postRule));
            }
        }
        else if (taskType.equalsIgnoreCase("startarea")) {
            if (args.length >= 5) {
                final int type = BasePregenCommand.getGenType(args[0]);
                final FilePos minPos = BasePregenCommand.getChunkPos(args[1], args[2], container.getPlayerPos());
                final FilePos maxPos = BasePregenCommand.getChunkPos(args[3], args[4], container.getPlayerPos());
                final int dimension = BasePregenCommand.getDimension(container, PregenCommand.getArg(args, 5));
                final int postRule = BasePregenCommand.getProcessRule(PregenCommand.getArg(args, 6));
                if (!BasePregenCommand.isDimensionValid(dimension)) {
                    errors.put(index, "Dimension " + dimension + " is not Registered!");
                    return null;
                }
                final long count = BasePregenCommand.getFullCount(minPos.x, minPos.z, maxPos.x, maxPos.z);
                if (count > 2500000000L) {
                    errors.put(index, "Area Generation uses more then 2.5 Billion Chunks. That is to big. Please make it smaller. (Your Area Amount: " + count + ")");
                    return null;
                }
                return Arrays.asList(new PregenTask(2, dimension, minPos.x, minPos.z, maxPos.x, maxPos.z, postRule));
            }
        }
        else if (taskType.equalsIgnoreCase("startextention")) {
            if (args.length >= 5) {
                final int type = BasePregenCommand.getGenType(args[0]);
                FilePos center = BasePregenCommand.getChunkPos(args[1], args[2], container.getPlayerPos());
                final int minRadius = BasePregenCommand.getNumber(args[3], 0);
                final int maxRadius = BasePregenCommand.getNumber(args[4], 0);
                final int dimension2 = BasePregenCommand.getDimension(container, PregenCommand.getArg(args, 5));
                final int postRule2 = BasePregenCommand.getProcessRule(PregenCommand.getArg(args, 6));
                if (!BasePregenCommand.isDimensionValid(dimension2)) {
                    errors.put(index, "Dimension " + dimension2 + " is not Registered!");
                    return null;
                }
                final long ringCount = BasePregenCommand.getRingCount(minRadius, maxRadius, type == 1);
                if (ringCount > 2500000000L) {
                    errors.put(index, "Expansion uses more then 2.5 Billion Chunks. That is to big. Please make it smaller. (Your Expansion Amount: " + ringCount + ")");
                    return null;
                }
                center = BasePregenCommand.applySpawn(args[1], args[2], center, container.getWorldSpawn(dimension2));
                return Arrays.asList(new PregenTask(4 + type, dimension2, center.x, center.z, minRadius, maxRadius, postRule2));
            }
        }
        else if (taskType.equals("startregion")) {
            if (args.length >= 2) {
                FilePos center2 = BasePregenCommand.getChunkPos(args[0], args[1], container.getPlayerPos()).toChunkFile();
                final int dimension3 = BasePregenCommand.getDimension(container, PregenCommand.getArg(args, 3));
                final int postRule3 = BasePregenCommand.getProcessRule(PregenCommand.getArg(args, 4));
                if (!BasePregenCommand.isDimensionValid(dimension3)) {
                    errors.put(index, "Dimension " + dimension3 + " is not Registered!");
                    return null;
                }
                center2 = BasePregenCommand.applySpawn(args[0], args[1], center2, container.getWorldSpawn(dimension3));
                return Arrays.asList(new PregenTask(2, dimension3, center2.x * 32, center2.z * 32, center2.x * 32 + 32, center2.z * 32 + 32, postRule3));
            }
        }
        else if (taskType.equalsIgnoreCase("startmassradius")) {
            final int type = BasePregenCommand.getGenType(args[0]);
            FilePos center = BasePregenCommand.getChunkPos(args[1], args[2], container.getPlayerPos());
            final int radius = BasePregenCommand.getNumber(args[3], 0);
            final int splitRadius = BasePregenCommand.clamp(BasePregenCommand.parseNumber(PregenCommand.getArg(args, 4), 25000), 100, 25000);
            final int dimension2 = BasePregenCommand.getDimension(container, PregenCommand.getArg(args, 5));
            final int postRule2 = BasePregenCommand.getProcessRule(PregenCommand.getArg(args, 6));
            if (!BasePregenCommand.isDimensionValid(dimension2)) {
                errors.put(index, "Dimension " + dimension2 + " is not Registered!");
                return null;
            }
            center = BasePregenCommand.applySpawn(args[1], args[2], center, container.getWorldSpawn(dimension2));
            return StartMassRadiusSubCommand.createTaskList(type, center, radius, dimension2, postRule2, splitRadius);
        }
        errors.put(index, "Known Command!");
        return new ArrayList<PregenTask>();
    }
    
    public List<String> getLines(final File file) {
        final List<String> list = new ArrayList<String>();
        if (!file.exists()) {
            return list;
        }
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(file));
            for (String data = reader.readLine(); data != null; data = reader.readLine()) {
                if (!data.startsWith("//")) {
                    list.add(data);
                }
            }
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
