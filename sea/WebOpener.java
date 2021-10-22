// 
// Decompiled by Procyon v0.5.36
// 

package sea;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.awt.Desktop;

public class WebOpener
{
    public static void openLink(final String url) {
        final Desktop desktop = Desktop.getDesktop();
        if (desktop != null) {
            try {
                desktop.browse(new URL(url).toURI());
            }
            catch (URISyntaxException | IOException ex2) {
                final Exception ex;
                final Exception ioexception = ex;
                ioexception.printStackTrace();
            }
        }
    }
}
