package me.koenn.blockrpg.items;

import com.flowpowered.nbt.CompoundTag;
import com.flowpowered.nbt.stream.NBTInputStream;
import me.koenn.blockrpg.util.NBTHelper;
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
public class ItemType {

    /*TEST_ITEM(
            "Test Item", ":x:",
            "Item used for testing."
    ),

    COOKIE(
            "Cookie", ":cookie:",
            "MissChikoo's favorite.\nContains high quantities of chocolate\nRestores 10 hp when eaten.\nDo not feed to parrots.",
            new FoodItemAction("Cookie", 10)
    ),

    LOLLIPOP(
            "Lollipop", ":lollipop:",
            "A sweet lollipop, nicely red and yellow.\nRestores 5 hp when eaten.\nMake sure to brush your teeth afterwards.",
            new FoodItemAction("Cookie", 10)
    ),

    CARROT(
            "Carrot", ":carrot:",
            "A healthy orange carrot.\nRestores 10 hp and gives increased\nvision when eaten.",
            new FoodItemAction("Cookie", 10)
    ),

    WATCH(
            "Watch", ":watch:",
            "An interesting device used to tell time.\nMight have other hidden functions,\nwho knows..."
    ),

    BASIC_SWORD(
            "Basic Sword", ":dagger:",
            "A basic weapon.\nAbilities:\n 1: Swing Sword\n 2: Block Attack",
            new IWeaponAction[]{new ActionSwingSword(), new ActionBlockAttack()}
    ),

    KNIFE(
            "Kitchen Knife", ":knife:",
            "A sharp knife, stolen from the kitchen.\nAbilities:\n 1: Stab",
            new IWeaponAction[]{}
    ),

    SHIELD(
            "Shield", ":shield:",
                    "A sturdy metal shield, used to block attacks.\nAbilities:\n 1: Block Attack",
                    new IWeaponAction[]{}
    );*/

    private static final HashMap<String, ItemType> ITEMS = new HashMap<>();

    private final String id;
    private final String name;
    private final String emote;
    private final String description;
    private IWeaponAction[] actions;
    private IItemAction IItemAction;

    public ItemType(String id, String name, String emote, String description) {
        this.id = id;
        this.name = name;
        this.emote = emote;
        this.description = description;
    }

    public ItemType(String id, String name, String emote, String description, IWeaponAction[] actions) {
        this.id = id;
        this.name = name;
        this.emote = emote;
        this.description = description;
        this.actions = actions;
    }

    public ItemType(String id, String name, String emote, String description, IItemAction IItemAction) {
        this.id = id;
        this.name = name;
        this.emote = emote;
        this.description = description;
        this.IItemAction = IItemAction;
    }

    public static void loadItemTypes(File directory) throws IOException {
        for (File file : directory.listFiles()) {
            if (!file.getName().endsWith(".it")) {
                continue;
            }

            NBTInputStream stream = new NBTInputStream(new FileInputStream(file));
            String name = file.getName().replace(".it", "");
            ITEMS.put(name, NBTHelper.parseItem((CompoundTag) stream.readTag()));
            SimpleLog.getLog("ItemLoader").info(String.format("Loaded item \'%s\'!", name));
        }
    }

    public static ItemType getItem(String name) {
        return ITEMS.get(name.toLowerCase());
    }

    public String getId() {
        return id;
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
