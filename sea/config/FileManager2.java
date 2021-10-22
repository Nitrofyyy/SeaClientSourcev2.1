// 
// Decompiled by Procyon v0.5.36
// 

package sea.config;

import sea.login.Alt;
import java.util.Iterator;
import java.io.IOException;
import sea.login.AltManager;
import sea.SeaClient;
import java.io.File;

public class FileManager2
{
    public File configFolder;
    public File accountFolder;
    public III1ii1i1 config;
    public III1ii1i1 III1II1I;
    
    public FileManager2() {
        this.configFolder = new File("SeaAccounts");
        this.accountFolder = new File("SeaAccounts/Accounts");
        this.III1II1I = II111II1II.newConfiguration(new File("SeaAccounts/Accounts/Account.json"));
    }
    
    public void saveAccountConfig() {
        if (!this.configFolder.exists()) {
            this.configFolder.mkdirs();
        }
        if (!this.accountFolder.exists()) {
            this.accountFolder.mkdirs();
        }
        System.out.println("Saving Account Config");
        final AltManager altManager = SeaClient.altManager;
        final Iterator iterator = AltManager.registry.iterator();
        while (iterator.hasNext()) {
            final Alt selectedAlt = null;
            this.III1II1I.set(String.valueOf(String.valueOf(selectedAlt.getUsername())) + "username: ", String.valueOf(selectedAlt.getUsername()));
            this.III1II1I.set(String.valueOf(String.valueOf(selectedAlt.getPassword())) + "password: ", String.valueOf(selectedAlt.getPassword()));
        }
        try {
            this.III1II1I.save();
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
    
    public void loadAccountConfig() {
        try {
            System.out.println("Loading Account Config");
            this.config = II111II1II.loadExistingConfiguration(new File("SeaClient/Accounts/Accounts.json"));
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
}
