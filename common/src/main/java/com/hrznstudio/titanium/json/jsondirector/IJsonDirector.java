// Titanium Lite I Json Director

package com.hrznstudio.titanium.json.jsondirector;

import net.minecraft.resources.Identifier;

public interface IJsonDirector<T> {
    void put(Identifier resourceLocation, T value);

    void clear();
}
