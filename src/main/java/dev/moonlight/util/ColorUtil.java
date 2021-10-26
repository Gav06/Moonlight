package dev.moonlight.util;

import java.awt.*;

public class ColorUtil {

    public static Color rainbow(int delay) {
        double rainbowState = Math.ceil((double) (System.currentTimeMillis() + (long) delay) / 20.0);
        return Color.getHSBColor((float) (rainbowState % 360.0 / 360.0), 1f, 1f);
    }
}
