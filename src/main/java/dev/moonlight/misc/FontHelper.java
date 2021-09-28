package dev.moonlight.misc;

import java.awt.Font;
import java.io.InputStream;

public final class FontHelper {

    public static Font getFontFromResource(String resPath, float fontSize) {
        try {
            InputStream stream = FontHelper.class.getResourceAsStream(resPath);

            if (stream != null) {
                Font font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(fontSize);
                stream.close();
                return font;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
