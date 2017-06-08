package me.koenn.blockrpg.items;

import org.json.simple.JSONObject;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public class ItemStack {

    private Material type;
    private int amount;

    public ItemStack(JSONObject itemStack) {
        this(Material.valueOf((String) itemStack.get("type")), Integer.parseInt((String) itemStack.get("amount")));
    }

    public ItemStack(Material type) {
        this(type, 1);
    }

    public ItemStack(Material type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public Material getType() {
        return type;
    }

    public void setType(Material type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public JSONObject getJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", this.type.name());
        jsonObject.put("amount", this.amount);
        return jsonObject;
    }
}
