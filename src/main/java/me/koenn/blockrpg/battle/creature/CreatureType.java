package me.koenn.blockrpg.battle.creature;

import com.flowpowered.nbt.CompoundTag;
import com.flowpowered.nbt.stream.NBTInputStream;
import me.koenn.blockrpg.battle.Battle;
import me.koenn.blockrpg.image.Texture;
import me.koenn.blockrpg.items.ItemStack;
import me.koenn.blockrpg.util.NBTHelper;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.utils.SimpleLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, June 2017
 */
public class CreatureType {

    public static final HashMap<String, CreatureType> CREATURES = new HashMap<>();
    private final String name;
    private final ItemStack weapon;
    private final Texture texture;
    private final int health;
    private final LootTable lootTable;
    public CreatureType(String name, ItemStack weapon, Texture texture, int health, LootTable lootTable) {
        this.name = name;
        this.weapon = weapon;
        this.texture = texture;
        this.health = health;
        this.lootTable = lootTable;
    }

    public static void loadCreatures(File directory) throws IOException {
        for (File file : directory.listFiles()) {
            if (!file.getName().endsWith(".cr")) {
                continue;
            }

            NBTInputStream stream = new NBTInputStream(new FileInputStream(file));
            String name = file.getName().replace(".cr", "");
            CREATURES.put(name, NBTHelper.parseCreature((CompoundTag) stream.readTag()));
            SimpleLog.getLog("CreatureLoader").info(String.format("Loaded creature \'%s\'!", name));
        }
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

    public ItemStack getWeapon() {
        return weapon;
    }
}
