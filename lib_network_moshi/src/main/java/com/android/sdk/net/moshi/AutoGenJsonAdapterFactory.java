package com.android.sdk.net.moshi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

/** Uses {@link AutoMoshi} to adapt an abstract model through its concrete class. */
public final class AutoGenJsonAdapterFactory implements JsonAdapter.Factory {

    @Override
    public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
        if (!annotations.isEmpty()) {
            return null;
        }
        AutoMoshi annotation = Types.getRawType(type).getAnnotation(AutoMoshi.class);
        return annotation == null ? null : moshi.adapter(annotation.autoClass());
    }
}
