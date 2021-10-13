package dev.moonlight.module.mods.client;

import dev.moonlight.module.Module;
import dev.moonlight.settings.impl.BoolSetting;
import dev.moonlight.settings.impl.FloatSetting;
import dev.moonlight.settings.impl.ModeSetting;
import dev.moonlight.settings.impl.StringSetting;
import org.lwjgl.input.Keyboard;

@Module.Info(
        name = "GUI",
        desc = "Opens the GUI.",
        category = Module.Category.Client,
        bind = Keyboard.KEY_U
)
public final class GUI extends Module {
    public BoolSetting othersParent = new BoolSetting("Others", false, true);
    public final ModeSetting backgroundMode = new ModeSetting("BackgroundMode", BackgroundMode.None, () -> othersParent.getValue());

    public enum BackgroundMode {Darken, Blur, None}

    public final BoolSetting descriptions = new BoolSetting("Descriptions", true, false, () -> othersParent.getValue());
    public BoolSetting leftBackgroundparent = new BoolSetting("LeftBackground", false, true);
    public final FloatSetting lbgr = new FloatSetting("LEFT-BG-R", 0, 0, 255, () -> leftBackgroundparent.getValue());
    public final FloatSetting lbgg = new FloatSetting("LEFT-BG-G", 0, 0, 255, () -> leftBackgroundparent.getValue());
    public final FloatSetting lbgb = new FloatSetting("LEFT-BG-B", 0, 0, 255, () -> leftBackgroundparent.getValue());
    public final FloatSetting lbga = new FloatSetting("LEFT-BG-A", 200, 0, 255, () -> leftBackgroundparent.getValue());

    public BoolSetting rightBackgroundParent = new BoolSetting("RightBackground", false, true);
    public final FloatSetting rbgr = new FloatSetting("RIGHT-BG-R", 0, 0, 255, () -> rightBackgroundParent.getValue());
    public final FloatSetting rbgg = new FloatSetting("RIGHT-BG-G", 0, 0, 255, () -> rightBackgroundParent.getValue());
    public final FloatSetting rbgb = new FloatSetting("RIGHT-BG-B", 0, 0, 255, () -> rightBackgroundParent.getValue());
    public final FloatSetting rbga = new FloatSetting("RIGHT-BG-A", 200, 0, 255, () -> rightBackgroundParent.getValue());

    public BoolSetting outlineParent = new BoolSetting("Outline", false, true);
    public final FloatSetting r = new FloatSetting("OUTLINE-R", 255, 0, 255, () -> outlineParent.getValue());
    public final FloatSetting g = new FloatSetting("OUTLINE-G", 0, 0, 255, () -> outlineParent.getValue());
    public final FloatSetting b = new FloatSetting("OUTLINE-B", 0, 0, 255, () -> outlineParent.getValue());
    public final FloatSetting a = new FloatSetting("OUTLINE-A", 255, 0, 255, () -> outlineParent.getValue());

    @Override
    protected void onEnable() {
        mc.displayGuiScreen(moonlight.getGui());
        disable();
    }
}
