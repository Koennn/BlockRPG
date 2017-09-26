package me.koenn.blockrpg.image;

import me.koenn.blockrpg.util.Cache;
import me.koenn.blockrpg.util.WorldHelper;
import me.koenn.blockrpg.world.Vector2;
import me.koenn.blockrpg.world.World;
import net.dv8tion.jda.core.entities.User;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, June 2017
 */
public class MapGenerator {

    public static final Cache<User, String> cachedMaps = new Cache<>();

    private static Texture HOME_TILE;
    private static Texture EXPLORED_TILE;
    private static Texture UNEXPLORED_TILE;
    private static Texture CURRENT_TILE;
    private final World world;
    private final Vector2 center;

    public MapGenerator(World world, User owner) {
        this.world = world;
        this.center = WorldHelper.getUserLocation(owner);
    }

    public static void loadTextures() {
        HOME_TILE = new Texture("home_tile", "home_tile.png");
        EXPLORED_TILE = new Texture("explored_tile", "explored_tile.png");
        UNEXPLORED_TILE = new Texture("unexplored_tile", "unexplored_tile.png");
        CURRENT_TILE = new Texture("current_tile", "current_tile.png");
    }

    public String generate(User owner) {
        return this.generate(owner, false);
    }

    public String generate(User owner, boolean clearCache) {
        if (clearCache) {
            MapGenerator.cachedMaps.clearCache(owner);
        } else if (cachedMaps.isCached(owner)) {
            return cachedMaps.get(owner);
        }

        ImageGenerator generator = new ImageGenerator(503, 503);
        int realX, realY = 3;
        for (int y = 0; y < 10; y++) {
            realX = 3;
            for (int x = 0; x < 10; x++) {
                Vector2 tile = translate(realX, realY);
                generator.draw(realX, realY, getTileTexture(tile));
                realX += 50;
            }
            realY += 50;
        }
        String image = generator.generate(owner);
        cachedMaps.put(owner, image);
        return image;
    }

    private Vector2 translate(int x, int y) {
        int cornerX = this.center.x - 4;
        int cornerY = this.center.y - 4;
        int scaledX = (x / 50) + cornerX;
        int scaledY = (y / 50) + cornerY;
        return new Vector2(scaledX, scaledY);
    }

    private Texture getTileTexture(Vector2 tile) {
        if (tile.equals(this.center)) {
            return CURRENT_TILE;
        } else if (world.getTile(tile) != null && world.getTile(tile).getProperty("homeTile") != null) {
            return HOME_TILE;
        } else if (this.world.isExplored(tile)) {
            return EXPLORED_TILE;
        } else {
            return UNEXPLORED_TILE;
        }
    }
}
