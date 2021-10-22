// 
// Decompiled by Procyon v0.5.36
// 

package sea.login;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import sea.SeaClient;
import java.io.IOException;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiAddAlt extends GuiScreen
{
    private final GuiAltManager manager;
    private PasswordField password;
    private String status;
    private GuiTextField username;
    
    public GuiAddAlt(final GuiAltManager manager) {
        this.status = EnumChatFormatting.GRAY + "Idle...";
        this.manager = manager;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0: {
                final AddAltThread login = new AddAltThread(this.username.getText(), this.password.getText());
                login.start();
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(this.manager);
                break;
            }
        }
    }
    
    @Override
    public void drawScreen(final int i2, final int j2, final float f2) {
        this.drawDefaultBackground();
        this.username.drawTextBox();
        this.password.drawTextBox();
        Gui.drawCenteredString(this.fontRendererObj, "Add Alt", (float)(GuiAddAlt.width / 2), 20.0f, -1);
        if (this.username.getText().isEmpty()) {
            this.drawString(Minecraft.fontRendererObj, "Username / E-Mail", GuiAddAlt.width / 2 - 96, 66, -7829368);
        }
        if (this.password.getText().isEmpty()) {
            this.drawString(Minecraft.fontRendererObj, "Password", GuiAddAlt.width / 2 - 96, 106, -7829368);
        }
        Gui.drawCenteredString(this.fontRendererObj, this.status, (float)(GuiAddAlt.width / 2), 30.0f, -1);
        super.drawScreen(i2, j2, f2);
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, GuiAddAlt.width / 2 - 100, GuiAddAlt.height / 4 + 92 + 12, "Login"));
        this.buttonList.add(new GuiButton(1, GuiAddAlt.width / 2 - 100, GuiAddAlt.height / 4 + 116 + 12, "Back"));
        this.username = new GuiTextField(this.eventButton, Minecraft.fontRendererObj, GuiAddAlt.width / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(Minecraft.fontRendererObj, GuiAddAlt.width / 2 - 100, 100, 200, 20);
    }
    
    @Override
    protected void keyTyped(final char par1, final int par2) {
        this.username.textboxKeyTyped(par1, par2);
        this.password.textboxKeyTyped(par1, par2);
        if (par1 == '\t' && (this.username.isFocused() || this.password.isFocused())) {
            this.username.setFocused(!this.username.isFocused());
            this.password.setFocused(!this.password.isFocused());
        }
        if (par1 == '\r') {
            this.actionPerformed(this.buttonList.get(0));
        }
    }
    
    @Override
    protected void mouseClicked(final int par1, final int par2, final int par3) {
        try {
            super.mouseClicked(par1, par2, par3);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.username.mouseClicked(par1, par2, par3);
        this.password.mouseClicked(par1, par2, par3);
    }
    
    static void access$0(final GuiAddAlt guiAddAlt, final String status) {
        guiAddAlt.status = status;
    }
    
    private class AddAltThread extends Thread
    {
        private String password;
        private String username;
        
        public AddAltThread(final String username, final String password) {
            this.username = username;
            this.password = password;
            try {
                this.username = (String)SeaClient.INSTANCE.config2.config.get(String.valueOf("username: " + username));
                this.password = (String)SeaClient.INSTANCE.config2.config.get(String.valueOf("password: " + password));
            }
            catch (NullPointerException e) {
                e.printStackTrace();
                this.username = username;
                this.password = password;
            }
            GuiAddAlt.access$0(GuiAddAlt.this, EnumChatFormatting.GRAY + "Idle...");
        }
        
        private final void checkAndAddAlt(final String username, final String password) throws IOException {
            final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
            final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
            auth.setUsername(username);
            auth.setPassword(password);
            try {
                auth.logIn();
                final AltManager altManager = SeaClient.altManager;
                AltManager.registry.add(new Alt(username, password, auth.getSelectedProfile().getName()));
                this.username = (String)SeaClient.INSTANCE.config2.config.get(String.valueOf("username: " + username));
                this.password = (String)SeaClient.INSTANCE.config2.config.get(String.valueOf("password: " + password));
                GuiAddAlt.access$0(GuiAddAlt.this, "Alt added. (" + username + ")");
            }
            catch (AuthenticationException e) {
                GuiAddAlt.access$0(GuiAddAlt.this, EnumChatFormatting.RED + "Alt failed!");
                e.printStackTrace();
            }
        }
        
        @Override
        public void run() {
            if (this.password.equals("")) {
                final AltManager altManager = SeaClient.altManager;
                AltManager.registry.add(new Alt(this.username, ""));
                GuiAddAlt.access$0(GuiAddAlt.this, EnumChatFormatting.GREEN + "Alt added. (" + this.username + " - offline name)");
                return;
            }
            GuiAddAlt.access$0(GuiAddAlt.this, EnumChatFormatting.YELLOW + "Trying alt...");
            try {
                this.checkAndAddAlt(this.username, this.password);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
