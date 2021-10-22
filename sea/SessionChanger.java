// 
// Decompiled by Procyon v0.5.36
// 

package sea;

import net.minecraft.util.Session;
import com.mojang.util.UUIDTypeAdapter;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import net.minecraft.client.Minecraft;
import java.util.UUID;
import com.mojang.authlib.UserAuthentication;

public class SessionChanger
{
    public static SessionChanger instance;
    public final UserAuthentication auth;
    
    public static SessionChanger getInstance() {
        if (SessionChanger.instance == null) {
            SessionChanger.instance = new SessionChanger();
        }
        return SessionChanger.instance;
    }
    
    public SessionChanger() {
        final UUID notSureWhyINeedThis = UUID.randomUUID();
        final YggdrasilAuthenticationService authService = new YggdrasilAuthenticationService(Minecraft.getMinecraft().getProxy(), notSureWhyINeedThis.toString());
        this.auth = authService.createUserAuthentication(Agent.MINECRAFT);
        authService.createMinecraftSessionService();
    }
    
    public void setUser(final String email, final String password) {
        if (!Minecraft.getMinecraft().getSession().getUsername().equals(email) || Minecraft.getMinecraft().getSession().getToken().equals("0")) {
            this.auth.logOut();
            this.auth.setUsername(email);
            this.auth.setPassword(password);
            try {
                this.auth.logIn();
                final Session e = new Session(this.auth.getSelectedProfile().getName(), UUIDTypeAdapter.fromUUID(this.auth.getSelectedProfile().getId()), this.auth.getAuthenticatedToken(), this.auth.getUserType().getName());
                this.setSession(e);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
    
    public void setSession(final Session session) {
        Minecraft.getMinecraft().session = session;
    }
    
    public void setUserOffline(final String username) {
        this.auth.logOut();
        final Session session = new Session(username, username, "0", "legacy");
        this.setSession(session);
    }
}
