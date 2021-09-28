package dev.moonlight.settings;

public abstract class Setting {

    private final String name;
    private final Visibility visible;

    public Setting(String name) {
        this.name = name;
        this.visible = null;
    }

    public Setting(String name, Visibility visible) {
        this.name = name;
        this.visible = visible;
    }

    public String getName() {
        return name;
    }

    public boolean isVisible() {
        if (visible == null) {
            return true;
        } else {
            return visible.visible();
        }
    }
}
