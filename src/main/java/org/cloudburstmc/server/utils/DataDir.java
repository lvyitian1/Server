package org.cloudburstmc.server.utils;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@BindingAnnotation
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
public @interface DataDir {

    Type type();

    enum Type {
        FILE,
        DATA,
        PLUGIN,
        LEVEL,
    }
}
