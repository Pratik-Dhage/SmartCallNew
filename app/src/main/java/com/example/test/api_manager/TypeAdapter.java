package com.example.test.api_manager;

import android.os.Build;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class TypeAdapter implements JsonSerializer<Type>, JsonDeserializer<Type> {

    @Override
    public Type deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String typeString = json.getAsString();
        try {
            return Class.forName(typeString);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unable to deserialize Type", e);
        }
    }

    @Override
    public JsonElement serialize(Type src, Type typeOfSrc, JsonSerializationContext context) {
        JsonPrimitive jsonPrimitive = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

             jsonPrimitive = new JsonPrimitive(src.getTypeName());

        }
        return jsonPrimitive;
    }
}
