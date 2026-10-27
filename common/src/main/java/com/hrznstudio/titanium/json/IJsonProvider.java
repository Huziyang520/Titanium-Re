// Titanium Lite I Json Provider

package com.hrznstudio.titanium.json;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.Identifier;

public interface IJsonProvider<T> {
    T provide(Identifier key, JsonObject jsonObject) throws JsonParseException;
}
