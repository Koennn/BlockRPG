package me.koenn.blockrpg.world.village;

import me.koenn.blockrpg.items.ItemStack;
import org.json.simple.JSONObject;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, September 2017
 */
public class Trade {

    private final ItemStack offer;
    private final ItemStack cost;

    public Trade(JSONObject json) {
        this.offer = new ItemStack((JSONObject) json.get("offer"));
        this.cost = new ItemStack((JSONObject) json.get("cost"));
    }

    public Trade(ItemStack offer, ItemStack cost) {
        this.offer = offer;
        this.cost = cost;
    }

    public ItemStack getOffer() {
        return offer;
    }

    public ItemStack getCost() {
        return cost;
    }

    public String getDisplay() {
        return String.format("Trade %s **%s** for %s **%s**", this.cost.getAmount(), this.cost, this.offer.getAmount(), this.offer);
    }

    public JSONObject getJSON() {
        JSONObject json = new JSONObject();
        json.put("offer", this.offer.getJson());
        json.put("cost", this.cost.getJson());
        return json;
    }
}
