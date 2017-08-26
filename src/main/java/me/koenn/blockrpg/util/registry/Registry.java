package me.koenn.blockrpg.util.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public final class Registry<T> {

    private static final Random random = new Random(System.nanoTime());
    private final List<T> registeredObjects;
    private final RegistryKey<T> registryKey;

    public Registry() {
        this.registeredObjects = new ArrayList<>();
        this.registryKey = Object::toString;
    }

    public Registry(RegistryKey<T> registryKey) {
        this.registeredObjects = new ArrayList<>();
        this.registryKey = registryKey;
    }

    public T register(T object) {
        this.registeredObjects.add(object);
        return object;
    }

    public void unregister(T object) {
        if (this.registeredObjects.contains(object)) {
            this.registeredObjects.remove(object);
        }
    }

    public T get(String string) {
        if (string == null || this.registeredObjects.isEmpty()) {
            return null;
        }

        for (T object : this.registeredObjects) {
            if (registryKey.getKey(object).equalsIgnoreCase(string)) {
                return object;
            }
        }
        return null;
    }

    public T getRandom() {
        return this.registeredObjects.get(random.nextInt(this.registeredObjects.size()));
    }

    public List<T> getRegisteredObjects() {
        return registeredObjects;
    }

    public void clear() {
        this.registeredObjects.clear();
    }
}
