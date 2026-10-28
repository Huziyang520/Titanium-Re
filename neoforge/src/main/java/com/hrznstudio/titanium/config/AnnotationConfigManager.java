// Titanium Lite Annotation Config Manager

package com.hrznstudio.titanium.config;

import com.hrznstudio.titanium.annotation.config.ConfigFile;
import com.hrznstudio.titanium.annotation.config.ConfigVal;
import com.hrznstudio.titanium.platform.IConfigScope;
import com.hrznstudio.titanium.util.AnnotationUtil;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnnotationConfigManager {

    public List<Type> configClasses;
    public List<SpecCache> specCaches;

    public AnnotationConfigManager() {
        configClasses = new ArrayList<>();
        specCaches = new ArrayList<>();
    }

    public void add(ModContainer container, Type type) {
        configClasses.add(type);
        SpecCache specCache = new SpecCache();

        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        for (Class configClass : type.configClass) {
            scanClass(configClass, builder, specCache);
        }

        File folder = new File("config" + File.separator + ModLoadingContext.get().getActiveContainer().getModId());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String fileName = ModLoadingContext.get().getActiveContainer().getModId() + "/" + (type.fileName.isEmpty() ? ModLoadingContext.get().getActiveContainer().getModId() : type.fileName);
        if (!fileName.endsWith(".toml")) fileName = fileName + ".toml";
        specCache.spec = builder.build();
        specCaches.add(specCache);
        container.registerConfig(toModConfigType(type.type), specCache.spec, fileName);
    }

    private void scanClass(Class configClass, ModConfigSpec.Builder builder, SpecCache specCache) {
        builder.push(configClass.getSimpleName());
        try {
            for (Field field : configClass.getFields()) {
                if (Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(ConfigVal.class)) {
                    if (field.getType().isPrimitive() || field.getType().equals(String.class)) {
                        ConfigVal value = field.getAnnotation(ConfigVal.class);
                        ModConfigSpec.ConfigValue configValue = null;
                        if (!value.comment().isEmpty()) builder.comment(value.comment());

                        if (field.isAnnotationPresent(ConfigVal.InRangeDouble.class))
                            configValue = builder.defineInRange(value.value().isEmpty() ? field.getName() : value.value(), (Double) field.get(null),
                                    field.getAnnotation(ConfigVal.InRangeDouble.class).min(), field.getAnnotation(ConfigVal.InRangeDouble.class).max());
                        if (field.isAnnotationPresent(ConfigVal.InRangeLong.class))
                            configValue = builder.defineInRange(value.value().isEmpty() ? field.getName() : value.value(), (Long) field.get(null),
                                    field.getAnnotation(ConfigVal.InRangeLong.class).min(), field.getAnnotation(ConfigVal.InRangeLong.class).max());
                        if (field.isAnnotationPresent(ConfigVal.InRangeInt.class))
                            configValue = builder.defineInRange(value.value().isEmpty() ? field.getName() : value.value(), (Integer) field.get(null),
                                    field.getAnnotation(ConfigVal.InRangeInt.class).min(), field.getAnnotation(ConfigVal.InRangeInt.class).max());

                        if (configValue == null)
                            configValue = builder.define(value.value().isEmpty() ? field.getName() : value.value(), field.get(null));
                        specCache.cachedConfigValues.put(field, configValue);
                    } if (field.getType().equals(List.class)) {
                        ConfigVal value = field.getAnnotation(ConfigVal.class);
                        ModConfigSpec.ConfigValue configValue = null;
                        if (!value.comment().isEmpty()) builder.comment(value.comment());
                        configValue = builder.defineList(value.value().isEmpty() ? field.getName() : value.value(), (List<String>) field.get(null), (object) -> true);
                        specCache.cachedConfigValues.put(field, configValue);
                    } else {
                        scanClass(field.getType(), builder, specCache);
                    }
                }
            }
            AnnotationUtil.getFilteredAnnotatedClasses(ConfigFile.Child.class, ModLoadingContext.get().getActiveContainer().getModId()).stream()
                .filter(aClass -> ((ConfigFile.Child) aClass.getAnnotation(ConfigFile.Child.class)).value().equals(configClass)).forEach(aClass -> scanClass(aClass, builder, specCache));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        builder.pop();
    }

    public void inject(IConfigSpec spec) {

        specCaches.forEach(specCache -> {
            if (specCache.spec == spec) {
                specCache.cachedConfigValues.forEach((field, configValue) -> {
                    try {
                        field.set(null, configValue.get());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    public boolean isClassManaged(Class clazz) {
        for (Type configClass : configClasses) {
            for (Class aClass : configClass.configClass) {
                if (clazz.equals(aClass)) return true;
            }
        }
        return false;
    }

    public static class Type {
        private IConfigScope type;
        private Class[] configClass;
        private String fileName;

        private Type(IConfigScope type, Class... configClass) {
            this.type = type;
            this.configClass = configClass;
            this.fileName = "";
        }

        public static Type client(Class... classes) {
            return new Type(IConfigScope.CLIENT, classes);
        }

        public static Type common(Class... classes) {
            return new Type(IConfigScope.COMMON, classes);
        }

        public static Type server(Class... classes) {
            return new Type(IConfigScope.SERVER, classes);
        }

        public static Type of(IConfigScope type, Class... classes) {
            return new Type(type, classes);
        }

        public Type setName(String name) {
            this.fileName = name;
            return this;
        }

        public Class[] getConfigClass() {
            return configClass;
        }
    }

    public static class SpecCache {

        public ModConfigSpec spec;
        public HashMap<Field, ModConfigSpec.ConfigValue> cachedConfigValues = new HashMap<>();

    }

    private static ModConfig.Type toModConfigType(com.hrznstudio.titanium.platform.IConfigScope scope) {
        return switch (scope) {
            case STARTUP -> ModConfig.Type.STARTUP;
            case CLIENT -> ModConfig.Type.CLIENT;
            case COMMON -> byName("LOCAL", "COMMON");
            case SERVER -> byName("SYNCED", "SERVER");
        };
    }

    private static ModConfig.Type byName(String preferred, String fallback) {
        try {
            return ModConfig.Type.valueOf(preferred);
        } catch (IllegalArgumentException ignored) {
            return ModConfig.Type.valueOf(fallback);
        }
    }
}
