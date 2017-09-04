package me.koenn.blockrpg.items;

import me.koenn.blockrpg.items.sword.ActionBlockAttack;
import me.koenn.blockrpg.items.sword.ActionSwingSword;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, June 2017
 */
public enum ItemType {

    TEST_ITEM("Test Item", ":x:"),
    COOKIE("Cookie", ":cookie:", new FoodItemAction("Cookie", 10)),
    BASIC_SWORD("Basic Sword", ":crossed_swords:", new WeaponAction[]{
            new ActionSwingSword(),
            new ActionBlockAttack()
    });

    private final String name;
    private final String emote;
    private WeaponAction[] actions;
    private ItemAction itemAction;

    ItemType(String name, String emote) {
        this.name = name;
        this.emote = emote;
    }

    ItemType(String name, String emote, WeaponAction[] actions) {
        this.name = name;
        this.emote = emote;
        this.actions = actions;
    }

    ItemType(String name, String emote, ItemAction itemAction) {
        this.name = name;
        this.emote = emote;
        this.itemAction = itemAction;
    }

    public String getName() {
        return name;
    }

    public String getEmote() {
        return emote;
    }

    public WeaponAction[] getActions() {
        return actions;
    }

    public ItemAction getItemAction() {
        return itemAction;
    }

    public boolean hasItemAction() {
        return this.itemAction != null;
    }
}
