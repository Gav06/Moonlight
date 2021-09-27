package dev.moonlight.misc;

import com.google.common.reflect.ClassPath;
import dev.moonlight.Moonlight;
import net.minecraft.launchwrapper.Launch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public final class ClassFinder {

    public static List<Class<?>> from(String packageName) {
        try {
            final List<Class<?>> classes = new ArrayList<>();
            for (ClassPath.ClassInfo info : ClassPath.from(Launch.classLoader).getAllClasses()) {
                if (info.getName().startsWith(packageName)) {
                    classes.add(info.load());
                }
            }
            return classes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
