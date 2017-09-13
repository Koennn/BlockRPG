package me.koenn.blockrpg.world;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, June 2017
 */
public enum Direction {

    NORTH(0, -1), SOUTH(0, 1), EAST(1, 0), WEST(-1, 0);

    private final int x;
    private final int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 move(Vector2 location) {
        return new Vector2(location.x + this.x, location.y + this.y);
    }
}
