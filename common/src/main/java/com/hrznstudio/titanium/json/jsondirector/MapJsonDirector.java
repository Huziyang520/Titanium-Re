// Titanium Lite Map Json Director

package com.hrznstudio.titanium.json.jsondirector;

import net.minecraft.resources.Identifier;

import java.util.Map;

public class MapJsonDirector<T> implements IJsonDirector<T> {
    private final Map<Identifier, T> map;

    public MapJsonDirector(Map<Identifier, T> map) {
        this.map = map;
    }

    @Override
    public void put(Identifier resourceLocation, T value) {
        map.put(resourceLocation, value);
    }

    @Override
    public void clear() {
        map.clear();
    }
}
