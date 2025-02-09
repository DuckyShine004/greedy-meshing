package com.duckyshine.app.math;

import org.joml.Vector3i;

public enum Direction {
    TOP(0, 1, 0, 0),
    BOTTOM(0, -1, 0, 1),
    LEFT(-1, 0, 0, 2),
    RIGHT(1, 0, 0, 3),
    FRONT(0, 0, 1, 4),
    BACK(0, 0, -1, 5);

    private final int index;

    private final Vector3i direction;

    private Direction(int dx, int dy, int dz, int index) {
        this.index = index;

        this.direction = new Vector3i(dx, dy, dz);
    }

    public Vector3i get() {
        return this.direction;
    }

    public int getIndex() {
        return this.index;
    }

    public String getName() {
        return this.name().toLowerCase();
    }
}
