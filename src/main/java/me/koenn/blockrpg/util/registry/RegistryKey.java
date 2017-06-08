package me.koenn.blockrpg.util.registry;

public interface RegistryKey<T> {

    String getKey(T object);
}
