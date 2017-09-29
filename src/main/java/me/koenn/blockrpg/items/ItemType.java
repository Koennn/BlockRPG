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

    private static final HashMap<String, ItemType> ITEMS = new HashMap<>();

    private final String id;
    private final String name;
    private final String emote;
    private final String description;
    private final int value;
    private IWeaponAction[] actions;
    private IItemAction itemAction;

    public ItemType(String id, String name, String emote, String description, int value) {
        this.id = id;
        this.name = name;
        this.emote = emote;
        this.description = description;
        this.value = value;
    }

    public ItemType(String id, String name, String emote, String description, int value, IWeaponAction[] actions) {
        this(id, name, emote, description, value);
        this.actions = actions;
    }

    public ItemType(String id, String name, String emote, String description, int value, IItemAction itemAction) {
        this(id, name, emote, description, value);
        this.itemAction = itemAction;
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

    public static HashMap<String, ItemType> getITEMS() {
        return ITEMS;
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

    public String getDescription() {
        return description;
    }

    public int getValue() {
        return value;
    }

    public IWeaponAction[] getActions() {
        return actions;
    }

    public IItemAction getItemAction() {
        return itemAction;
    }

    public boolean hasItemAction() {
        return this.itemAction != null;
    }
}
