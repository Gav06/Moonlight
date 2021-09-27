package dev.moonlight.misc;

import org.lwjgl.input.Keyboard;

public class Bind {

    private int bind = Keyboard.KEY_NONE;

    public int getBind() {
        return bind;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }
}
