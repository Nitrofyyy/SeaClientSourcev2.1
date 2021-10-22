// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator;

import java.util.concurrent.Executors;
import org.apache.logging.log4j.LogManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;
import pregenerator.impl.command.base.CommandContainer;
import net.minecraft.command.ICommandSender;
import pregenerator.impl.storage.TaskStorage;
import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import pregenerator.impl.storage.GlobalListeners;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import pregenerator.impl.network.packets.gui.ManualTaskPacket;
import pregenerator.impl.network.packets.gui.ProcessAnswerPacket;
import pregenerator.impl.network.packets.gui.ProcessRequestPacket;
import pregenerator.impl.network.packets.gui.DimensionTaskPacket;
import pregenerator.impl.network.packets.gui.DeletionTaskPacket;
import pregenerator.impl.network.packets.gui.MassPregenTaskPacket;
import pregenerator.impl.network.packets.gui.PregenTaskPacket;
import pregenerator.impl.network.packets.retrogen.RetrogenChangePacket;
import pregenerator.impl.network.packets.retrogen.RetrogenCheckAnswerPacket;
import pregenerator.impl.network.packets.retrogen.RetrogenCheckPacket;
import pregenerator.impl.network.packets.chunkRequest.RemoveStructurePacket;
import pregenerator.impl.network.packets.chunkRequest.StructureAnswerPacket;
import pregenerator.impl.network.packets.chunkRequest.StructureRequestPacket;
import pregenerator.impl.network.packets.chunkRequest.KillWorldRequest;
import pregenerator.impl.network.packets.chunkRequest.EntityAnswerPacket;
import pregenerator.impl.network.packets.chunkRequest.EntityRequestPacket;
import pregenerator.impl.network.packets.PermissionAnswerPacket;
import pregenerator.impl.network.packets.PermissionRequestPacket;
import pregenerator.impl.network.packets.chunkRequest.KillRequest;
import pregenerator.impl.network.packets.chunkRequest.TPChunkPacket;
import pregenerator.impl.network.packets.chunkRequest.ChunkAnswerPacket;
import pregenerator.impl.network.packets.chunkRequest.ChunkRequest;
import pregenerator.impl.network.packets.DimAnswerPacket;
import pregenerator.impl.network.packets.DimRequestPacket;
import pregenerator.impl.network.packets.TrackerAnswerPacket;
import pregenerator.impl.network.packets.TrackerRequestPacket;
import pregenerator.impl.network.packets.AnswerPacket;
import pregenerator.base.api.network.PregenPacket;
import pregenerator.impl.network.packets.RequestPacket;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import pregenerator.impl.tracking.ChunkEntry;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.common.util.EnumHelper;
import java.util.LinkedHashMap;
import net.minecraft.world.chunk.storage.RegionFileCache;
import pregenerator.impl.retrogen.RetrogenHandler;
import pregenerator.impl.tracking.ServerTracker;
import pregenerator.impl.structure.StructureManager;
import net.minecraftforge.common.MinecraftForge;
import pregenerator.impl.processor.deleter.DeleteProcessor;
import pregenerator.impl.processor.generator.ChunkProcessor;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;
import java.nio.ByteBuffer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import java.io.File;
import pregenerator.impl.command.PregenBaseCommand;
import pregenerator.base.api.network.INetworkManager;
import java.util.concurrent.ExecutorService;
import net.minecraftforge.fml.common.SidedProxy;
import pregenerator.base.PregenAPI;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = "chunkpregenerator", version = "2.4.1", name = "Chunk Pregenerator", acceptableRemoteVersions = "*", acceptedMinecraftVersions = "[1.8,1.8.9]")
public class ChunkPregenerator
{
    public static Logger LOGGER;
    public static PregenAPI pregenBase;
    @SidedProxy(clientSide = "pregenerator.impl.client.PregenClientProxy", serverSide = "pregenerator.PregenProxy")
    public static PregenProxy proxy;
    public static final ExecutorService SERVICE;
    public static INetworkManager networking;
    public static PregenBaseCommand pregenCommand;
    public static File pregeneratorFolder;
    public static String[] fileText;
    
    @Mod.EventHandler
    public void load(final FMLPreInitializationEvent event) {
        try {
            final ByteBuffer buffer = ByteBuffer.allocate(1600);
            buffer.put((byte)1);
            buffer.putInt(1000).putInt(2000);
            buffer.put((byte)1);
            final Random rand = new Random();
            for (int i = 0; i < 256; ++i) {
                buffer.putInt(rand.nextInt());
            }
            for (int i = 0; i < 256; ++i) {
                buffer.putShort((byte)rand.nextInt(256));
            }
            System.out.println("Test: " + buffer.remaining());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        ChunkPregenerator.pregenBase.init();
        ChunkPregenerator.pregeneratorFolder = new File(event.getModConfigurationDirectory(), "pregenerator");
        if (!ChunkPregenerator.pregeneratorFolder.exists()) {
            ChunkPregenerator.pregeneratorFolder.mkdirs();
            try {
                final BufferedWriter buffered = new BufferedWriter(new FileWriter(new File(ChunkPregenerator.pregeneratorFolder, "ExampleFile.txt")));
                for (int j = 0; j < ChunkPregenerator.fileText.length; ++j) {
                    buffered.write(ChunkPregenerator.fileText[j]);
                    if (j + 1 < ChunkPregenerator.fileText.length) {
                        buffered.newLine();
                    }
                }
                buffered.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        ChunkPregenerator.pregenBase.registerTickEvent(ChunkProcessor.INSTANCE);
        ChunkPregenerator.pregenBase.registerTickEvent(DeleteProcessor.INSTANCE);
        MinecraftForge.EVENT_BUS.register((Object)this);
        StructureManager.instance.init();
        ConfigManager.createConfig(ChunkPregenerator.pregenBase.getConfig(new File(ChunkPregenerator.pregeneratorFolder, "PregenConfig.cfg")));
        ServerTracker.INSTANCE.init();
        RetrogenHandler.INSTANCE.init();
        try {
            EnumHelper.setFailsafeFieldValue(RegionFileCache.class.getDeclaredFields()[0], (Object)null, (Object)new LinkedHashMap(256, 1.0f, true));
            FMLLog.getLogger().info("Made the Pregenerator ThreadSave");
            ChunkEntry.init();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Mod.EventHandler
    public void onLoad(final FMLInitializationEvent event) {
        ChunkPregenerator.pregenBase.postInit();
        (ChunkPregenerator.networking = ChunkPregenerator.pregenBase.createNetworking()).registerPacket(RequestPacket.class);
        ChunkPregenerator.networking.registerPacket(AnswerPacket.class);
        ChunkPregenerator.networking.registerPacket(TrackerRequestPacket.class);
        ChunkPregenerator.networking.registerPacket(TrackerAnswerPacket.class);
        ChunkPregenerator.networking.registerPacket(DimRequestPacket.class);
        ChunkPregenerator.networking.registerPacket(DimAnswerPacket.class);
        ChunkPregenerator.networking.registerPacket(ChunkRequest.class);
        ChunkPregenerator.networking.registerPacket(ChunkAnswerPacket.class);
        ChunkPregenerator.networking.registerPacket(TPChunkPacket.class);
        ChunkPregenerator.networking.registerPacket(KillRequest.class);
        ChunkPregenerator.networking.registerPacket(PermissionRequestPacket.class);
        ChunkPregenerator.networking.registerPacket(PermissionAnswerPacket.class);
        ChunkPregenerator.networking.registerPacket(EntityRequestPacket.class);
        ChunkPregenerator.networking.registerPacket(EntityAnswerPacket.class);
        ChunkPregenerator.networking.registerPacket(KillWorldRequest.class);
        ChunkPregenerator.networking.registerPacket(StructureRequestPacket.class);
        ChunkPregenerator.networking.registerPacket(StructureAnswerPacket.class);
        ChunkPregenerator.networking.registerPacket(RemoveStructurePacket.class);
        ChunkPregenerator.networking.registerPacket(RetrogenCheckPacket.class);
        ChunkPregenerator.networking.registerPacket(RetrogenCheckAnswerPacket.class);
        ChunkPregenerator.networking.registerPacket(RetrogenChangePacket.class);
        ChunkPregenerator.networking.registerPacket(PregenTaskPacket.class);
        ChunkPregenerator.networking.registerPacket(MassPregenTaskPacket.class);
        ChunkPregenerator.networking.registerPacket(DeletionTaskPacket.class);
        ChunkPregenerator.networking.registerPacket(DimensionTaskPacket.class);
        ChunkPregenerator.networking.registerPacket(ProcessRequestPacket.class);
        ChunkPregenerator.networking.registerPacket(ProcessAnswerPacket.class);
        ChunkPregenerator.networking.registerPacket(ManualTaskPacket.class);
        ChunkPregenerator.proxy.init();
    }
    
    @Mod.EventHandler
    public void onServerStopping(final FMLServerStoppingEvent event) {
        ChunkProcessor.INSTANCE.interruptTask(false, true, false);
        DeleteProcessor.INSTANCE.interruptTask();
        GlobalListeners.INSTANCE.clearListeners();
        StructureManager.instance.onServerStopped();
        ServerTracker.INSTANCE.onServerStopped();
        RetrogenHandler.INSTANCE.onServerStopped();
    }
    
    @Mod.EventHandler
    public void onServerLoaded(final FMLServerStartingEvent event) {
        event.registerServerCommand((ICommand)(ChunkPregenerator.pregenCommand = new PregenBaseCommand()));
        final TaskStorage storage = TaskStorage.getFromServer(event.getServer());
        if (event.getServer().func_71264_H()) {
            storage.addListenState(event.getServer(), true);
        }
        if (new File(ChunkPregenerator.pregeneratorFolder, "onWorldCreation.txt").exists()) {
            if (!storage.isCreated()) {
                try {
                    ChunkPregenerator.pregenCommand.getCommand("loadFromFile").execute(new CommandContainer(event.getServer(), event.getServer()), new String[] { "onWorldCreation.txt" });
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                storage.setCreated();
                return;
            }
        }
        else {
            storage.setCreated();
        }
        if (!ConfigManager.shouldContinue) {
            return;
        }
        if (storage.hasTasks()) {
            try {
                GlobalListeners.INSTANCE.addListener(event.getServer());
                ChunkProcessor.INSTANCE.startTask(storage.getNextTask());
            }
            catch (Exception ex) {}
        }
    }
    
    @SubscribeEvent
    public void onWorldLoad(final WorldEvent.Load event) {
        if (new File(ChunkPregenerator.pregeneratorFolder, "onDimensionCreation.txt").exists() && TaskStorage.getFromWorld(event.world).hasNotRanAlready(event.world.field_73011_w.func_177502_q())) {
            ChunkPregenerator.pregenCommand.getCommand("loadFromFile").execute(new CommandContainer.PerWorldContainer(getServer(), getServer(), event.world.field_73011_w.func_177502_q()), new String[] { "onDimensionCreation.txt" });
        }
    }
    
    @SubscribeEvent
    public void onPlayerLoggedIn(final PlayerEvent.PlayerLoggedInEvent evt) {
        if (isOpped(evt.player) && ChunkProcessor.INSTANCE.isRunning() && ChunkPregenerator.proxy.shouldLog() && TaskStorage.getFromServer(getServer()).autoListens(evt.player)) {
            GlobalListeners.INSTANCE.addListener(evt.player);
        }
    }
    
    @SubscribeEvent
    public void onPlayerLoggedOut(final PlayerEvent.PlayerLoggedOutEvent evt) {
        GlobalListeners.INSTANCE.removeListener(evt.player);
    }
    
    public static boolean isOpped(final EntityPlayer player) {
        return getServer().func_71203_ab().func_152596_g(player.func_146103_bH());
    }
    
    public static MinecraftServer getServer() {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }
    
    static {
        ChunkPregenerator.LOGGER = LogManager.getLogger("Chunk Pregenerator");
        ChunkPregenerator.pregenBase = new PregenAPI();
        SERVICE = Executors.newFixedThreadPool(1);
        ChunkPregenerator.fileText = new String[] { "//Pregenerator ExampleFile", "//This File is a Tutorial File that should help the person who uses it to understand the formatting of the loadFromFile command.", "//Before we start a side note: This Example file is only generated when the Config Folder for ChunkPregen is not present.", "//First of all: Whenever a line starts with // that means the line gets skipped completly.", "//On top of that the lines get processed by way of writing. (Top to bottom)", "//When there is a mistake inside of the File the Tool will throw an error and not execute the File until the mistake is being fixed.", "//It will also tell you exactly which line has a mistake and what type of mistake it is.", "//Also if the file is called onWorldCreation.txt it will be automatically executed as soon a world gets created.", "//Also if the file is called onDimensionCreation.txt it will be automatically executed every time a new dimension is loaded. It keeps also track of which one it already ran", "//Also make sure for the onDimensionCreation.txt to use a ~ for the dimension parameter", "//That file will be also executed as the Server with full admin rights.", "//Now to the Valid commands:", "//startradius, startarea, startextention, startregion, startmassradius.", "//The Parameters for each command are equal to the IngameCommand versions which can be detailed explained with the help command.", "//Here is a ruff explanation for each parameter:", "//startradius: <Square/Circle> <CenterX> <CenterZ> <Radius> <Dimension> <TerrainOnly/PostProcessingOnly/BlockingPostProcessing>", "//startarea: <XMin> <ZMin> <XMax> <ZMax> <Dimension> <TerrainOnly/PostProcessingOnly/BlockingPostProcessing>", "//startextention: <Square/Circle> <CenterX> <CenterZ> <MinRadius> <MaxRadius> <Dimension> <TerrainOnly/PostProcessingOnly/BlockingPostProcessing>", "//startregion: <CenterX> <CenterZ> <Dimension> <TerrainOnly/PostProcessingOnly/BlockingPostProcessing>", "//startmassradius: <CenterX> <CenterZ> <Radius> <RadiusCutting> <Dimension> <TerrainOnly/PostProcessingOnly/BlockingPostProcessing>", "//", "//Every Parameter until the Dimension Parameter is required. For startmassradius it ends at RadiusCutting.", "//So you don't have to provide the dimension or the type of generation.", "//Unlike other versions PostProcessing is selected by default.", "//For the Center Parameter you can use the ~ to reference the Players/Servers Position as center", "//or use 's' letter to reference the World Spawn Position.", "//While these are being used there still can be numbers applied on top of that.", "//These count as offsets to the referenced base position.", "//Also center & radius parameter is referring to Chunk Values (16 block steps).", "//If there is a need for usage of a blockPosition use a 'b' letter infront of a number.", "//this can be also combined with Player/WorldSpawn symbols. But the letter 'b' has to be between the offset and the symbol. (Exampe: sb-100 for Negative Values or sb100 for Positive Values)", "//Now to some examples:", "//startradius Square 0 0 100", "//Generates a 100 Chunks around the Center of the World in the Dimension of the Player/Server is in.", "//startradius Square s s 100 -1", "//Generates a 100 Chunks around the Spawn in the Nether.", "//startradius Square 0 0 100 0 TerrainOnly", "//Generates a TerrainOnly 100 Chunk radius around the Center of the World" };
    }
}
