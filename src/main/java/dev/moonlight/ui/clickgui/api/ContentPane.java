package dev.moonlight.ui.clickgui.api;

import java.util.ArrayList;

public class ContentPane extends AbstractComponent {

    private final ArrayList<AbstractComponent> components;

    public ContentPane(int x, int y, int width, int height) {
        super(x, y, width, height);
        components = new ArrayList<>();
    }

    @Override
    public void click(int mouseX, int mouseY, int mouseButton) {
        for (AbstractComponent component : components) {
            if (component.isVisible()) {
                component.click(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void release(int mouseX, int mouseY, int mouseButton) {
        for (AbstractComponent component : components) {
            if (component.isVisible()) {
                component.release(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        int yVal = 0;
        for (AbstractComponent component : components) {
            if (component.isVisible()) {
                component.x = x;
                component.y = y + yVal;
                yVal += component.height;
                component.draw(mouseX, mouseY, partialTicks);
            }
        }
    }

    @Override
    public void typed(char keyChar, int keyCode) {
        for (AbstractComponent component : components) {
            if (component.isVisible()) {
                component.typed(keyChar, keyCode);
            }
        }
    }

    public ArrayList<AbstractComponent> getComponents() {
        return components;
    }
}