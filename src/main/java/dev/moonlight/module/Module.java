package dev.moonlight.module;

import dev.moonlight.Moonlight;
import dev.moonlight.events.PacketEvent;
import dev.moonlight.misc.Bind;
import dev.moonlight.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Objects;

public abstract class Module extends Bind {

    protected final Minecraft mc = Minecraft.getMinecraft();
    protected final Moonlight moonlight = Moonlight.INSTANCE;

    private final ArrayList<Setting> settings = new ArrayList<>();
    private final String name;
    private final Category category;
    private final String desc;
    private boolean enabled = false;
    private final boolean registerByDefault;

    public Module() {
        if (getClass().isAnnotationPresent(Info.class)) {
            final Info info = getClass().getAnnotation(Info.class);
            this.name = info.name();
            this.category = info.category();
            this.desc = info.desc();
            this.registerByDefault = info.registerByDefault();
            this.setBind(info.bind());
            this.setEnabled(info.enabled());
        } else {
            throw new RuntimeException(String.format("Module (%s) is missing @Info annotation", getClass().getName()));
        }
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public String getDesc() {
        return desc;
    }

    public void enable() {
        enabled = true;
        if (registerByDefault) {
            MinecraftForge.EVENT_BUS.register(this);
        }
        onEnable();
    }

    public void disable() {
        enabled = false;
        if (registerByDefault) {
            MinecraftForge.EVENT_BUS.unregister(this);
        }
        onDisable();
    }

    public void toggle() {
        if (enabled) {
            disable();
        } else {
            enable();
        }
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            enable();
        } else {
            disable();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    protected void onEnable() { }

    protected void onDisable() { }

    public ArrayList<Setting> getSettings() {
        return settings;
    }

    public String getMetaData(String message) {
        return "";
    }

    public enum Category {
        Combat,
        Render,
        Movement,
        World,
        Player,
        Client,
        HUD
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Info {
        String name();
        Category category();
        String desc();
        int bind() default Keyboard.KEY_NONE;
        boolean enabled() default false;
        boolean registerByDefault() default true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Module module = (Module) o;
        return enabled == module.enabled && name.equals(module.name) && category == module.category && desc.equals(module.desc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category, desc, enabled, getBind());
    }
}
