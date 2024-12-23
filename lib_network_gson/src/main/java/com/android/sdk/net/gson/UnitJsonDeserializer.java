package com.android.sdk.net.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import kotlin.Unit;

public class UnitJsonDeserializer implements JsonDeserializer<Unit> {

    @Override
    public Unit deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Unit.INSTANCE;
    }

}