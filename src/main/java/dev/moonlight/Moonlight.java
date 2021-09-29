package dev.moonlight;

import dev.moonlight.misc.ApiCall;
import dev.moonlight.misc.EventListener;
import dev.moonlight.misc.FontHelper;
import dev.moonlight.misc.font.CFontRenderer;
import dev.moonlight.module.Module;
import dev.moonlight.module.ModuleManager;
import dev.moonlight.ui.clickgui.GUI;
import dev.moonlight.ui.hud.HUD;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

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

    private Logger logger;

    private CFontRenderer fontRenderer;

    private ModuleManager moduleManager;

    private HUD hud;

    private GUI gui;

    @SuppressWarnings("all")
    @ApiCall
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        final long startTime = System.currentTimeMillis();
        logger = LogManager.getLogger(MOD_ID);

        logger.info("Starting {} v{}", MOD_NAME, VERSION);

        fontRenderer = new CFontRenderer(FontHelper.getFontFromResource("/assets/moonlight/Jetbrains_Mono.ttf", 18.0f), true, true);
        moduleManager = new ModuleManager();
        hud = new HUD(this);
        gui = new GUI(this);

        MinecraftForge.EVENT_BUS.register(new EventListener(this));

        logger.debug("Running " + GL11.glGetString(GL11.GL_VERSION));
        logger.info("Completed initialization! ({} seconds)", (System.currentTimeMillis() - startTime) / 1000.0);
    }

    public Logger getLogger() {
        return logger;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public CFontRenderer getFontRenderer() {
        return fontRenderer;
    }

    public GUI getGui() {
        return gui;
    }

    public HUD getHud() {
        return hud;
    }
}