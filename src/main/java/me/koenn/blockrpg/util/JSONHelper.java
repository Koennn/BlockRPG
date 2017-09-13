package me.koenn.blockrpg.util;

import me.koenn.blockrpg.world.Tile;
import me.koenn.blockrpg.world.Vector2;
import org.json.simple.JSONObject;

import java.util.HashMap;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, August 2017
 */
public final class JSONHelper {

    public static JSONObject locationToJson(Vector2 location) {
        JSONObject json = new JSONObject();
        json.put("x", location.x);
        json.put("y", location.y);
        return json;
    }

    public static Vector2 jsonToLocation(JSONObject json) {
        return new Vector2(Math.toIntExact((long) json.get("x")), Math.toIntExact((long) json.get("y")));
    }

    public static JSONObject tileToJson(Tile tile) {
        JSONObject json = locationToJson(tile.getLocation());
        JSONObject properties = new JSONObject();
        HashMap<String, Object> tileProperties = tile.getProperties();
        for (String key : tileProperties.keySet()) {
            properties.put(key, tileProperties.get(key));
        }
        json.put("properties", properties);
        return json;
    }

    public static Tile jsonToTile(JSONObject json) {
        return new Tile(jsonToLocation(json), (JSONObject) json.get("properties"));
    }
}
