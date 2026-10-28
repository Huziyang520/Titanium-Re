// Titanium Lite Config File

package com.hrznstudio.titanium.annotation.config;

import com.hrznstudio.titanium.platform.IConfigScope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ConfigFile {

    IConfigScope type() default IConfigScope.COMMON;

    String value() default "";

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @interface Child {

        Class value();

    }
}
