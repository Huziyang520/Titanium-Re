// Titanium Lite NeoForge class scanner

package com.hrznstudio.titanium.platform.neoforge;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.platform.IClassScanner;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

public final class NeoForgeClassScanner implements IClassScanner {

    @Override
    public <T extends Annotation> Map<Class<?>, T> findAnnotated(Class<T> annotation, String modId) {
        Type type = Type.getType(annotation);
        Map<Class<?>, T> found = new LinkedHashMap<>();
        for (ModFileScanData.AnnotationData data : collect(modId)) {
            if (data.targetType() != ElementType.TYPE || !type.equals(data.annotationType())) continue;
            Class<?> clazz = load(data.memberName());
            if (clazz == null) continue;
            T value = clazz.getAnnotation(annotation);
            if (value != null) found.put(clazz, value);
        }
        return found;
    }

    private static Collection<ModFileScanData.AnnotationData> collect(String modId) {
        if (modId != null) {
            ModFileScanData scanData = ModList.get().getModFileById(modId).getFile().getScanResult();
            return scanData.getAnnotations();
        }
        Collection<ModFileScanData.AnnotationData> all = new LinkedHashSet<>();
        ModList.get().getAllScanData().forEach(scanData -> all.addAll(scanData.getAnnotations()));
        return all;
    }

    private static Class<?> load(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            Titanium.LOGGER.error("Unable to load annotated class " + className, e);
            return null;
        }
    }
}
