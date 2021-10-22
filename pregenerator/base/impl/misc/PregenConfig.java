// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.base.impl.misc;

import java.io.File;
import net.minecraftforge.common.config.Configuration;
import pregenerator.base.api.misc.IConfig;

public class PregenConfig implements IConfig
{
    boolean massChange;
    Configuration config;
    
    public PregenConfig(final File file) {
        this.massChange = true;
        this.config = new Configuration(file);
    }
    
    @Override
    public boolean getBoolean(final String category, final String name, final boolean value) {
        return this.config.get(category, name, value).getBoolean();
    }
    
    @Override
    public boolean getBoolean(final String category, final String name, final boolean value, final String comment) {
        return this.config.get(category, name, value, comment).getBoolean();
    }
    
    @Override
    public void setBoolean(final String category, final String name, final boolean value) {
        if (!this.massChange) {
            this.config.load();
            this.config.get(category, name, value).set(value);
            this.config.save();
        }
        else {
            this.config.get(category, name, value).set(value);
        }
    }
    
    @Override
    public int getInt(final String category, final String name, final int value) {
        return this.config.get(category, name, value).getInt();
    }
    
    @Override
    public int getInt(final String category, final String name, final int value, final String comment) {
        return this.config.get(category, name, value, comment).getInt();
    }
    
    @Override
    public int getInt(final String category, final String name, final int min, final int max, final int value, final String comment) {
        return this.config.getInt(name, category, value, min, max, comment);
    }
    
    @Override
    public void setInt(final String category, final String name, final int value) {
        if (!this.massChange) {
            this.config.load();
            this.config.get(category, name, value).set(value);
            this.config.save();
        }
        else {
            this.config.get(category, name, value).set(value);
        }
    }
    
    @Override
    public String getString(final String category, final String name, final String value) {
        return this.config.get(category, name, value).getString();
    }
    
    @Override
    public String getString(final String category, final String name, final String value, final String comment) {
        return this.config.get(category, name, value, comment).getString();
    }
    
    @Override
    public void setString(final String category, final String name, final String value) {
        if (!this.massChange) {
            this.config.load();
            this.config.get(category, name, value).set(value);
            this.config.save();
        }
        else {
            this.config.get(category, name, value).set(value);
        }
    }
    
    @Override
    public void prepaireMassChange() {
        this.massChange = true;
        this.config.load();
    }
    
    @Override
    public void saveConfig() {
        this.massChange = false;
        this.config.save();
    }
}
