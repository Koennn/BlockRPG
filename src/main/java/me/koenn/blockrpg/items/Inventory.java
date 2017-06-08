package me.koenn.blockrpg.items;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public class Inventory {

    private final List<ItemStack> items;

    public Inventory() {
        this(new ArrayList<>());
    }

    public Inventory(JSONObject inventory) {
        this.items = new ArrayList<>();
        JSONArray items = (JSONArray) inventory.get("items");
        for (Object itemObject : items) {
            this.items.add(new ItemStack((JSONObject) itemObject));
        }
    }

    public Inventory(List<ItemStack> items) {
        this.items = items;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public JSONObject getJson() {
        JSONObject jsonObject = new JSONObject();
        JSONArray items = new JSONArray();
        for (ItemStack itemStack : this.items) {
            items.add(itemStack.getJson());
        }
        jsonObject.put("items", items);
        return jsonObject;
    }
}
