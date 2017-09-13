package me.koenn.blockrpg.util;

import com.flowpowered.nbt.*;
import me.koenn.blockrpg.battle.creature.CreatureType;
import me.koenn.blockrpg.battle.creature.LootTable;
import me.koenn.blockrpg.image.Texture;
import me.koenn.blockrpg.items.IItemAction;
import me.koenn.blockrpg.items.ItemStack;
import me.koenn.blockrpg.items.ItemType;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, August 2017
 */
public final class NBTHelper {

    public static ItemType parseItem(CompoundTag tag) {
        String name = getChildTag(tag.getValue(), "name", StringTag.class).getValue();
        String emote = getChildTag(tag.getValue(), "emote", StringTag.class).getValue();
        String description = getChildTag(tag.getValue(), "description", StringTag.class).getValue();
        String actionType = getChildTag(tag.getValue(), "action_type", StringTag.class).getValue();

        switch (actionType) {
            case "item":
                return new ItemType(name, emote, description, parseItemAction(getChildTag(tag.getValue(), "action", CompoundTag.class)));
            case "weapon":
                return null;
            default:
                return null;
        }

    }

    public static IItemAction parseItemAction(CompoundTag tag) {
        String type = getChildTag(tag.getValue(), "type", StringTag.class).getValue();
        CompoundTag paramsTag = getChildTag(tag.getValue(), "params", CompoundTag.class);
        Object[] params = new Object[paramsTag.getValue().size()];
        for (int i = 0; i < params.length; i++) {
            params[i] = paramsTag.getValue().get(String.valueOf(i)).getValue();
        }
        return (IItemAction) ReflectionHelper.newInstance(ReflectionHelper.getClass("me.koenn.blockrpg.items." + type), params);
    }

    public static CreatureType parseCreature(CompoundTag tag) {
        ItemStack weapon = parseItemStack(getChildTag(tag.getValue(), "weapon", CompoundTag.class));
        Texture texture = parseTexture(getChildTag(tag.getValue(), "texture", CompoundTag.class));
        String name = getChildTag(tag.getValue(), "name", StringTag.class).getValue();
        int health = getChildTag(tag.getValue(), "health", IntTag.class).getValue();
        LootTable lootTable = parseLoot(getChildTag(tag.getValue(), "loot", ListTag.class));
        return new CreatureType(name, weapon, texture, health, lootTable);
    }

    public static Texture parseTexture(CompoundTag tag) {
        String texture = getChildTag(tag.getValue(), "texture", StringTag.class).getValue();
        String label = getChildTag(tag.getValue(), "label", StringTag.class).getValue();
        return new Texture(label, texture);
    }

    public static LootTable parseLoot(ListTag tag) {
        return () -> null;
    }

    public static ItemStack parseItemStack(CompoundTag tag) {
        int amount = getChildTag(tag.getValue(), "amount", IntTag.class).getValue();
        ItemType type = ItemType.getItem(getChildTag(tag.getValue(), "name", StringTag.class).getValue());
        return new ItemStack(type, amount);
    }

    public static <T extends Tag> T getChildTag(CompoundMap items, String key, Class<T> expected) throws IllegalArgumentException {
        if (!items.containsKey(key)) {
            throw new IllegalArgumentException("NBT file is missing a \"" + key + "\" tag");
        }
        Tag tag = items.get(key);
        if (!expected.isInstance(tag)) {
            throw new IllegalArgumentException(key + " tag is not of tag type " + expected.getName());
        }
        return expected.cast(tag);
    }
}
