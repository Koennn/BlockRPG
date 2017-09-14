package me.koenn.blockrpg.util;

import com.flowpowered.nbt.*;
import me.koenn.blockrpg.battle.creature.CreatureType;
import me.koenn.blockrpg.battle.creature.LootTable;
import me.koenn.blockrpg.image.Texture;
import me.koenn.blockrpg.items.IItemAction;
import me.koenn.blockrpg.items.IWeaponAction;
import me.koenn.blockrpg.items.ItemStack;
import me.koenn.blockrpg.items.ItemType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, August 2017
 */
public final class NBTHelper {

    private static final String ITEM_ACTION_PACKAGE = "me.koenn.blockrpg.items.";
    private static final String WEAPON_ACTION_PACKAGE = "me.koenn.blockrpg.items.weapon.";

    public static ItemType parseItem(CompoundTag tag) {
        String name = getChildTag(tag.getValue(), "name", StringTag.class).getValue();
        String emote = getChildTag(tag.getValue(), "emote", StringTag.class).getValue();
        String description = getChildTag(tag.getValue(), "description", StringTag.class).getValue();
        String actionType = getChildTag(tag.getValue(), "action_type", StringTag.class).getValue();

        switch (actionType) {
            case "item":
                return new ItemType(tag.getName(), name, emote, description, parseItemAction(getChildTag(tag.getValue(), "action", CompoundTag.class)));
            case "weapon":
                return new ItemType(tag.getName(), name, emote, description, parseWeaponActions(getChildTag(tag.getValue(), "actions", ListTag.class)));
            default:
                return new ItemType(tag.getName(), name, emote, description);
        }
    }

    public static IWeaponAction[] parseWeaponActions(ListTag<StringTag> tag) {
        List<IWeaponAction> weaponActions = new ArrayList<>();
        for (StringTag actionName : tag.getValue()) {
            weaponActions.add((IWeaponAction) ReflectionHelper.newInstance(ReflectionHelper.getClass(WEAPON_ACTION_PACKAGE + actionName.getValue()), null));
        }
        return weaponActions.toArray(new IWeaponAction[weaponActions.size()]);
    }

    public static IItemAction parseItemAction(CompoundTag tag) {
        String type = getChildTag(tag.getValue(), "type", StringTag.class).getValue();
        CompoundTag paramsTag = getChildTag(tag.getValue(), "params", CompoundTag.class);
        Object[] params = new Object[paramsTag.getValue().size()];
        for (int i = 0; i < params.length; i++) {
            params[i] = paramsTag.getValue().get(String.valueOf(i)).getValue();
        }
        return (IItemAction) ReflectionHelper.newInstance(ReflectionHelper.getClass(ITEM_ACTION_PACKAGE + type), params);
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

    public static LootTable parseLoot(ListTag<CompoundTag> tag) {
        List<ItemStack> loot = new ArrayList<>();
        tag.getValue().forEach(compoundTag -> loot.add(parseItemStack(compoundTag)));
        return () -> loot.get(ThreadLocalRandom.current().nextInt(loot.size()));
    }

    public static ItemStack parseItemStack(CompoundTag tag) {
        int amount = getChildTag(tag.getValue(), "amount", IntTag.class).getValue();
        ItemType type = ItemType.getItem(getChildTag(tag.getValue(), "name", StringTag.class).getValue());
        return new ItemStack(type, amount);
    }

    public static <T extends Tag> T getChildTag(CompoundMap items, String key, Class<T> expected) throws IllegalArgumentException {
        if (!items.containsKey(key)) {
            throw new IllegalArgumentException(String.format("NBT file is missing a \'%s\' tag!", key));
        }
        Tag tag = items.get(key);
        if (!expected.isInstance(tag)) {
            throw new IllegalArgumentException(String.format("\'%s\' tag is not of type \'%s\'", key, expected.getName()));
        }
        return expected.cast(tag);
    }
}
