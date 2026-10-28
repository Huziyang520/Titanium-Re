// Fabric annotation scan is not wired up yet, an empty result keeps callers safe

package com.hrznstudio.titanium.platform.fabric;

import com.hrznstudio.titanium.platform.IClassScanner;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Map;

public final class FabricClassScanner implements IClassScanner {

    @Override
    public <T extends Annotation> Map<Class<?>, T> findAnnotated(Class<T> annotation, String modId) {
        return Collections.emptyMap();
    }
}
