package me.koenn.blockrpg.data;

import me.koenn.blockrpg.items.Inventory;
import me.koenn.blockrpg.items.ItemStack;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public class Stats {

    private int health;
    private Inventory inventory;
    private ItemStack weapon;
    private ItemStack[] armor;
    private int level;
    private int kills;
    private int deaths;
    private int takedowns;
    private SkillPoints skillPoints;

    public Stats() {
        this.inventory = new Inventory();
        this.armor = new ItemStack[4];
    }

    public Stats(int health, Inventory inventory, ItemStack weapon, ItemStack[] armor, int level, int kills, int deaths, int takedowns, SkillPoints skillPoints) {
        this.health = health;
        this.inventory = inventory;
        this.weapon = weapon;
        this.armor = armor;
        this.level = level;
        this.kills = kills;
        this.deaths = deaths;
        this.takedowns = takedowns;
        this.skillPoints = skillPoints;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public ItemStack getWeapon() {
        return weapon;
    }

    public void setWeapon(ItemStack weapon) {
        this.weapon = weapon;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getTakedowns() {
        return takedowns;
    }

    public void setTakedowns(int takedowns) {
        this.takedowns = takedowns;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public SkillPoints getSkillPoints() {
        return skillPoints;
    }
}
