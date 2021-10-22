// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.preview.world;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.block.material.MapColor;
import pregenerator.base.impl.misc.DimensionLister;
import org.apache.commons.lang3.StringUtils;
import java.io.File;
import net.minecraft.server.integrated.IntegratedServer;
import pregenerator.impl.storage.TaskStorage;
import pregenerator.impl.processor.generator.ChunkProcessor;
import net.minecraft.crash.CrashReportCategory;
import java.lang.reflect.Field;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraft.world.WorldType;
import java.util.Random;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraft.world.WorldSettings;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiTextField;

public class WorldSeed
{
    static boolean init;
    static boolean betterCompression;
    GuiTextField seedField;
    GuiCreateWorld prevGui;
    GuiScreen mainMenu;
    WorldSettings settings;
    String name;
    String folderName;
    long seed;
    
    public WorldSeed(final GuiCreateWorld gui) {
        this.prevGui = gui;
        this.seedField = (GuiTextField)ReflectionHelper.getPrivateValue((Class)GuiCreateWorld.class, (Object)this.prevGui, 2);
        this.name = ((GuiTextField)ReflectionHelper.getPrivateValue((Class)GuiCreateWorld.class, (Object)this.prevGui, 1)).func_146179_b().trim();
        this.folderName = (String)ReflectionHelper.getPrivateValue((Class)GuiCreateWorld.class, (Object)this.prevGui, 3);
        this.mainMenu = (GuiScreen)ReflectionHelper.getPrivateValue((Class)GuiCreateWorld.class, (Object)this.prevGui, 0);
        this.readSeed();
    }
    
    private void readSeed() {
        final long textSeed = makeSeed(this.seedField.func_146179_b());
        this.seed = ((textSeed == 0L) ? new Random().nextLong() : textSeed);
        this.seedField.func_146180_a(Long.toString(this.seed));
        if (this.settings == null) {
            this.createSettings();
            return;
        }
        this.settings.func_77165_h().onGUICreateWorldPress();
        final WorldSettings newSettings = new WorldSettings(this.seed, this.settings.func_77162_e(), this.settings.func_77164_g(), this.settings.func_77158_f(), this.settings.func_77165_h());
        if (this.settings.func_77167_c()) {
            newSettings.func_77159_a();
        }
        if (this.settings.func_77163_i()) {
            newSettings.func_77166_b();
        }
        this.settings = newSettings;
    }
    
    private void createSettings() {
        final int selectedIndex = (int)ReflectionHelper.getPrivateValue((Class)GuiCreateWorld.class, (Object)this.prevGui, 24);
        WorldType.field_77139_a[selectedIndex].onGUICreateWorldPress();
        final WorldSettings worldsettings = new WorldSettings(this.seed, WorldSettings.GameType.func_77142_a((String)ReflectionHelper.getPrivateValue((Class)GuiCreateWorld.class, (Object)this.prevGui, 4)), (boolean)ReflectionHelper.getPrivateValue((Class)GuiCreateWorld.class, (Object)this.prevGui, 6), (boolean)ReflectionHelper.getPrivateValue((Class)GuiCreateWorld.class, (Object)this.prevGui, 10), WorldType.field_77139_a[selectedIndex]);
        worldsettings.func_82750_a(this.prevGui.field_146334_a);
        if ((boolean)ReflectionHelper.getPrivateValue((Class)GuiCreateWorld.class, (Object)this.prevGui, 9) && !(boolean)ReflectionHelper.getPrivateValue((Class)GuiCreateWorld.class, (Object)this.prevGui, 10)) {
            worldsettings.func_77159_a();
        }
        if ((boolean)ReflectionHelper.getPrivateValue((Class)GuiCreateWorld.class, (Object)this.prevGui, 7) && !(boolean)ReflectionHelper.getPrivateValue((Class)GuiCreateWorld.class, (Object)this.prevGui, 10)) {
            worldsettings.func_77166_b();
        }
        this.settings = worldsettings;
    }
    
    public WorldSettings getSettings() {
        return this.settings;
    }
    
    public String getWorldName() {
        return this.name;
    }
    
    public String getFolderName() {
        return this.folderName;
    }
    
    public GuiScreen getMainMenu() {
        return this.mainMenu;
    }
    
    public GuiCreateWorld getPrevGui() {
        return this.prevGui;
    }
    
    public long getSeed() {
        return this.seed;
    }
    
    public String getTextSeed() {
        return this.seedField.func_146179_b();
    }
    
    public void setSeed(final String seed) {
        ReflectionHelper.setPrivateValue((Class)GuiCreateWorld.class, (Object)this.prevGui, (Object)seed, 22);
        this.seedField.func_146180_a(seed);
        this.readSeed();
    }
    
    public CustomServer createServer() {
        final String folderName = "Preview";
        FMLClientHandler.instance().startIntegratedServer(folderName, this.name, this.settings);
        System.gc();
        final Minecraft mc = Minecraft.func_71410_x();
        try {
            final CustomServer server = new CustomServer(mc, folderName, this.name, this.settings);
            final Field field = this.findServer();
            field.setAccessible(true);
            field.set(mc, server);
            return server;
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.func_85055_a(throwable, "Starting integrated server");
            final CrashReportCategory crashreportcategory = crashreport.func_85058_a("Starting integrated server");
            crashreportcategory.func_71507_a("Level ID", (Object)folderName);
            crashreportcategory.func_71507_a("Level Name", (Object)this.name);
            throw new ReportedException(crashreport);
        }
    }
    
    public void destroyServer(final CustomServer server) {
        server.func_152344_a((Runnable)new Runnable() {
            @Override
            public void run() {
                if (ChunkProcessor.INSTANCE.isRunning()) {
                    ChunkProcessor.INSTANCE.interruptTask(false, true, true);
                    TaskStorage.getStorage().clearAll();
                }
            }
        });
        if (server.func_71241_aa()) {
            return;
        }
        server.func_71263_m();
        while (!server.func_71241_aa()) {
            try {
                Thread.sleep(1L);
            }
            catch (Exception ex) {}
        }
        try {
            final Field field = this.findServer();
            field.setAccessible(true);
            field.set(Minecraft.func_71410_x(), null);
        }
        catch (Exception ex2) {}
    }
    
    private Field findServer() {
        final Minecraft mc = Minecraft.func_71410_x();
        for (final Field field : mc.getClass().getDeclaredFields()) {
            if (IntegratedServer.class.isAssignableFrom(field.getType())) {
                return field;
            }
        }
        return null;
    }
    
    public void removePreview(final boolean fully) {
        removeFile(getPreviewFolder(), false);
    }
    
    public void createPaths() {
        getMapFolder().mkdirs();
    }
    
    public boolean forceRemovePreview(final boolean tempOnly) {
        final File file = tempOnly ? getMapFolder() : getPreviewFolder();
        for (int limit = 0; file.exists() && limit < 50; ++limit) {
            removeFile(file, true);
            if (file.exists()) {
                try {
                    System.gc();
                }
                catch (Exception ex) {}
            }
        }
        return !file.exists();
    }
    
    public static long makeSeed(final String input) {
        if (!StringUtils.isEmpty((CharSequence)input)) {
            try {
                return Long.parseLong(input);
            }
            catch (NumberFormatException var7) {
                return input.hashCode();
            }
        }
        return 0L;
    }
    
    public static String getDimensionName(final int id) {
        try {
            return DimensionLister.getDimensionName(id);
        }
        catch (Exception e) {
            return "Unknown";
        }
    }
    
    public static File getPreviewFolder() {
        return new File(Minecraft.func_71410_x().field_71412_D, "saves/Preview");
    }
    
    public static File getMapFolder() {
        return new File(getPreviewFolder(), "previewData");
    }
    
    public static boolean canUseBetterCompression() {
        if (!WorldSeed.init) {
            int largest = 0;
            for (int i = 0; i < MapColor.field_76281_a.length; ++i) {
                if (MapColor.field_76281_a[i] != null) {
                    largest = i;
                }
            }
            WorldSeed.betterCompression = (BiomeGenBase.field_180278_o.values().size() <= 256 && largest <= 83);
            WorldSeed.init = true;
        }
        return WorldSeed.betterCompression;
    }
    
    public static boolean isUsingCompression() {
        return false;
    }
    
    public static void removeFile(final File file, final boolean itself) {
        if (!file.exists()) {
            return;
        }
        for (final File sub : file.listFiles()) {
            try {
                if (sub.isDirectory()) {
                    removeFile(sub, true);
                }
                else {
                    sub.delete();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (itself) {
            try {
                file.delete();
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
    
    public static long[] getData() {
        final long first = Runtime.getRuntime().totalMemory();
        final long second = Runtime.getRuntime().freeMemory();
        final long last = Runtime.getRuntime().maxMemory();
        return new long[] { first - second, first, last };
    }
    
    public static long value(final long input) {
        return input / 1024L / 1024L;
    }
    
    static {
        WorldSeed.init = false;
        WorldSeed.betterCompression = false;
    }
}
