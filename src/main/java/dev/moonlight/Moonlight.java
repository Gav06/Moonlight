package dev.moonlight;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(
        modid = Moonlight.MOD_ID,
        name = Moonlight.MOD_NAME,
        version = Moonlight.VERSION,
        clientSideOnly = true,
        acceptedMinecraftVersions = "[1.12.2]"
)
public class Moonlight {

    public static final String MOD_ID = "moonlight";
    public static final String MOD_NAME = "Moonlight";
    public static final String VERSION = "1.0";

    /** This is the instance of your mod as created by Forge. It will never be null. */
    @Mod.Instance(MOD_ID)
    public static Moonlight INSTANCE;

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }
}
