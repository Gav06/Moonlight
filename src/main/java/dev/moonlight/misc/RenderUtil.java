package dev.moonlight.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public final class RenderUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void outline2d(double x1, double y1, double x2, double y2, int color) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder buffer = tessellator.getBuffer();
        float[] colors = hexToRGBA(color);
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.glLineWidth(1f);
        GlStateManager.color(colors[0], colors[1], colors[2], colors[3]);
        buffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
        buffer.pos(x1, y1, 0.0).endVertex();
        buffer.pos(x2, y1, 0.0).endVertex();
        buffer.pos(x2, y2, 0.0).endVertex();;
        buffer.pos(x1, y2, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    public static float[] hexToRGBA(int color) {
        return new float[] {
                (color >> 16 & 255) / 255.0F,
                (color >> 8 & 255) / 255.0F,
                (color & 255) / 255.0F,
                (color >> 24 & 255) / 255.0F
        };
    }

    public static Vec3d offsetVec(Vec3d vec) {
        return vec.subtract(mc.getRenderManager().viewerPosX, mc.getRenderManager().viewerPosY, mc.getRenderManager().viewerPosZ);
    }

    public static AxisAlignedBB offsetBB(AxisAlignedBB bb) {
        return bb.offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
    }

    public static BlockPos offsetBlockPos(BlockPos pos) {
        return pos.add(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
    }
}
