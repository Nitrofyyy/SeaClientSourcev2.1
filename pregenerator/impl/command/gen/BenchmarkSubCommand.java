// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.gen;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.Entity;
import pregenerator.impl.storage.PregenTask;
import net.minecraftforge.common.DimensionManager;
import net.minecraft.entity.player.EntityPlayer;
import pregenerator.impl.processor.generator.BenchmarkManager;
import pregenerator.impl.command.base.PregenCommand;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class BenchmarkSubCommand extends BasePregenCommand
{
    public BenchmarkSubCommand() {
        super(3);
        this.addDescription(0, "start/submit: (Start: to start the Default Benchmark (Player Only Command!!!!!!!!))/(Submit: to Submit ALL Previous Benchmarks)");
        this.addDescription(1, "(Optional) Type: if the Benchmark should be small or large Benchmark. Player Only Command!!!!!!");
        this.addDescription(2, "(Optional) Analytics: If the benchmarks should be send to the Analytics Server. (Opt in Only). Player Only Command!!!!!!");
        this.addSuggestion("benchmark", "to get information about how Benchmarks work and what is send to the analytics if requested");
        this.addSuggestion("benchmark start", "to start the default Small Benchmark Task without sending Analytics Data");
        this.addSuggestion("benchmark start big", "to start a Big Benchmark Task without sending Analytics Data");
        this.addSuggestion("benchmark start small true", "to start a Small Benchmark Task and sending the Results to the analytics server");
        this.addSuggestion("benchmark submit", "to submit ALL previous benchmarks to the analytics server");
    }
    
    @Override
    public String getName() {
        return "benchmark";
    }
    
    @Override
    public String getDescription() {
        return "Starts a Benchmark to test how well worldgeneration runs on the Computer/Server";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 0;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (args.length >= 1) {
            if ("submit".equalsIgnoreCase(PregenCommand.getArg(args, 0))) {
                container.sendChatMessage("Attempting to send privious Benchmarks to Analytics server");
                if (BenchmarkManager.INSTANCE.reportMass()) {
                    container.sendChatMessage("Send Benchmarks to Analytics Server");
                    return;
                }
                container.sendChatMessage("No or Corrupted Benchmarks found");
            }
            else {
                if (!"start".equalsIgnoreCase(PregenCommand.getArg(args, 0))) {
                    container.sendChatMessage(PregenCommand.getArg(args, 0) + " Is a unknown command");
                    return;
                }
                final BenchmarkManager manager = BenchmarkManager.INSTANCE;
                if (manager.isBenchmarkRunning()) {
                    container.sendChatMessage("Benchmark already running.");
                    return;
                }
                final Entity entity = container.getSender().func_174793_f();
                if (!(entity instanceof EntityPlayer)) {
                    container.sendChatMessage("A Player Must execute this command!");
                    return;
                }
                container.sendChatMessage("Starting Benchmarks");
                final boolean big = "big".equalsIgnoreCase(PregenCommand.getArg(args, 1));
                final boolean analytics = Boolean.parseBoolean(PregenCommand.getArg(args, 2));
                final Integer[] dimensions = DimensionManager.getStaticDimensionIDs();
                for (int i = 0; i < dimensions.length; ++i) {
                    final PregenTask task = new PregenTask(big ? 7 : 6, dimensions[i], 0, 0, big ? 250 : 100, big ? 250 : 100, 1);
                    if (container.onProcessStarted(task)) {
                        container.sendChatMessage("Pregenerator already running. Adding Task to the TaskStorage");
                    }
                    else {
                        container.getProcessor().startTask(task);
                    }
                }
                manager.startBenchmark(entity.func_110124_au(), analytics);
            }
        }
        else {
            container.sendChatMessage("The Benchmark command will start a small (40k Chunks) or Big (250k Chunks) task for every Dimension");
            container.sendChatMessage("Once everything is generated a score will be calculated for each dimension and overall.");
            container.sendChatMessage("The score is based on How many Milliseconds it took to generate 1 chunk on average. Lower => Better");
            container.sendChatMessage("On top of that Analytics can be enabled Optionally, or can be send later with submit-cache");
            container.sendChatMessage("If thats the case the result of the Benchmark + Extra data is being send to a Server");
            container.sendChatMessage("Collected Data includes: User Unique Identifier, Session Instance, CPU Info, Provided Ram, World-Seed, Java Version, MC Version, Loaded Mods and Benchmark Results");
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0) {
            return PregenCommand.getBestMatch(args, "start", "submit");
        }
        if (commandIndex == 1) {
            return PregenCommand.getBestMatch(args, "small", "big");
        }
        if (commandIndex == 2) {
            return PregenCommand.getBestMatch(args, "false", "true");
        }
        return new ArrayList<String>();
    }
}
