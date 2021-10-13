package dev.moonlight.module;

import dev.moonlight.misc.ClassFinder;
import dev.moonlight.settings.Setting;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class ModuleManager {

    private final List<Module> moduleList;
    private final HashMap<Class<? extends Module>, Module> moduleMap;

    public ModuleManager() {
        this.moduleList = new ArrayList<>();
        this.moduleMap = new HashMap<>();
        addModules();
        this.moduleList.sort(this::sortAlphabetically);
    }

    private int sortAlphabetically(Module module1, Module module2) {
        return module1.getName().compareTo(module2.getName());
    }

    private void addModules() {
        try {
            final List<Class<?>> classes = ClassFinder.from("dev.moonlight.module");
            if (classes != null) {
                for (Class<?> clazz : classes) {
                    if (!Modifier.isAbstract(clazz.getModifiers()) && Module.class.isAssignableFrom(clazz)) {
                        for (Constructor<?> constructor : clazz.getConstructors()) {
                            if (constructor.getParameterCount() == 0) {
                                final Module instance = (Module) constructor.newInstance();

                                for (Field field : instance.getClass().getDeclaredFields()) {
                                    if (!field.isAccessible())
                                        field.setAccessible(true);

                                    if (Setting.class.isAssignableFrom(field.getType())) {
                                        instance.getSettings().add((Setting) field.get(instance));
                                    }
                                }

                                moduleList.add(instance);
                                moduleMap.put(instance.getClass(), instance);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Module> getModuleList() {
        return moduleList;
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(Class<T> clazz) {
        return (T) moduleMap.get(clazz);
    }

    public boolean isModuleEnabled(Class<? extends Module> clazz) {
        return getModule(clazz).isEnabled();
    }

    public List<Module> getCategoryModules(Module.Category category) {
        final List<Module> list = new ArrayList<>();

        for (Module module : moduleList) {
            if (module.getCategory() == category)
                list.add(module);
        }

        return list;
    }
}