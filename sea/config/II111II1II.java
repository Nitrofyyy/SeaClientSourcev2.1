// 
// Decompiled by Procyon v0.5.36
// 

package sea.config;

import java.io.IOException;
import java.util.Map;
import org.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.Charsets;
import java.io.File;

public class II111II1II
{
    public static III1ii1i1 loadExistingConfiguration(final File file) throws IOException {
        final JSONObject jsonObject = new JSONObject(FileUtils.readFileToString(file, Charsets.UTF_8));
        return new III1ii1i1(file, jsonObject.toMap());
    }
    
    public static III1ii1i1 newConfiguration(final File file) {
        return new III1ii1i1(file);
    }
}
