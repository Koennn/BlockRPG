package me.koenn.blockrpg.world;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.battle.Battle;
import me.koenn.blockrpg.battle.Creature;
import me.koenn.blockrpg.battle.CreatureType;
import me.koenn.blockrpg.items.ItemStack;
import me.koenn.blockrpg.items.ItemType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public class World {

    private final long userId;
    private final HashMap<Vector2, Tile> tiles = new HashMap<>();

    public World(long userId) {
        this.userId = userId;
        final Vector2 homeTile = new Vector2(0, 0);
        final Tile tile = new Tile(homeTile);
        tile.setProperty("homeTile", true);
        this.tiles.put(homeTile, tile);
    }

    public World(JSONObject world) {
        this.userId = (long) world.get("userId");
        final JSONArray tiles = (JSONArray) world.get("tiles");
        for (Object tileObject : tiles) {
            final JSONObject tileJson = (JSONObject) tileObject;
            final Vector2 location = new Vector2(Math.toIntExact((long) tileJson.get("x")), Math.toIntExact((long) tileJson.get("y")));
            final JSONObject properties = (JSONObject) tileJson.get("properties");
            this.tiles.put(location, new Tile(location, properties));
        }
    }

    public Tile explore(Vector2 location) {
        if (this.isExplored(location)) {
            return this.getTile(location);
        }
        Tile tile = new Tile(location);
        this.tiles.put(location, tile);
        return tile;
    }

    public boolean isExplored(Vector2 location) {
        return this.getTile(location) != null;
    }

    public Tile getTile(Vector2 location) {
        for (Tile tile : this.tiles.values()) {
            if (tile.getLocation().equals(location)) {
                return tile;
            }
        }
        return null;
    }

    public Message getBattle(User user, MessageChannel channel, Tile tile) {
        /*if (ThreadLocalRandom.current().nextInt(10) != 1) {
            return null;
        }*/
        Battle battle = new Battle(user, channel, new Creature(CreatureType.SCARY_MONSTER, 50, new ItemStack(ItemType.BASIC_SWORD)), tile);
        BlockRPG.getInstance().getUserBattles().put(user.getIdLong(), battle);
        return battle.start();
    }

    public JSONObject toJson() {
        final JSONObject jsonObject = new JSONObject();
        final JSONArray tiles = new JSONArray();
        for (Tile tile : this.tiles.values()) {
            final JSONObject tileJson = new JSONObject();
            tileJson.put("x", tile.getLocation().x);
            tileJson.put("y", tile.getLocation().x);
            final JSONObject propertiesJson = new JSONObject();
            final HashMap<String, Object> properties = tile.getProperties();
            for (String key : properties.keySet()) {
                propertiesJson.put(key, properties.get(key));
            }
            tileJson.put("properties", propertiesJson);
            tiles.add(tileJson);
        }
        jsonObject.put("tiles", tiles);
        jsonObject.put("userId", this.userId);
        return jsonObject;
    }

    public long getUserId() {
        return userId;
    }
}
