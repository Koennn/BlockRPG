package me.koenn.blockrpg.world.mine;

import me.koenn.blockrpg.commands.CommandManager;
import me.koenn.blockrpg.items.ItemStack;
import me.koenn.blockrpg.items.ItemType;
import me.koenn.blockrpg.world.TileType;
import org.json.simple.JSONObject;

import java.util.HashMap;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, September 2017
 */
public class Mine implements TileType {

    private final HashMap<ItemType, Integer> availableResources = new HashMap<>();

    public Mine() {
        this.availableResources.put(ItemType.getItem("diamond"), 12);
    }

    @Override
    public String getName() {
        return "mine";
    }

    @Override
    public String getDisplay() {
        StringBuilder builder = new StringBuilder();
        builder.append("**------------------------------------**\n");
        int index = 1;
        for (ItemType type : this.availableResources.keySet()) {
            builder.append(String.format(" **%s)** %sx %s\n", index++, this.availableResources.get(type), new ItemStack(type)));
        }
        builder.append("**------------------------------------**\n");
        builder.append(String.format("`Use %smine <id> to start mining a resource.`", CommandManager.CMD_CHAR));
        return builder.toString();
    }

    @Override
    public JSONObject getJSON() {
        JSONObject json = new JSONObject();
        json.put("class", this.getClass().getName());
        JSONObject resources = new JSONObject();
        for (ItemType type : this.availableResources.keySet()) {
            resources.put(type.getId(), this.availableResources.get(type));
        }
        json.put("resources", resources);
        return json;
    }

    @Override
    public void fromJSON(JSONObject json) {
        JSONObject resources = (JSONObject) json.get("resources");
        for (Object key : resources.keySet()) {
            this.availableResources.put(ItemType.getItem((String) key), Integer.parseInt(String.valueOf(resources.get(key))));
        }
    }
}
