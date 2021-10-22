// 
// Decompiled by Procyon v0.5.36
// 

package sea.config;

import java.io.IOException;
import java.io.FileWriter;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

public class III1ii1i1
{
    private File file;
    public Map options;
    
    public III1ii1i1(final File file, final Map options) {
        this.file = file;
        this.options = options;
    }
    
    public III1ii1i1(final File file) {
        this.file = file;
        this.options = new HashMap();
    }
    
    public Object get(final String key) {
        return this.options.get(key);
    }
    
    public void set(final String key, final Object value) {
        this.options.put(key, value);
    }
    
    public void save() throws IOException {
        final JSONObject jsonObject = new JSONObject(this.options);
        this.file.createNewFile();
        final FileWriter fileWriter = new FileWriter(this.file);
        fileWriter.write(jsonObject.toString());
        fileWriter.flush();
        fileWriter.close();
    }
}
