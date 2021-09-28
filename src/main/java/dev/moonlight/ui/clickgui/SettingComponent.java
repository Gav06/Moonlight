package dev.moonlight.ui.clickgui;

import dev.moonlight.Moonlight;
import dev.moonlight.misc.font.CFontRenderer;
import dev.moonlight.ui.clickgui.api.AbstractComponent;

public abstract class SettingComponent extends AbstractComponent {

    protected final CFontRenderer cfont = Moonlight.INSTANCE.getFontRenderer();

    public SettingComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }
}
