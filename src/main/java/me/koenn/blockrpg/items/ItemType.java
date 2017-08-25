package me.koenn.blockrpg.items;

import me.koenn.blockrpg.items.basic_sword.ActionBlockAttack;
import me.koenn.blockrpg.items.basic_sword.ActionSwingSword;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, June 2017
 */
public enum ItemType {

    TEST_ITEM("Test Item"),
    BASIC_SWORD("Basic Sword", new WeaponAction[]{
            new ActionSwingSword(),
            new ActionBlockAttack()
    });

    private final String name;
    private WeaponAction[] actions;

    ItemType(String name) {
        this.name = name;
    }

    ItemType(String name, WeaponAction[] actions) {
        this.name = name;
        this.actions = actions;
    }

    public String getName() {
        return name;
    }

    public WeaponAction[] getActions() {
        return actions;
    }
}
