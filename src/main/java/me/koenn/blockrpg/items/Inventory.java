package me.koenn.blockrpg.items;

import me.koenn.blockrpg.data.JSONConvertible;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, June 2017
 */
public class Inventory implements JSONConvertible {

    private final List<ItemStack> items;

    public Inventory() {
        this(new ArrayList<>());

        //TODO: Move somewhere else?
        this.items.add(new ItemStack(ItemType.getItem("basic_sword")));
        this.items.add(new ItemStack(ItemType.getItem("cookie"), 12));
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

    public boolean hasItem(ItemType type) {
        return this.getItemStack(type) != null;
    }

    public ItemStack getItemStack(ItemType type) {
        if (type == null) {
            return null;
        }
        for (ItemStack stack : this.items) {
            if (stack.getType().getName().equals(type.getName())) {
                return stack;
            }
        }
        return null;
    }

    public void addItemStack(ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }
        if (this.hasItem(itemStack.getType())) {
            this.getItemStack(itemStack.getType()).add(itemStack.getAmount());
        } else {
            this.items.add(new ItemStack(itemStack));
        }
    }

    public void removeItemStack(ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }
        if (!this.hasItem(itemStack.getType())) {
            return;
        }
        ItemStack stack = this.getItemStack(itemStack.getType());
        stack.setAmount(stack.getAmount() - itemStack.getAmount());
        if (stack.getAmount() <= 0) {
            this.items.remove(stack);
        }
    }

    public List<ItemStack> getItems() {
        return items;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        JSONArray items = new JSONArray();
        for (ItemStack itemStack : this.items) {
            items.add(itemStack.toJSON());
        }
        jsonObject.put("items", items);
        return jsonObject;
    }

    public String getFormattedString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (ItemStack itemStack : this.items) {
            if (itemStack == null || itemStack.getType() == null) {
                continue;
            }
            stringBuilder.append(String.format("%sx %s\n", itemStack.getAmount(), itemStack.toString()));
        }
        return stringBuilder.toString().trim();
    }
}
