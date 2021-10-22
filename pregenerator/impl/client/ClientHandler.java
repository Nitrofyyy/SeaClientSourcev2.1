// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client;

import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import pregenerator.impl.client.gui.GuiPregenMenu;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraft.client.gui.GuiScreen;
import pregenerator.impl.client.preview.GuiSeedPreview;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraftforge.client.event.GuiScreenEvent;
import java.util.Iterator;
import pregenerator.impl.client.trackerInfo.TrackerEntry;
import pregenerator.impl.client.infos.InfoEntry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import java.io.File;
import net.minecraftforge.common.MinecraftForge;
import pregenerator.ChunkPregenerator;
import pregenerator.base.api.misc.IRenderHelper;
import pregenerator.base.api.misc.IConfig;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientHandler
{
    public static KeyBinding uiKey;
    public static ClientHandler INSTANCE;
    public PregenInfo info;
    public TrackerInfo tracker;
    IConfig clientConfig;
    IRenderHelper helper;
    
    public void init() {
        ChunkPregenerator.pregenBase.registerTickEvent(this);
        MinecraftForge.EVENT_BUS.register((Object)this);
        this.clientConfig = ChunkPregenerator.pregenBase.getConfig(new File(ChunkPregenerator.pregeneratorFolder, "ClientConfig.cfg"));
        ClientRegistry.registerKeyBinding(ClientHandler.uiKey = new KeyBinding("Options Gui", 23, "Chunk-Pregenerator"));
        this.info = new PregenInfo(this.clientConfig);
        this.tracker = new TrackerInfo(this.clientConfig);
        try {
            for (final InfoEntry entry : InfoEntry.getRegistry()) {
                entry.readFromConfig(this.clientConfig);
            }
            this.info.loadConfig();
            for (final TrackerEntry entry2 : TrackerEntry.getRegistry()) {
                entry2.readFromConfig(this.clientConfig);
            }
            this.tracker.loadConfig();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            this.clientConfig.saveConfig();
        }
        this.info.updateList();
        this.tracker.updateList();
        this.helper = ChunkPregenerator.pregenBase.createRenderHelper();
    }
    
    @SubscribeEvent
    public void onGuiOpened(final GuiScreenEvent.InitGuiEvent event) {
        if (event.gui instanceof GuiCreateWorld) {
            final int width = event.gui.field_146294_l / 2;
            event.buttonList.add(new GuiButton(-100, width - 155, 187, 72, 20, "Preview"));
        }
    }
    
    @SubscribeEvent
    public void onButtonPressed(final GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.gui instanceof GuiCreateWorld && event.button.field_146127_k == -100) {
            Minecraft.func_71410_x().func_147108_a((GuiScreen)new GuiSeedPreview((GuiCreateWorld)event.gui));
        }
    }
    
    @SubscribeEvent
    public void onKeyPressed(final InputEvent.KeyInputEvent event) {
        final Minecraft mc = Minecraft.func_71410_x();
        if (ClientHandler.uiKey.func_151470_d() && mc.field_71462_r == null) {
            mc.func_147108_a((GuiScreen)new GuiPregenMenu());
        }
    }
    
    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Post evt) {
        if (evt.type != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        this.info.render(this.helper, evt.resolution);
        this.tracker.render(this.helper, evt.resolution);
    }
    
    @SubscribeEvent
    public void onClientTickEvent(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START || Minecraft.func_71410_x().field_71441_e == null) {
            return;
        }
        this.info.update();
        this.tracker.update();
    }
    
    static {
        ClientHandler.INSTANCE = new ClientHandler();
    }
}
