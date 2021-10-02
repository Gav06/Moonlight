package dev.moonlight.settings.impl;

import dev.moonlight.settings.Setting;
import dev.moonlight.settings.Visibility;

import java.awt.*;

public class ColorSetting extends Setting {
    public FloatSetting r, g, b, a;

    public ColorSetting(String name, float r, float g, float b, float a) {
        super(name);
        this.r = new FloatSetting("Red", r, 0, 255);
        this.g = new FloatSetting("Green", g, 0, 255);
        this.b = new FloatSetting("Blue", b, 0, 255);
        this.a = new FloatSetting("Alpha", a, 0, 255);
    }

    public ColorSetting(String name, Color color) {
        super(name);
        this.r = new FloatSetting("Red", color.getRed(), 0, 255);
        this.g = new FloatSetting("Green", color.getGreen(), 0, 255);
        this.b = new FloatSetting("Blue", color.getBlue(), 0, 255);
        this.a = new FloatSetting("Alpha", color.getAlpha(), 0, 255);
    }

    public ColorSetting(String name, float r, float g, float b, float a, Visibility visible) {
        super(name, visible);
        this.r = new FloatSetting("Red", r, 0, 255);
        this.g = new FloatSetting("Green", g, 0, 255);
        this.b = new FloatSetting("Blue", b, 0, 255);
        this.a = new FloatSetting("Alpha", a, 0, 255);
    }

    public ColorSetting(String name, Color color, Visibility visible) {
        super(name, visible);
        this.r = new FloatSetting("Red", color.getRed(), 0, 255);
        this.g = new FloatSetting("Green", color.getGreen(), 0, 255);
        this.b = new FloatSetting("Blue", color.getBlue(), 0, 255);
        this.a = new FloatSetting("Alpha", color.getAlpha(), 0, 255);
    }

    public Color getAsColor() {
        return new Color(r.getValue(), g.getValue(), b.getValue(), a.getValue());
    }

    public float getR() {
        return r.getValue();
    }

    public void setR(float r) {
        this.r.setValue(r);
    }

    public float getG() {
        return g.getValue();
    }

    public void setG(float g) {
        this.g.setValue(g);
    }

    public float getB() {
        return b.getValue();
    }

    public void setB(float b) {
        this.b.setValue(b);
    }

    public float getA() {
        return a.getValue();
    }

    public void setA(float a) {
        this.a.setValue(a);
    }
}
