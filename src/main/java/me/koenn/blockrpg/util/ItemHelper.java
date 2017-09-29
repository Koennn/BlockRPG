package me.koenn.blockrpg.util;

import me.koenn.blockrpg.items.ItemType;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, September 2017
 */
public final class ItemHelper {

    public static ItemType findItem(int value, ItemType not) {
        ItemType item = null;
        for (ItemType type : ItemType.getITEMS().values()) {
            if (item == null || Math.abs(value - type.getValue()) < Math.abs(value - item.getValue())) {
                item = type;
            }
        }
        if (item == null) {
            throw new IllegalArgumentException("Unable to find item for given value");
        }

        if (not != null && item.equals(not)) {
            return ItemType.getItem("cookie");
        }

        return item;
    }
}
