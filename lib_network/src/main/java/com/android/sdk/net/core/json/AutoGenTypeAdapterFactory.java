package com.android.sdk.net.core.json;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * 使用 AutoGson 注解自动映射，比如把需要反序列化的抽象类映射到具体的实现。具体参考：
 * <ol>
 *     <li><a href="https://gist.github.com/JakeWharton/0d67d01badcee0ae7bc9">JakeWharton/AutoGson.java</a></li>
 *     <li><a href="https://gist.github.com/Piasy/fa507251da452d36b221">Piasy/AutoGenTypeAdapterFactory.java</a></li>
 * </ol>
 */
public final class AutoGenTypeAdapterFactory implements TypeAdapterFactory {

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
        final Class<T> rawType = (Class<T>) type.getRawType();
        final AutoGson annotation = rawType.getAnnotation(AutoGson.class);
        // Only deserialize classes decorated with @AutoGson.
        return annotation == null ? null : (TypeAdapter<T>) gson.getAdapter(annotation.autoClass());
    }

}