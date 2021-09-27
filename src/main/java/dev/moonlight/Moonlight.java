package dev.moonlight;

import dev.moonlight.misc.ApiCall;
import dev.moonlight.misc.EventListener;
import dev.moonlight.misc.FontHelper;
import dev.moonlight.misc.SlickFontRenderer;
import dev.moonlight.module.ModuleManager;
import dev.moonlight.ui.clickgui.GUI;
import dev.moonlight.ui.hud.HUD;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(
        modid = Moonlight.MOD_ID,
        name = Moonlight.MOD_NAME,
        version = Moonlight.VERSION,
        clientSideOnly = true,
        acceptedMinecraftVersions = "[1.12.2]"
)
public final class Moonlight {

    public static final String MOD_ID = "moonlight";
    public static final String MOD_NAME = "Moonlight";
    public static final String VERSION = "1.0";

    /** This is the instance of your mod as created by Forge. It will never be null. */
    @Mod.Instance(MOD_ID)
    public static Moonlight INSTANCE;

    private SlickFontRenderer fontRenderer;

    private ModuleManager moduleManager;

    private HUD hud;

    private GUI gui;

    @ApiCall
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        fontRenderer = new SlickFontRenderer(FontHelper.getFontFromResource("/assets/moonlight/SFUI.ttf", 17.0f));
        moduleManager = new ModuleManager();
        hud = new HUD();
        gui = new GUI();

        MinecraftForge.EVENT_BUS.register(new EventListener(this));
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public SlickFontRenderer getFontRenderer() {
        return fontRenderer;
    }

    public GUI getGui() {
        return gui;
    }

    public HUD getHud() {
        return hud;
    }
}