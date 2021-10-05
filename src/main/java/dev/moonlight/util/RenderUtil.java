package dev.moonlight.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

import java.awt.*;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glHint;

public final class RenderUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();
    static ICamera camera;
    static {
        camera = new Frustum();
    }
    public static void drawBox(final BlockPos pos, final Color color) {
        final AxisAlignedBB bb = new AxisAlignedBB(pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, pos.getX() + 1 - RenderUtil.mc.getRenderManager().viewerPosX, pos.getY() + 1 - RenderUtil.mc.getRenderManager().viewerPosY, pos.getZ() + 1 - RenderUtil.mc.getRenderManager().viewerPosZ);
        camera.setPosition(Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity()).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + RenderUtil.mc.getRenderManager().viewerPosX, bb.minY + RenderUtil.mc.getRenderManager().viewerPosY, bb.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, bb.maxX + RenderUtil.mc.getRenderManager().viewerPosX, bb.maxY + RenderUtil.mc.getRenderManager().viewerPosY, bb.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            glEnable(2848);
            glHint(3154, 4354);
            RenderGlobal.renderFilledBox(bb, color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
            GL11.glDisable(2848);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }
    //i really dont care this is just so good to have man stfu skidded from client 2.0.0 nigger
    public static void drawRoundedRect(double x, double y, double width, double height, final double radius, final Color color) {
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);
        x *= 2.0;
        y *= 2.0;
        width *= 2.0;
        height *= 2.0;
        width += x;
        height += y;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        GL11.glEnable(2848);
        GL11.glBegin(9);
        // draws top left
        for (int i = 0; i <= 90; ++i) {
            GL11.glVertex2d(x + radius + Math.sin(i * 3.141592653589793 / 180.0) * radius * -1.0, y + radius + Math.cos(i * 3.141592653589793 / 180.0) * radius * -1.0);
        }
        //draws bottom left
        for (int i = 90; i <= 180; ++i) {
            GL11.glVertex2d(x + radius + Math.sin(i * 3.141592653589793 / 180.0) * radius * -1.0, height - radius + Math.cos(i * 3.141592653589793 / 180.0) * radius * -1.0);
        }
        // draws bottom right
        for (int i = 0; i <= 90; ++i) {
            GL11.glVertex2d(width - radius + Math.sin(i * 3.141592653589793 / 180.0) * radius, height - radius + Math.cos(i * 3.141592653589793 / 180.0) * radius);
        }
        //draws top right
        for (int i = 90; i <= 180; ++i) {
            GL11.glVertex2d(width - radius + Math.sin(i * 3.141592653589793 / 180.0) * radius, y + radius + Math.cos(i * 3.141592653589793 / 180.0) * radius);
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glScaled(2.0, 2.0, 2.0);
        GL11.glPopAttrib();
    }


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
}
