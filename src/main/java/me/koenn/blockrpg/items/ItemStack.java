package me.koenn.blockrpg.items;

import me.koenn.blockrpg.data.JSONConvertible;
import org.json.simple.JSONObject;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, June 2017
 */
public class ItemStack implements JSONConvertible {

    private ItemType type;
    private int amount;

    public ItemStack(ItemType type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public ItemStack(JSONObject itemStack) {
        this(itemStack == null ? null : ItemType.getItem((String) itemStack.get("type")), itemStack == null ? 1 : Math.toIntExact((long) itemStack.get("amount")));
    }

    public ItemStack(ItemStack itemStack) {
        this(itemStack.getType(), itemStack.getAmount());
    }

    public ItemStack(ItemType type) {
        this(type, 1);
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

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", this.type.getId());
        jsonObject.put("amount", this.amount);
        return jsonObject;
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.type.getName(), this.type.getEmote());
    }
}
