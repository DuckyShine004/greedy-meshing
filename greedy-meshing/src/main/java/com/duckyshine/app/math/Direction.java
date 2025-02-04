package com.duckyshine.app.math;

public enum Direction {
    TOP(0, 1, 0),
    BOTTOM(0, -1, 0),
    LEFT(-1, 0, 0),
    RIGHT(1, 0, 0),
    FRONT(0, 0, 1),
    BACK(0, 0, -1);

    protected final int dx;
    protected final int dy;
    protected final int dz;

    private Direction(int dx, int dy, int dz) {
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }
}
