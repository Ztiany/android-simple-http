package com.android.sdk.net.moshi;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory;

import kotlin.Unit;

/** Creates a Moshi instance with adapters for common HTTP response types. */
public final class MoshiFactory {

    private MoshiFactory() {
    }

    public static Moshi newMoshi() {
        Moshi.Builder builder = new Moshi.Builder();
        ScalarJsonAdapters.addTo(builder);
        return builder
                .add(new AutoGenJsonAdapterFactory())
                .add(Unit.class, ScalarJsonAdapters.UNIT)
                .add(Void.class, ScalarJsonAdapters.VOID)
                .addLast(new KotlinJsonAdapterFactory())
                .build();
    }
}
