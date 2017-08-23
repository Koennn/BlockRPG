package me.koenn.blockrpg.world;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public class Vector2 {

    public int x;
    public int y;

    public Vector2() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector2)) {
            return false;
        }
        Vector2 vector2 = (Vector2) obj;
        return vector2.x == this.x && vector2.y == this.y;
    }

    @Override
    public String toString() {
        return x + ", " + y;
    }

    @Override
    public Vector2 clone() {
        return new Vector2(this.x, this.y);
    }
}
