package com.android.sdk.net.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

import kotlin.Unit;

/**
 * @author Ztiany
 */
public class GsonFactory {

    public static Gson newGson() {
        return new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .excludeFieldsWithModifiers(Modifier.STATIC)
                .registerTypeAdapter(int.class, new PrimitiveIntegerJsonDeserializer())
                .registerTypeAdapter(float.class, new PrimitiveFloatJsonDeserializer())
                .registerTypeAdapter(double.class, new PrimitiveDoubleJsonDeserializer())
                .registerTypeAdapter(Integer.class, new IntegerJsonDeserializer())
                .registerTypeAdapter(Float.class, new FloatJsonDeserializer())
                .registerTypeAdapter(Double.class, new DoubleJsonDeserializer())
                .registerTypeAdapter(String.class, new StringJsonDeserializer())
                .registerTypeAdapter(Void.class, new VoidJsonDeserializer())
                .registerTypeAdapter(Unit.class, new UnitJsonDeserializer())
                .registerTypeAdapterFactory(new AutoGenTypeAdapterFactory())
                .create();
    }

}