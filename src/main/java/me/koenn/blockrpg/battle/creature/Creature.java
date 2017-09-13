package me.koenn.blockrpg.battle.creature;

import me.koenn.blockrpg.items.IWeaponAction;
import me.koenn.blockrpg.items.ItemStack;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, June 2017
 */
public class Creature {

    private final CreatureType type;
    private final int maxHealth;
    private final ItemStack weapon;
    private int health;

    private Creature(CreatureType type, ItemStack weapon) {
        this.type = type;
        this.health = type.getHealth();
        this.maxHealth = type.getHealth();
        this.weapon = weapon;
    }

    public static Creature create(CreatureType type) {
        return new Creature(type, type.getWeapon());
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

    public IWeaponAction getAction() {
        return this.weapon.getType().getActions()[0];
    }
}
