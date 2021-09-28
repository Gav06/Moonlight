package dev.moonlight.ui.clickgui.api;

public class DragComponent extends AbstractComponent {
    public DragComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    private boolean dragging;
    private int dragX, dragY;

    @Override
    public void click(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY) && mouseButton == 0) {
            dragging = true;
            dragX = mouseX - x;
            dragY = mouseY - y;
        }
    }

    @Override
    public void release(int mouseX, int mouseY, int mouseButton) {
        dragging = false;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (dragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }
    }

    @Override
    public void typed(char keyChar, int keyCode) { }

}
