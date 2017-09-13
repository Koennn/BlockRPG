package me.koenn.blockrpg.world;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.battle.Battle;
import me.koenn.blockrpg.battle.creature.Creature;
import me.koenn.blockrpg.battle.creature.CreatureType;
import me.koenn.blockrpg.util.Chance;
import me.koenn.blockrpg.util.JSONHelper;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, June 2017
 */
public class World {

    private final long userId;
    private final HashMap<Vector2, Tile> tiles = new HashMap<>();
    private Vector2 location;

    public World(long userId) {
        this.userId = userId;
        Vector2 homeTile = new Vector2(0, 0);
        Tile tile = new Tile(homeTile);
        tile.setProperty("homeTile", true);
        this.tiles.put(homeTile, tile);
        this.location = homeTile;
    }

    public World(JSONObject world) {
        this.userId = (long) world.get("userId");
        this.location = JSONHelper.jsonToLocation((JSONObject) world.get("location"));
        JSONArray tiles = (JSONArray) world.get("tiles");
        for (Object tileObject : tiles) {
            Tile tile = JSONHelper.jsonToTile((JSONObject) tileObject);
            this.tiles.put(tile.getLocation(), tile);
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
        if (!Chance.of(20)) {
            return null;
        }
        Battle battle = new Battle(user, channel, Creature.create(CreatureType.CREATURES.get("scary_monster")), tile);
        BlockRPG.getInstance().getUserBattles().put(user.getIdLong(), battle);
        return battle.start();
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        JSONArray tiles = new JSONArray();
        this.tiles.values().forEach(tile -> tiles.add(JSONHelper.tileToJson(tile)));
        jsonObject.put("tiles", tiles);
        jsonObject.put("userId", this.userId);
        jsonObject.put("location", JSONHelper.locationToJson(this.location));
        return jsonObject;
    }

    public long getUserId() {
        return userId;
    }

    public Vector2 getLocation() {
        return location;
    }

    public void setLocation(Vector2 location) {
        this.location = location;
    }
}
