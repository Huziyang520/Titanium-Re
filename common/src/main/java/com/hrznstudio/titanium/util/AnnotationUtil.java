// Titanium Lite Annotation Util

package com.hrznstudio.titanium.util;

import com.hrznstudio.titanium.platform.Platform;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class AnnotationUtil {

    public static List<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotation) {
        return new ArrayList<>(Platform.scanner().findAnnotated(annotation, null).keySet());
    }

    public static List<Class<?>> getFilteredAnnotatedClasses(Class<? extends Annotation> annotation, String filter) {
        return new ArrayList<>(Platform.scanner().findAnnotated(annotation, filter).keySet());
    }
}
