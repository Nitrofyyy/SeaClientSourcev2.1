// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator;

import pregenerator.impl.command.base.BasePregenCommand;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.EnumSet;
import pregenerator.impl.processor.generator.ChunkLogger;
import net.minecraftforge.fml.common.FMLCommonHandler;
import pregenerator.impl.retrogen.RetrogenHandler;
import pregenerator.impl.processor.generator.ChunkProcessor;
import pregenerator.base.api.misc.IConfig;

public class ConfigManager
{
    static IConfig config;
    public static boolean shouldContinue;
    public static int playerDeactivation;
    public static int msPerTick;
    public static int skippingAmount;
    public static boolean tracking;
    public static boolean autoRestart;
    public static int restartMemory;
    public static int[] dimensionOrder;
    public static boolean orderOnly;
    public static boolean autoClearMineshaft;
    
    public static void createConfig(final IConfig theConfig) {
        ConfigManager.config = theConfig;
        try {
            final ChunkProcessor pro = ChunkProcessor.INSTANCE;
            RetrogenHandler.INSTANCE.preInit(ConfigManager.config);
            pro.setMaxTime(ConfigManager.config.getInt("general", "TimePerTick", 10, Integer.MAX_VALUE, 40, "Storage for the TimePerTickCommand if needs to be changed outside of the game"));
            ConfigManager.shouldContinue = ConfigManager.config.getBoolean("general", "AutoStart", false, "Config for auto restart on World Load. Do not change unless you have to!");
            ConfigManager.playerDeactivation = ConfigManager.config.getInt("general", "PlayerLimit", -1, Integer.MAX_VALUE, -1, "Disables the Pregenerator when a certain playercount is reached. -1 disables it.");
            ConfigManager.skippingAmount = ConfigManager.config.getInt("general", "SkippingMarker", -1, "NEVER CHANGE THIS! Its a Marker for the SkipChunkCommand");
            ConfigManager.tracking = ConfigManager.config.getBoolean("general", "ServerTracking", false, "Enables that the Pregenerator does a bit of live profiling. Its not the best but it does things that others don't do");
            ConfigManager.autoRestart = ConfigManager.config.getBoolean("general", "autoRestart", FMLCommonHandler.instance().getSide().isServer(), "Auto Stops the game when the memory gets to low on average, ");
            ConfigManager.restartMemory = ConfigManager.config.getInt("general", "restartMemory", 1024, "How much free memory should be left until the stop kicks in");
            ConfigManager.autoClearMineshaft = ConfigManager.config.getBoolean("general", "autocleanMineshafts", true, "Automatically removes mineshaft command references during pregeneration. This fixes a memory leak. No worldgen effect");
            pro.setPriority(ConfigManager.config.getBoolean("general", "PregenPriority", false, "Sets if the pregenerator should have the priority"));
            final EnumSet<ChunkLogger> log = ChunkProcessor.INSTANCE.getLoggerInfo();
            final List<ChunkLogger> loggers = ChunkLogger.getLoggers();
            for (final ChunkLogger entry : loggers) {
                addIfTrue(entry, log);
            }
            final String dims = ConfigManager.config.getString("preview", "Dimension Order", "0:-1:1", "Sets the order of the Preview Guis Dimension loading. Format: 0:1:-1");
            final String[] values = dims.split(":");
            final int[] dimensions = new int[values.length];
            for (int i = 0; i < values.length; ++i) {
                try {
                    dimensions[i] = Integer.parseInt(values[i]);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ConfigManager.dimensionOrder = dimensions;
            ConfigManager.orderOnly = ConfigManager.config.getBoolean("preview", "OrderOnly", false, "Instead of Collecting all of the Dimensions the Preview Gui will only allow the dimensions provided by 'Dimension Order'");
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        finally {
            ConfigManager.config.saveConfig();
        }
    }
    
    private static void addIfTrue(final ChunkLogger type, final EnumSet<ChunkLogger> log) {
        if (ConfigManager.config.getBoolean("info", type.getName(), type.getDefault())) {
            log.add(type);
        }
    }
    
    public static List<Integer> getDimensions() {
        final List<Integer> list = new ArrayList<Integer>();
        for (final int id : ConfigManager.dimensionOrder) {
            if (BasePregenCommand.isDimensionValid(id)) {
                list.add(id);
            }
        }
        return list;
    }
    
    public static void changeTracking(final boolean result) {
        ConfigManager.config.setBoolean("general", "ServerTracking", result);
        ConfigManager.tracking = result;
    }
    
    public static void saveStart(final boolean flag) {
        ConfigManager.config.setBoolean("general", "AutoStart", flag);
        ConfigManager.shouldContinue = flag;
    }
    
    public static void setPrority(final boolean flag) {
        ConfigManager.config.setBoolean("general", "PregenPriority", flag);
    }
    
    public static void setRestart(final boolean flag) {
        ConfigManager.config.setBoolean("general", "autoRestart", flag);
        ConfigManager.autoRestart = flag;
    }
    
    public static void setRestartMemory(final int memory) {
        ConfigManager.config.setInt("general", "restartMemory", memory);
        ConfigManager.restartMemory = memory;
    }
    
    public static void setPlayerCount(final int count) {
        ConfigManager.config.setInt("general", "PlayerLimit", count);
        ConfigManager.playerDeactivation = count;
    }
    
    public static void saveTime(final int time) {
        ConfigManager.config.setInt("general", "TimePerTick", time);
    }
    
    public static void setSkipMarker(final int amount) {
        ConfigManager.skippingAmount = amount;
        ConfigManager.config.setInt("general", "SkippingMarker", amount);
    }
    
    public static void saveType() {
        final EnumSet<ChunkLogger> logging = ChunkProcessor.INSTANCE.getLoggerInfo();
        ConfigManager.config.prepaireMassChange();
        for (final ChunkLogger log : ChunkLogger.getLoggers()) {
            ConfigManager.config.setBoolean("info", log.getName(), logging.contains(log));
        }
        ConfigManager.config.saveConfig();
    }
    
    public static void setBackupData(final String value) {
        ConfigManager.config.setString("general", "backupData", value);
    }
    
    public static String getBackupData() {
        return ConfigManager.config.getString("general", "backupData", "", "DO NOT TOUCH THIS!!! UNLESS YOU WANT TO CORRUPT YOUR WORLD!!!");
    }
    
    static {
        ConfigManager.msPerTick = 40;
        ConfigManager.skippingAmount = -1;
        ConfigManager.restartMemory = 1024;
        ConfigManager.dimensionOrder = new int[0];
        ConfigManager.orderOnly = false;
        ConfigManager.autoClearMineshaft = true;
    }
}
