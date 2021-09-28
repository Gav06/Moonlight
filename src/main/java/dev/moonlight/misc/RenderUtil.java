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
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

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

    public static void quad3d(int glMode, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder buffer = tessellator.getBuffer();
        prepareRender();
        buffer.begin(glMode, DefaultVertexFormats.POSITION);
        buffer.pos(x1, y1, z1).endVertex();
        buffer.pos(x2, y2, z2).endVertex();
        buffer.pos(x3, y3, z3).endVertex();
        buffer.pos(x4, y4, z4).endVertex();
        tessellator.draw();
        releaseRender();
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

    public static void prepareRender() {
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableAlpha();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//        GL11.glEnable(GL32.GL_DEPTH_CLAMP);
    }

    public static void releaseRender() {
//        GL11.glDisable(GL32.GL_DEPTH_CLAMP);
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
        GlStateManager.disableAlpha();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    public static void drawSimpleGradientBB(AxisAlignedBB bb, int topColor, int bottomColor, boolean depth) {
        prepareRender();
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder buffer = tessellator.getBuffer();
        final float[] topColors = hexToRGBA(topColor);
        final float[] bottomColors = hexToRGBA(bottomColor);
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(bb.minX, bb.minY, bb.minZ).color(bottomColors[0], bottomColors[1], bottomColors[2], bottomColors[3]).endVertex();
        buffer.pos(bb.maxX, bb.minY, bb.minZ).color(bottomColors[0], bottomColors[1], bottomColors[2], bottomColors[3]).endVertex();
        buffer.pos(bb.maxX, bb.minY, bb.maxZ).color(bottomColors[0], bottomColors[1], bottomColors[2], bottomColors[3]).endVertex();
        buffer.pos(bb.minX, bb.minY, bb.maxZ).color(bottomColors[0], bottomColors[1], bottomColors[2], bottomColors[3]).endVertex();
        buffer.pos(bb.minX, bb.maxY, bb.minZ).color(topColors[0], topColors[1], topColors[2], topColors[3]).endVertex();
        buffer.pos(bb.minX, bb.maxY, bb.maxZ).color(topColors[0], topColors[1], topColors[2], topColors[3]).endVertex();
        buffer.pos(bb.maxX, bb.maxY, bb.maxZ).color(topColors[0], topColors[1], topColors[2], topColors[3]).endVertex();
        buffer.pos(bb.maxX, bb.maxY, bb.minZ).color(topColors[0], topColors[1], topColors[2], topColors[3]).endVertex();
        buffer.pos(bb.minX, bb.minY, bb.minZ).color(bottomColors[0], bottomColors[1], bottomColors[2], bottomColors[3]).endVertex();
        buffer.pos(bb.minX, bb.maxY, bb.minZ).color(topColors[0], topColors[1], topColors[2], topColors[3]).endVertex();
        buffer.pos(bb.maxX, bb.maxY, bb.minZ).color(topColors[0], topColors[1], topColors[2], topColors[3]).endVertex();
        buffer.pos(bb.maxX, bb.minY, bb.minZ).color(bottomColors[0], bottomColors[1], bottomColors[2], bottomColors[3]).endVertex();
        buffer.pos(bb.maxX, bb.minY, bb.minZ).color(bottomColors[0], bottomColors[1], bottomColors[2], bottomColors[3]).endVertex();
        buffer.pos(bb.maxX, bb.maxY, bb.minZ).color(topColors[0], topColors[1], topColors[2], topColors[3]).endVertex();
        buffer.pos(bb.maxX, bb.maxY, bb.maxZ).color(topColors[0], topColors[1], topColors[2], topColors[3]).endVertex();
        buffer.pos(bb.maxX, bb.minY, bb.maxZ).color(bottomColors[0], bottomColors[1], bottomColors[2], bottomColors[3]).endVertex();
        buffer.pos(bb.minX, bb.minY, bb.maxZ).color(bottomColors[0], bottomColors[1], bottomColors[2], bottomColors[3]).endVertex();
        buffer.pos(bb.maxX, bb.minY, bb.maxZ).color(bottomColors[0], bottomColors[1], bottomColors[2], bottomColors[3]).endVertex();
        buffer.pos(bb.maxX, bb.maxY, bb.maxZ).color(topColors[0], topColors[1], topColors[2], topColors[3]).endVertex();
        buffer.pos(bb.minX, bb.maxY, bb.maxZ).color(topColors[0], topColors[1], topColors[2], topColors[3]).endVertex();
        buffer.pos(bb.minX, bb.minY, bb.minZ).color(bottomColors[0], bottomColors[1], bottomColors[2], bottomColors[3]).endVertex();
        buffer.pos(bb.minX, bb.minY, bb.maxZ).color(bottomColors[0], bottomColors[1], bottomColors[2], bottomColors[3]).endVertex();
        buffer.pos(bb.minX, bb.maxY, bb.maxZ).color(topColors[0], topColors[1], topColors[2], topColors[3]).endVertex();
        buffer.pos(bb.minX, bb.maxY, bb.minZ).color(topColors[0], topColors[1], topColors[2], topColors[3]).endVertex();
        tessellator.draw();
        releaseRender();
    }
}
