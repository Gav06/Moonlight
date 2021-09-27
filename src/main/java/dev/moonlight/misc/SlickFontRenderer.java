package dev.moonlight.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public final class SlickFontRenderer {
    private static final Pattern COLOR_CODE_PATTERN = Pattern.compile("§[0123456789abcdefklmnor]");

    public final int FONT_HEIGHT = 9;
    private final Font font;

    private final int[] colorCodes = new int[] {
            0, 170, 43520, 43690, 11141120, 11141290, 16755200, 11184810, 5592405, 5592575,
            5635925, 5636095, 16733525, 16733695, 16777045, 16777215 };

    private final Map<String, Float> cachedStringWidth = new HashMap<>();

    private float antiAliasingFactor;

    private UnicodeFont unicodeFont;

    private int prevScaleFactor = (new ScaledResolution(Minecraft.getMinecraft())).getScaleFactor();

    private String name;

    private float size;

    public SlickFontRenderer(Font font) {
        this.name = font.getFontName();
        this.size = font.getSize();
        this.font = font;
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        try {
            this.prevScaleFactor = resolution.getScaleFactor();
            this.unicodeFont = new UnicodeFont(font.deriveFont(font.getSize() * this.prevScaleFactor / 2.0F));
            this.unicodeFont.addAsciiGlyphs();
            this.unicodeFont.getEffects().add(new ColorEffect(Color.WHITE));
            this.unicodeFont.loadGlyphs();
        } catch (org.newdawn.slick.SlickException e) {
            e.printStackTrace();
        }
        this.antiAliasingFactor = resolution.getScaleFactor();
    }

    public void drawStringScaled(String text, int givenX, int givenY, int color, double givenScale) {
        GL11.glPushMatrix();
        GL11.glTranslated(givenX, givenY, 0.0D);
        GL11.glScaled(givenScale, givenScale, givenScale);
        drawString(text, 0.0F, 0.0F, color);
        GL11.glPopMatrix();
    }

    public int drawString(String text, float x, float y, int color) {
        if (text == null)
            return 0;

        // text render offset
        y -= 2f;

        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        try {
            if (resolution.getScaleFactor() != this.prevScaleFactor) {
                this.prevScaleFactor = resolution.getScaleFactor();
                this.unicodeFont = new UnicodeFont(font);
                this.unicodeFont.addAsciiGlyphs();
                this.unicodeFont.getEffects().add(new ColorEffect(Color.WHITE));
                this.unicodeFont.loadGlyphs();
            }
        } catch (org.newdawn.slick.SlickException e) {
            e.printStackTrace();
        }
        this.antiAliasingFactor = resolution.getScaleFactor();
        GL11.glPushMatrix();
        GlStateManager.scale(1.0F / this.antiAliasingFactor, 1.0F / this.antiAliasingFactor, 1.0F / this.antiAliasingFactor);
        x *= this.antiAliasingFactor;
        y *= this.antiAliasingFactor;
        float originalX = x;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        GlStateManager.color(red, green, blue, alpha);
        int currentColor = color;
        char[] characters = text.toCharArray();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.blendFunc(770, 771);
        String[] parts = COLOR_CODE_PATTERN.split(text);
        int index = 0;
        for (String s : parts) {
            for (String s2 : s.split("\n")) {
                for (String s3 : s2.split("\r")) {
                    this.unicodeFont.drawString(x, y, s3, new org.newdawn.slick.Color(currentColor));
                    x += this.unicodeFont.getWidth(s3);
                    index += s3.length();
                    if (index < characters.length && characters[index] == '\r') {
                        x = originalX;
                        index++;
                    }
                }
                if (index < characters.length && characters[index] == '\n') {
                    x = originalX;
                    y += getHeight(s2) * 2.0F;
                    index++;
                }
            }
            if (index < characters.length) {
                char colorCode = characters[index];
                if (colorCode == '\u0017') {
                    char colorChar = characters[index + 1];
                    int codeIndex = "0123456789abcdef".indexOf(colorChar);
                    if (codeIndex < 0) {
                        if (colorChar == 'r')
                            currentColor = color;
                    } else {
                        currentColor = this.colorCodes[codeIndex];
                    }
                    index += 2;
                }
            }
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.bindTexture(0);
        GlStateManager.popMatrix();
        return (int)getWidth(text);
    }

    public int drawStringWithShadow(String text, float x, float y, int color) {
        if (text == null || text.equals(""))
            return 0;
        drawString(StringUtils.stripControlCodes(text), x + 0.5F, y + 0.5F, 0);
        return drawString(text, x, y, color);
    }

    public void drawCenteredString(String text, float x, float y, int color) {
        drawString(text, x - ((int)getWidth(text) >> 1), y, color);
    }

    public void drawCenteredTextScaled(String text, int givenX, int givenY, int color, double givenScale) {
        GL11.glPushMatrix();
        GL11.glTranslated(givenX, givenY, 0.0D);
        GL11.glScaled(givenScale, givenScale, givenScale);
        drawCenteredString(text, 0.0F, 0.0F, color);
        GL11.glPopMatrix();
    }

    public void drawCenteredStringWithShadow(String text, float x, float y, int color) {
        drawCenteredString(StringUtils.stripControlCodes(text), x + 0.5F, y + 0.5F, 0);
        drawCenteredString(text, x, y, color);
    }

    public float getWidth(String text) {
        if (this.cachedStringWidth.size() > 1000)
            this.cachedStringWidth.clear();
        return (Float) this.cachedStringWidth.computeIfAbsent(text, e -> this.unicodeFont.getWidth(ChatFormatting.stripFormatting(text)) / this.antiAliasingFactor);
    }

    public float getCharWidth(char c) {
        return this.unicodeFont.getWidth(String.valueOf(c));
    }

    public float getHeight(String s) {
        return this.unicodeFont.getHeight(s) / 2.0F;
    }

    public UnicodeFont getFont() {
        return this.unicodeFont;
    }

    public void drawSplitString(ArrayList<String> lines, int x, int y, int color) {
        drawString(String.join("\n\r", lines), x, y, color);
    }

    public List<String> splitString(String text, int wrapWidth) {
        List<String> lines = new ArrayList<>();
        String[] splitText = text.split(" ");
        StringBuilder currentString = new StringBuilder();
        for (String word : splitText) {
            String potential = currentString + " " + word;
            if (getWidth(potential) >= wrapWidth) {
                lines.add(currentString.toString());
                currentString = new StringBuilder();
            }
            currentString.append(word).append(" ");
        }
        lines.add(currentString.toString());
        return lines;
    }

    public float getStringWidth(String p_Name) {
        return (this.unicodeFont.getWidth(ChatFormatting.stripFormatting(p_Name)) / 2f);
    }

    public float getStringHeight(String p_Name) {
        return getHeight(p_Name);
    }

    public String trimStringToWidth(String text, int width) {
        return trimStringToWidth(text, width, false);
    }

    public String trimStringToWidth(String text, int width, boolean reverse) {
        StringBuilder stringbuilder = new StringBuilder();
        int i = 0;
        int j = reverse ? (text.length() - 1) : 0;
        int k = reverse ? -1 : 1;
        boolean flag = false;
        boolean flag1 = false;
        int l;
        for (l = j; l >= 0 && l < text.length() && i < width; l += k) {
            char c0 = text.charAt(l);
            float i1 = getWidth(text);
            if (flag) {
                flag = false;
                if (c0 != 'l' && c0 != 'L') {
                    if (c0 == 'r' || c0 == 'R')
                        flag1 = false;
                } else {
                    flag1 = true;
                }
            } else if (i1 < 0.0F) {
                flag = true;
            } else {
                i = (int)(i + i1);
                if (flag1)
                    i++;
            }
            if (i > width)
                break;
            if (reverse) {
                stringbuilder.insert(0, c0);
            } else {
                stringbuilder.append(c0);
            }
        }
        return stringbuilder.toString();
    }
}