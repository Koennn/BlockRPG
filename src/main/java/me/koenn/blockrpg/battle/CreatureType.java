package me.koenn.blockrpg.battle;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public enum CreatureType {

    SCARY_MONSTER("Scary Monster");

    private final String name;

    CreatureType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
