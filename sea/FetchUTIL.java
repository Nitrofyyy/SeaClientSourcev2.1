// 
// Decompiled by Procyon v0.5.36
// 

package sea;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

public class FetchUTIL
{
    static String apiBase;
    static JsonArray js0n;
    public static volatile int subCount;
    static boolean alreadyExecuted;
    
    static {
        FetchUTIL.subCount = 0;
    }
    
    public static String getApiBase() {
        return FetchUTIL.apiBase;
    }
    
    public static int getSubCount() {
        return FetchUTIL.subCount;
    }
    
    public static void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Packages.start1();
                    try {
                        Thread.sleep(2000L);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    
    static class Packages
    {
        static int subCount;
        static int prevSubCount;
        
        public static void start1() {
            try {
                final String json = readUrl("https://www.googleapis.com/youtube/v3/channels?part=statistics&id=UCDO0hEkGSvujLnb3cZb0XCA&fields=items/statistics/subscriberCount&key=AIzaSyDUUfmvtaHY3lQ11CbkF8gplSJSXwgLe2g");
                final Gson gson = new Gson();
                final clientFormateJson page = (clientFormateJson)gson.fromJson(json, (Class)clientFormateJson.class);
                final String raw = page.items.toString();
                final String[] parts = raw.split(":");
                String numberAndExtras = parts[2];
                numberAndExtras = numberAndExtras.replace("\"", "");
                numberAndExtras = numberAndExtras.replace("}", "");
                numberAndExtras = numberAndExtras.replace("]", "");
                Packages.subCount = Integer.parseInt(numberAndExtras);
                FetchUTIL.subCount = Packages.subCount;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        private static String readUrl(final String urlString) throws Exception {
            BufferedReader reader = null;
            try {
                final URL url = new URL(urlString);
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
                final StringBuffer buffer = new StringBuffer();
                final char[] chars = new char[1024];
                int read;
                while ((read = reader.read(chars)) != -1) {
                    buffer.append(chars, 0, read);
                }
                return buffer.toString();
            }
            finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }
        
        static class clientFormateJson
        {
            JsonArray items;
        }
    }
}
