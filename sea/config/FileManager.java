// 
// Decompiled by Procyon v0.5.36
// 

package sea.config;

import java.util.Iterator;
import java.io.IOException;
import sea.mods.huds.HudMod;
import sea.SeaClient;
import java.io.File;

public class FileManager
{
    public File configFolder;
    public File modsFolder;
    public III1ii1i1 config;
    public III1ii1i1 III1II1I;
    
    public FileManager() {
        this.configFolder = new File("SeaMods");
        this.modsFolder = new File("SeaMods/Mods");
        this.III1II1I = II111II1II.newConfiguration(new File("SeaMods/Mods/Mods.json"));
    }
    
    public void saveModConfig() {
        if (!this.configFolder.exists()) {
            this.configFolder.mkdirs();
        }
        if (!this.modsFolder.exists()) {
            this.modsFolder.mkdirs();
        }
        System.out.println("Saving Mod Config");
        for (final HudMod e : SeaClient.INSTANCE.hudList.hudMods) {
            this.III1II1I.set(String.valueOf(String.valueOf(e.name)) + " x", e.getX());
            this.III1II1I.set(String.valueOf(String.valueOf(e.name)) + " y", e.getY());
            this.III1II1I.set(String.valueOf(String.valueOf(e.name)) + " enabled", e.isEnabled());
        }
        try {
            this.III1II1I.save();
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
    
    public void loadModConfig() {
        try {
            System.out.println("Loading Mod Config");
            this.config = II111II1II.loadExistingConfiguration(new File("SeaClient/Mods/Mods.json"));
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
}
