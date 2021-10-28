package dev.moonlight.module.mods.render;

import dev.moonlight.module.Module;
import net.minecraft.world.chunk.Chunk;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

@Module.Info(
        name = "NewChunks",
        desc = "Shows new chunks",
        category = Module.Category.Render
)
public class NewChunks extends Module {

    private int list = GL11.glGenLists(1);

    private final ArrayList<Chunk> chunks = new ArrayList<>();


}
