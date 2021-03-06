package me.koenn.blockrpg.world;

import me.koenn.blockrpg.util.FancyString;
import me.koenn.blockrpg.util.ReflectionHelper;
import org.json.simple.JSONObject;

import java.util.HashMap;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, June 2017
 */
public class Tile {

    private final HashMap<String, Object> properties = new HashMap<>();
    private final Vector2 location;

    public Tile(Vector2 location) {
        this.location = location;
    }

    public Tile(Vector2 location, JSONObject properties) {
        this.location = location;
        for (Object key : properties.keySet()) {
            if (properties.get(key) instanceof JSONObject) {
                JSONObject json = (JSONObject) properties.get(key);
                TileType type = (TileType) ReflectionHelper.newInstance(ReflectionHelper.getClass((String) json.get("class")), null);
                type.fromJSON(json);
                this.properties.put((String) key, type);
            } else {
                this.properties.put((String) key, properties.get(key));
            }
        }
    }

    public Vector2 getLocation() {
        return location;
    }

    public Object getProperty(String name) {
        return this.properties.get(name);
    }

    public void setProperty(String name, Object value) {
        this.properties.put(name, value);
        this.properties.get(name);
    }

    public HashMap<String, Object> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("**Coordinates:** ").append(this.location).append("\n");
        for (String key : this.properties.keySet()) {
            if (this.properties.get(key) instanceof TileType) {
                stringBuilder.append("**").append(new FancyString(key)).append(":** \n").append(((TileType) this.properties.get(key)).getDisplay()).append("\n");
            } else {
                stringBuilder.append("**").append(new FancyString(key)).append(":** ").append(this.properties.get(key)).append("\n");
            }
        }
        return stringBuilder.toString().trim();
    }
}
