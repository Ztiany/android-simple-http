package com.android.sdk.net.core.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Marks an <i>AutoValue</i>/<i>AutoParcel</i>-annotated type for proper
 * Gson serialization.
 * <p>
 * This annotation is needed because the {@linkplain Retention retention} of
 * <i>AutoValue</i>/<i>AutoParcel</i>
 * does not allow reflection at runtime.
 *
 * @see AutoGenTypeAdapterFactory
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoGson {

    /**
     * A reference to the Auto*-generated class (e.g. AutoValue_MyClass/AutoParcel_MyClass). This
     * is necessary to handle obfuscation of the class names.
     *
     * @return the annotated class's real type.
     */
    Class<?> autoClass();

}