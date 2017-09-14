package me.koenn.blockrpg.items;

import org.json.simple.JSONObject;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, June 2017
 */
public class ItemStack {

    private ItemType type;
    private int amount;

    public ItemStack(JSONObject itemStack) {
        this(itemStack == null ? null : ItemType.getItem((String) itemStack.get("type")), itemStack == null ? 1 : Math.toIntExact((long) itemStack.get("amount")));
    }

    public ItemStack(ItemType type) {
        this(type, 1);
    }

    public ItemStack(ItemType type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void add(int amount) {
        this.amount += amount;
    }

    public JSONObject getJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", this.type.getId());
        jsonObject.put("amount", this.amount);
        return jsonObject;
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.type.getEmote(), this.type.getName());
    }
}
