package dev.moonlight.settings.impl;

import dev.moonlight.settings.Setting;
import dev.moonlight.settings.Visibility;

import java.awt.*;

public class ColorSetting extends Setting {
    public FloatSetting r, g, b, a;

    public ColorSetting(String name, float r, float g, float b, float a) {
        super(name);
        this.r.setValue(r);
        this.g.setValue(g);
        this.b.setValue(b);
        this.a.setValue(a);
    }

    public ColorSetting(String name, Color color) {
        super(name);
        this.r.setValue(color.getRed());
        this.g.setValue(color.getGreen());
        this.b.setValue(color.getBlue());
        this.a.setValue(color.getAlpha());
    }

    public ColorSetting(String name, float r, float g, float b, float a, Visibility visible) {
        super(name, visible);
        this.r.setValue(r);
        this.g.setValue(g);
        this.b.setValue(b);
        this.a.setValue(a);
    }

    public ColorSetting(String name, Color color, Visibility visible) {
        super(name, visible);
        this.r.setValue(color.getRed());
        this.g.setValue(color.getGreen());
        this.b.setValue(color.getBlue());
        this.a.setValue(color.getAlpha());
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
