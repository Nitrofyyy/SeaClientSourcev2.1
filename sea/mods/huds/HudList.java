// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.huds;

import sea.event.impl.EventUpdate;
import java.util.Iterator;
import sea.mods.huds.impl.MotionBlur;
import sea.mods.huds.impl.ReachDisplay;
import sea.mods.huds.impl.Scoreboard;
import sea.mods.huds.impl.CustomCrosshair;
import sea.mods.huds.impl.ComboDisplay;
import sea.mods.huds.impl.AutoTOXIC;
import sea.mods.huds.impl.AutoGG;
import sea.mods.huds.impl.ZoomAnimations;
import sea.mods.huds.impl.TimeChanger;
import sea.mods.huds.impl.BlockOverlay;
import sea.mods.huds.impl.ItemPhysics;
import sea.mods.huds.impl.CompactChat;
import sea.mods.huds.impl.TntTimer;
import sea.mods.huds.impl.TargetHud;
import sea.mods.huds.impl.ServerIPHud;
import sea.mods.huds.impl.Animations;
import sea.mods.huds.impl.ModPerspectiveHud;
import sea.mods.huds.impl.PackDisplay;
import sea.mods.huds.impl.ToggleSprintHud;
import sea.mods.huds.impl.MiniPlayer;
import sea.mods.huds.impl.FullBright;
import sea.mods.huds.impl.ModPotionStatus;
import sea.mods.huds.impl.ModKeystrokes;
import sea.mods.huds.impl.PingMod;
import sea.mods.huds.impl.ArmorStatusMod;
import sea.mods.huds.impl.DirectionMod;
import sea.mods.huds.impl.CPSMod;
import sea.mods.huds.impl.ClockMod;
import sea.mods.huds.impl.FPSMod;
import java.util.ArrayList;

public class HudList
{
    public ArrayList<HudMod> hudMods;
    public FPSMod FPSMod;
    public static HudList INSTANCE;
    public ClockMod ClockMod;
    public CPSMod CPSMod;
    public DirectionMod DirectionMod;
    public ArmorStatusMod armor;
    public PingMod ping;
    public ModKeystrokes key;
    public ModPotionStatus pot;
    public FullBright fullbright;
    public MiniPlayer mini;
    public static ToggleSprintHud sprint;
    public PackDisplay pack;
    public ModPerspectiveHud freelook;
    public static Animations oldanime;
    public ServerIPHud serverdisplay;
    public TargetHud heartDisplay;
    public static TntTimer tntTimer;
    public static CompactChat chat;
    public static ItemPhysics item;
    public static BlockOverlay blockoverlay;
    public static TimeChanger timechanger;
    public static ZoomAnimations zoom;
    public static AutoGG gg;
    public static AutoTOXIC toxic;
    public static ComboDisplay combo;
    public static CustomCrosshair crosshair;
    public static Scoreboard score;
    public static ReachDisplay reach;
    public static MotionBlur motion;
    public static HudList getInstance;
    
    public HudList() {
        (this.hudMods = new ArrayList<HudMod>()).add(this.ClockMod = new ClockMod());
        this.hudMods.add(this.FPSMod = new FPSMod());
        this.hudMods.add(this.CPSMod = new CPSMod());
        this.hudMods.add(this.DirectionMod = new DirectionMod());
        this.hudMods.add(this.armor = new ArmorStatusMod());
        this.hudMods.add(this.ping = new PingMod());
        this.hudMods.add(this.pot = new ModPotionStatus());
        this.hudMods.add(this.mini = new MiniPlayer());
        this.hudMods.add(this.key = new ModKeystrokes());
        this.hudMods.add(HudList.sprint = new ToggleSprintHud());
        this.hudMods.add(this.fullbright = new FullBright());
        this.hudMods.add(this.pack = new PackDisplay());
        this.hudMods.add(this.freelook = new ModPerspectiveHud());
        this.hudMods.add(this.serverdisplay = new ServerIPHud());
        this.hudMods.add(this.heartDisplay = new TargetHud());
        this.hudMods.add(HudList.tntTimer = new TntTimer());
        this.hudMods.add(HudList.oldanime = new Animations());
        this.hudMods.add(HudList.chat = new CompactChat());
        this.hudMods.add(HudList.item = new ItemPhysics());
        this.hudMods.add(HudList.zoom = new ZoomAnimations());
        this.hudMods.add(HudList.timechanger = new TimeChanger());
        this.hudMods.add(HudList.gg = new AutoGG());
        this.hudMods.add(HudList.toxic = new AutoTOXIC());
        this.hudMods.add(HudList.combo = new ComboDisplay());
        this.hudMods.add(HudList.reach = new ReachDisplay());
        this.hudMods.add(HudList.score = new Scoreboard());
        this.hudMods.add(HudList.crosshair = new CustomCrosshair());
        this.hudMods.add(HudList.motion = new MotionBlur());
        this.hudMods.add(HudList.blockoverlay = new BlockOverlay());
    }
    
    public void renderMod() {
        for (final HudMod hm : this.hudMods) {
            if (hm.isEnabled()) {
                hm.draw();
            }
        }
    }
    
    public static CustomCrosshair getCrosshair() {
        return HudList.crosshair;
    }
    
    public static ComboDisplay getCombo() {
        return HudList.combo;
    }
    
    public static Scoreboard getScore() {
        return HudList.score;
    }
    
    public static AutoTOXIC getToxic() {
        return HudList.toxic;
    }
    
    public static MotionBlur getMotion() {
        return HudList.motion;
    }
    
    public static TimeChanger getTimechanger() {
        return HudList.timechanger;
    }
    
    public static ZoomAnimations getZoom() {
        return HudList.zoom;
    }
    
    public static Animations getOldanime() {
        return HudList.oldanime;
    }
    
    public static ItemPhysics getItem() {
        return HudList.item;
    }
    
    public static AutoGG getGg() {
        return HudList.gg;
    }
    
    public static BlockOverlay getBlockoverlay() {
        return HudList.blockoverlay;
    }
    
    public static CompactChat getChat() {
        return HudList.chat;
    }
    
    public static TntTimer tntTimer() {
        return HudList.tntTimer;
    }
    
    public static HudList getInstance() {
        return HudList.INSTANCE;
    }
    
    public EventUpdate[] getRegisteredRenderers() {
        return null;
    }
}
