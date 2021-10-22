// 
// Decompiled by Procyon v0.5.36
// 

package sea.login;

import sea.SeaClient;
import java.util.ArrayList;

public class AltManager
{
    public static Alt lastAlt;
    public static ArrayList<Alt> registry;
    
    static {
        AltManager.registry = new ArrayList<Alt>();
    }
    
    public ArrayList<Alt> getRegistry() {
        return AltManager.registry;
    }
    
    public void setLastAlt(final Alt alt2) {
        AltManager.lastAlt = alt2;
        try {
            AltManager.lastAlt = (Alt)SeaClient.INSTANCE.config2.config.get(String.valueOf("username: " + AltManager.lastAlt));
        }
        catch (Exception e) {
            AltManager.lastAlt = alt2;
        }
    }
}
