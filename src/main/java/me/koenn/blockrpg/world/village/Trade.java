package me.koenn.blockrpg.world.village;

import me.koenn.blockrpg.data.JSONConvertible;
import me.koenn.blockrpg.items.ItemStack;
import me.koenn.blockrpg.items.ItemType;
import me.koenn.blockrpg.util.ItemHelper;
import org.json.simple.JSONObject;

import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, September 2017
 */
public class Trade implements JSONConvertible {

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

    public Trade(int value) {
        ItemType offer = ItemHelper.findItem(value, null);
        int divider = ThreadLocalRandom.current().nextInt(10) + 1;
        ItemType cost = ItemHelper.findItem(value / divider, offer);

        int amount = (offer.getValue() / cost.getValue());
        if (amount > 1) {
            amount -= ThreadLocalRandom.current().nextBoolean() ? 1 : 0;
        }

        this.offer = new ItemStack(offer, 1);
        this.cost = new ItemStack(cost, amount);
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

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("offer", this.offer.toJSON());
        json.put("cost", this.cost.toJSON());
        return json;
    }
}
