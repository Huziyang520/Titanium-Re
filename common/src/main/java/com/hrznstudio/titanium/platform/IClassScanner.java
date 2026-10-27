// Annotation discovery that hides the loader scan data source

package com.hrznstudio.titanium.platform;

import java.lang.annotation.Annotation;
import java.util.Map;

public interface IClassScanner {

    <T extends Annotation> Map<Class<?>, T> findAnnotated(Class<T> annotation, String modId);
}
