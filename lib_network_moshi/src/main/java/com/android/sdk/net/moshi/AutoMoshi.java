package com.android.sdk.net.moshi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Maps an abstract model to its concrete implementation for Moshi conversion. */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoMoshi {

    Class<?> autoClass();
}
