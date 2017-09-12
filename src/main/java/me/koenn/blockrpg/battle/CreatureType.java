package me.koenn.blockrpg.battle;

import me.koenn.blockrpg.image.Texture;
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

    SCARY_MONSTER("Scary Monster", new ItemStack(ItemType.BASIC_SWORD), new Texture("scary_monster", "scary_monster.png"), 10, () -> new ItemStack(ItemType.COOKIE));

    private final String name;
    private final ItemStack weapon;
    private final Texture texture;
    private final int health;
    private final LootTable lootTable;

    CreatureType(String name, ItemStack weapon, Texture texture, int health, LootTable lootTable) {
        this.name = name;
        this.weapon = weapon;
        this.texture = texture;
        this.health = health;
        this.lootTable = lootTable;
    }

    public String getName() {
        return name;
    }

    public Message doMove(Battle battle) {
        return weapon.getType().getActions()[0].executeCreature(this, battle.getUser(), battle);
    }

    public Texture getTexture() {
        return texture;
    }

    public int getHealth() {
        return health;
    }

    public LootTable getLootTable() {
        return lootTable;
    }
}
