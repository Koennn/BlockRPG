package me.koenn.blockrpg.items;

import me.koenn.blockrpg.items.sword.ActionBlockAttack;
import me.koenn.blockrpg.items.sword.ActionSwingSword;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, June 2017
 */
public enum ItemType {

    TEST_ITEM(
            "Test Item", ":x:",
            "Item used for testing."
    ),

    COOKIE(
            "Cookie", ":cookie:",
            "A nice and delicious cookie.\nRestores 10 hp.",
            new FoodItemAction("Cookie", 10)
    ),

    BASIC_SWORD(
            "Basic Sword", ":crossed_swords:",
            "A basic sword.\nAbilities:\n  `Swing Sword`\n  `Block Attack`",
            new IWeaponAction[]{new ActionSwingSword(), new ActionBlockAttack()}
    );

    private final String name;
    private final String emote;
    private final String description;
    private IWeaponAction[] actions;
    private IItemAction IItemAction;

    ItemType(String name, String emote, String description) {
        this.name = name;
        this.emote = emote;
        this.description = description;
    }

    ItemType(String name, String emote, String description, IWeaponAction[] actions) {
        this.name = name;
        this.emote = emote;
        this.description = description;
        this.actions = actions;
    }

    ItemType(String name, String emote, String description, IItemAction IItemAction) {
        this.name = name;
        this.emote = emote;
        this.description = description;
        this.IItemAction = IItemAction;
    }

    public String getName() {
        return name;
    }

    public String getEmote() {
        return emote;
    }

    public IWeaponAction[] getActions() {
        return actions;
    }

    public IItemAction getIItemAction() {
        return IItemAction;
    }

    public boolean hasItemAction() {
        return this.IItemAction != null;
    }

    public String getDescription() {
        return description;
    }
}
