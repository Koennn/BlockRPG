package me.koenn.blockrpg.util;

import java.util.HashMap;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, August 2017
 */
public class Cache<K, V> {

    private final HashMap<K, V> data;

    public Cache() {
        this.data = new HashMap<>();
    }

    public void put(K key, V value) {
        this.data.put(key, value);
    }

    public boolean isCached(K key) {
        return this.data.containsKey(key);
    }

    public V get(K key) {
        return this.data.get(key);
    }

    public void clearCache(K key) {
        this.data.remove(key);
    }
}
