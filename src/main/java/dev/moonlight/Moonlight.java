package dev.moonlight;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.moonlight.event.EventListener;
import dev.moonlight.friend.FriendManager;
import dev.moonlight.misc.ApiCall;
import dev.moonlight.misc.FontHelper;
import dev.moonlight.misc.font.CFontRenderer;
import dev.moonlight.module.ModuleManager;
import dev.moonlight.ui.clickgui.ClickGUI;
import dev.moonlight.ui.clickgui.GUI;
import dev.moonlight.ui.hud.HUD;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

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
    public static final String DISPLAY_MOD_NAME = ChatFormatting.GRAY + "Moon" + ChatFormatting.RESET + "light";
    public static final String VERSION = "1.0";

    /** This is the instance of your mod as created by Forge. It will never be null. */
    @Mod.Instance(MOD_ID)
    public static Moonlight INSTANCE;
    private Logger logger;
    private CFontRenderer fontRenderer;
    private ModuleManager moduleManager;
    private HUD hud;
    private GUI gui;
    private ClickGUI clickGUI;
    private FriendManager friendManager;

    @SuppressWarnings("all")
    @ApiCall
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        final long startTime = System.currentTimeMillis();
        logger = LogManager.getLogger(MOD_ID);

        logger.info("Starting {} v{}", MOD_NAME, VERSION);

        fontRenderer = new CFontRenderer(FontHelper.getFontFromResource("/assets/moonlight/Jetbrains_Mono.ttf", 18.0f), true, true);
        friendManager = new FriendManager();
        moduleManager = new ModuleManager();
        hud = new HUD(this);
        gui = new GUI(this);
        clickGUI = new ClickGUI(this);

        MinecraftForge.EVENT_BUS.register(new EventListener(this));

        logger.info("Completed initialization! ({} seconds)", (System.currentTimeMillis() - startTime) / 1000.0);

        final InputStream stream16 = getClass().getResourceAsStream("/assets/moonlight/icon_16.png");
        final InputStream stream32 = getClass().getResourceAsStream("/assets/moonlight/icon_32.png");

        if (stream16 != null && stream32 != null) {
            Display.setIcon(new ByteBuffer[] {readImageToBuffer(stream16), readImageToBuffer(stream32)});
        }

        Display.setTitle(MOD_NAME + " | " + VERSION);
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

    public ClickGUI getClickGUI() {
        return clickGUI;
    }

    public HUD getHud() {
        return hud;
    }

    private ByteBuffer readImageToBuffer(InputStream imageStream)
    {
        BufferedImage bufferedimage = null;
        try {
            bufferedimage = ImageIO.read(imageStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), (int[])null, 0, bufferedimage.getWidth());
        ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);

        for (int i : aint)
        {
            bytebuffer.putInt(i << 8 | i >> 24 & 255);
        }

        bytebuffer.flip();
        return bytebuffer;
    }

    public FriendManager getFriendManager() {
        return friendManager;
    }
}