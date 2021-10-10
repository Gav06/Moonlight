package dev.moonlight.ui.clickgui.settings;

import dev.moonlight.settings.impl.StringSetting;
import dev.moonlight.ui.clickgui.api.SettingComponent;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;

//somewhat phobos i had to skid a lot of the methods for the actual string manipulation stuff
public class StringComponent extends SettingComponent {
    private final StringSetting setting;

    CurrentString currentString = new CurrentString("");
    boolean isTyping = false;

    public StringComponent(StringSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void click(int mouseX, int mouseY, int mouseButton) {
        if(isInside(mouseX, mouseY) && mouseButton == 0) {
            isTyping = !isTyping;
        }
    }

    @Override
    public void release(int mouseX, int mouseY, int mouseButton) { }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (isInside(mouseX, mouseY)) {
            Gui.drawRect(x, y, x + width, y + height, 0x20ffffff);
        }
        final StringBuilder sb = new StringBuilder(setting.getName() + ":");
        if(isTyping) {
            sb.append(currentString);
        }else {
            sb.append(setting.getValue());
        }
        cfont.drawStringWithShadow(sb.toString(), x + 2f, y + (height / 2f) - (cfont.getHeight() / 2f) - 1f, -1);
    }

    @Override
    public void typed(char keyChar, int keyCode) {
        if(isTyping) {
            if (keyCode == 1) {
                return;
            }
            if (keyCode == 28) {
                this.enterString();
            } else if (keyCode == 14) {
                this.setString(removeLastChar(this.currentString.getString()));
            } else if (keyCode == 47 && (Keyboard.isKeyDown((int)157) || Keyboard.isKeyDown((int)29))) {
                try {
                    this.setString(this.currentString.getString() + Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ChatAllowedCharacters.isAllowedCharacter(keyChar)) {
                this.setString(this.currentString.getString() + keyChar);
            }
        }
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    private void enterString() {
        if (this.currentString.getString().isEmpty()) {
            this.setting.setValue(this.setting.getValue());
        } else {
            this.setting.setValue(this.currentString.getString());
        }
        this.setString("");
    }

    public void setString(String newString) {
        this.currentString = new CurrentString(newString);
    }

    public static String removeLastChar(String str) {
        String output = "";
        if (str != null && str.length() > 0) {
            output = str.substring(0, str.length() - 1);
        }
        return output;
    }

    public static class CurrentString {
        private String string;

        public CurrentString(String string) {
            this.string = string;
        }

        public String getString() {
            return this.string;
        }
    }
}
