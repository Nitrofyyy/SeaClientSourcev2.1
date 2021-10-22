// 
// Decompiled by Procyon v0.5.36
// 

package sea;

import sea.event.EventTarget;
import java.lang.reflect.InvocationTargetException;
import net.minecraft.client.gui.GuiScreen;
import sea.mods.huds.HUDConfigScreen;
import sea.event.impl.ClientTickEvent;
import org.lwjgl.opengl.Display;
import net.minecraft.entity.Entity;
import net.minecraft.util.IChatComponent;
import net.minecraft.client.Minecraft;
import sea.login.AltManager;
import sea.event.entity.EntityJoinWorldEvent;
import sea.mods.ModList;
import pregenerator.ChunkPregenerator;
import sea.utils.font.FontUtils;
import sea.mods.huds.settings.Settings;
import sea.mods.huds.settings.color.ColorList;
import sea.mods.huds.HudList;
import sea.config.FileManager2;
import sea.config.FileManager;
import sea.event.EventManager;

public class SeaClient
{
    public static String name;
    public static String version;
    public static String author;
    public static EventManager eventManager;
    public FileManager config;
    public FileManager2 config2;
    public HudList hudList;
    public ColorList colorList;
    public Settings settings;
    public FontUtils font;
    String customfontSkiddedfrom;
    public ChunkPregenerator chunkpreg;
    public static ModList modList;
    private EntityJoinWorldEvent event;
    public boolean animations;
    public static AltManager altManager;
    public Minecraft mc;
    public static String nameverplusauthor;
    public static SeaClient INSTANCE;
    public DiscordRP discord;
    public Test test;
    private IChatComponent updateMessage;
    private int messageDelay;
    static Entity entity;
    
    static {
        SeaClient.name = "SeaClient";
        SeaClient.version = " v2 ";
        SeaClient.author = "BLueDD ";
        SeaClient.nameverplusauthor = String.valueOf(SeaClient.name) + SeaClient.version + SeaClient.author;
        SeaClient.INSTANCE = new SeaClient();
    }
    
    public SeaClient() {
        this.customfontSkiddedfrom = "quick";
        this.mc = Minecraft.getMinecraft();
        this.messageDelay = 0;
    }
    
    public static SeaClient getINSTANCE() {
        return SeaClient.INSTANCE;
    }
    
    public void start() {
        (this.test = new Test()).test(this.chunkpreg);
        this.chunkpreg = new ChunkPregenerator();
        this.config = new FileManager();
        (this.config2 = new FileManager2()).loadAccountConfig();
        this.config.loadModConfig();
        SeaClient.eventManager = new EventManager();
        (this.discord = new DiscordRP()).start();
        this.hudList = new HudList();
        this.colorList = new ColorList();
        this.settings = new Settings();
        SeaClient.altManager = new AltManager();
        SeaClient.modList = new ModList();
        this.font = new FontUtils();
        FontUtils.bootstrap();
        Display.setTitle(SeaClient.nameverplusauthor);
        EventManager.register(this);
    }
    
    public void shutdown() {
        System.out.println("shutdown");
        this.discord.shutdown();
        this.config.saveModConfig();
        this.config2.saveAccountConfig();
        EventManager.unregister(this);
        this.mc.shutdown();
    }
    
    public DiscordRP getDiscord() {
        return this.discord;
    }
    
    @EventTarget
    public void onTick(final ClientTickEvent event) throws InvocationTargetException {
        if (this.updateMessage != null && this.mc.thePlayer != null && ++this.messageDelay == 80) {
            this.mc.thePlayer.addChatMessage(this.updateMessage);
            this.updateMessage = null;
        }
        if (HudList.getBlockoverlay().isDisabled()) {
            final ColorList colorList = SeaClient.INSTANCE.colorList;
            ColorList.getBlockaqua().setEnabled(false);
            final ColorList colorList2 = SeaClient.INSTANCE.colorList;
            ColorList.getBlockpink().setEnabled(false);
            final ColorList colorList3 = SeaClient.INSTANCE.colorList;
            ColorList.getBlockgreen().setEnabled(false);
            final ColorList colorList4 = SeaClient.INSTANCE.colorList;
            ColorList.getBlockblue().setEnabled(false);
            final ColorList colorList5 = SeaClient.INSTANCE.colorList;
            ColorList.getBlockyellow().setEnabled(false);
        }
        if (HudList.getTimechanger().isDisabled()) {
            final Settings settings = getINSTANCE().settings;
            Settings.time1.setEnabled(false);
            final Settings settings2 = getINSTANCE().settings;
            Settings.time2.setEnabled(false);
            final Settings settings3 = getINSTANCE().settings;
            Settings.time3.setEnabled(false);
        }
        if (this.mc.gameSettings.HUD_CONFIG.isPressed()) {
            this.mc.displayGuiScreen(new HUDConfigScreen());
        }
        if (this.mc.gameSettings.FREELOOK.isPressed() && this.hudList.freelook.isEnabled()) {
            ModList.perspective.toggle();
        }
        if (this.hudList.fullbright.isEnabled()) {
            SeaClient.modList.full.onEnable();
        }
        else {
            SeaClient.modList.full.onDisable();
        }
        if (this.mc.gameSettings.TOGGLE_SPRINT.isPressed() && HudList.sprint.isEnabled()) {
            SeaClient.modList.sprint.toggle();
        }
    }
}
