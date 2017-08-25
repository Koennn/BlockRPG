package me.koenn.blockrpg.battle;

import me.koenn.blockrpg.items.ItemStack;
import me.koenn.blockrpg.items.ItemType;
import net.dv8tion.jda.core.entities.Message;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public enum CreatureType {

    SCARY_MONSTER("Scary Monster", new ItemStack(ItemType.BASIC_SWORD));

    private final String name;
    private final ItemStack weapon;

    CreatureType(String name, ItemStack weapon) {
        this.name = name;
        this.weapon = weapon;
    }

    public String getName() {
        return name;
    }

    public Message doMove(Battle battle) {
        return weapon.getType().getActions()[0].executeCreature(this, battle.getUser(), battle);
    }
}
