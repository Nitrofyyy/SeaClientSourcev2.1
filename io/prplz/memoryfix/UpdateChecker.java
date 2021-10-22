// 
// Decompiled by Procyon v0.5.36
// 

package io.prplz.memoryfix;

import java.io.IOException;
import java.io.Reader;
import java.io.InputStreamReader;
import net.minecraftforge.common.ForgeVersion;
import java.net.URL;
import java.net.HttpURLConnection;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.TypeAdapterFactory;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import java.util.function.Consumer;

public class UpdateChecker extends Thread
{
    private final String url;
    private final Consumer<UpdateResponse> callback;
    private final Gson gson;
    
    public UpdateChecker(final String url, final Consumer<UpdateResponse> callback) {
        this.gson = new GsonBuilder().registerTypeHierarchyAdapter((Class)IChatComponent.class, (Object)new IChatComponent.Serializer()).registerTypeHierarchyAdapter((Class)ChatStyle.class, (Object)new ChatStyle.Serializer()).registerTypeAdapterFactory((TypeAdapterFactory)new EnumTypeAdapterFactory()).setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        this.url = url;
        this.callback = callback;
    }
    
    @Override
    public void run() {
        for (int retry = 0; retry < 3; ++retry) {
            try {
                final UpdateResponse response = this.check(this.url);
                try {
                    this.callback.accept(response);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            catch (Exception ex2) {
                System.out.println("GET " + this.url + " failed:");
                System.out.println(ex2.toString());
                try {
                    Thread.sleep(10000L);
                }
                catch (InterruptedException interrupted) {
                    return;
                }
                continue;
            }
            return;
        }
    }
    
    private UpdateResponse check(final String url) throws IOException {
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection)new URL(url).openConnection();
            con.setRequestMethod("GET");
            final String agent = "Java/" + System.getProperty("java.version") + " " + "Forge/" + ForgeVersion.getVersion() + " " + System.getProperty("os.name") + " " + System.getProperty("os.arch") + " ";
            con.setRequestProperty("User-Agent", agent);
            final int response = con.getResponseCode();
            if (response == 200) {
                try (final InputStreamReader in = new InputStreamReader(con.getInputStream(), "UTF-8")) {
                    return (UpdateResponse)this.gson.fromJson((Reader)in, (Class)UpdateResponse.class);
                }
            }
            throw new IOException("HTTP " + response);
        }
        finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }
    
    public static class UpdateResponse
    {
        private final IChatComponent updateMessage;
        
        public UpdateResponse(final IChatComponent updateMessage) {
            this.updateMessage = updateMessage;
        }
        
        public IChatComponent getUpdateMessage() {
            return this.updateMessage;
        }
    }
}
