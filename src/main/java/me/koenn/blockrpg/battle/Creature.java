package me.koenn.blockrpg.battle;

import me.koenn.blockrpg.items.ItemStack;
import me.koenn.blockrpg.items.WeaponAction;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public class Creature {

    private final CreatureType type;
    private int health;
    private final int maxHealth;
    private final ItemStack weapon;

    public Creature(CreatureType type, int maxHealth, ItemStack weapon) {
        this.type = type;
        this.health = maxHealth;
        this.maxHealth = maxHealth;
        this.weapon = weapon;
    }

    public CreatureType getType() {
        return type;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public ItemStack getWeapon() {
        return weapon;
    }

    public WeaponAction getAction() {
        return this.weapon.getType().getActions()[0];
    }
}
