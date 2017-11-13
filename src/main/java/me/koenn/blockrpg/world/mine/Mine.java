package me.koenn.blockrpg.world.mine;

import me.koenn.blockrpg.commands.CommandManager;
import me.koenn.blockrpg.items.ItemStack;
import me.koenn.blockrpg.items.ItemType;
import me.koenn.blockrpg.world.TileType;
import org.json.simple.JSONObject;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, September 2017
 */
public class Mine implements TileType {

    private ItemType resource;
    private int amount;

    public Mine() {
    }

    public Mine(ItemType resource, int amount) {
        this.resource = resource;
        this.amount = amount;
    }

    @Override
    public String getName() {
        return "mine";
    }

    @Override
    public String getDisplay() {
        return "**------------------------------------**\n" +
                String.format(" %sx %s\n", this.amount, new ItemStack(this.resource)) +
                "**------------------------------------**\n" +
                String.format("`Use %suse pickaxe to mine a resource.`", CommandManager.CMD_CHAR);
    }

    @Override
    public JSONObject getJSON() {
        JSONObject json = new JSONObject();
        json.put("class", this.getClass().getName());
        json.put("resource", this.resource.getId());
        json.put("amount", this.amount);
        return json;
    }

    @Override
    public void fromJSON(JSONObject json) {
        this.resource = ItemType.getItem((String) json.get("resource"));
        this.amount = Integer.parseInt(String.valueOf(json.get("amount")));
    }

    public ItemType getResource() {
        return resource;
    }

    public int getAmount() {
        return amount;
    }

    public ItemStack mine(int amount) {
        this.amount -= amount;
        return new ItemStack(this.resource, amount);
    }
}
