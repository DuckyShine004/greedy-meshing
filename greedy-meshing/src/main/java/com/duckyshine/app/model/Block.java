package com.duckyshine.app.model;

import java.util.Arrays;

import org.joml.Vector3i;

import com.duckyshine.app.math.Direction;

public class Block {
    private final int FACES = 6;

    private final Vector3i position;

    private boolean[] isFaceActive;

    public Block(Vector3i position) {
        this.position = position;

        this.initialise();
    }

    public Block(int x, int y, int z) {
        this.position = new Vector3i(x, y, z);

        this.initialise();
    }

    public void initialise() {
        this.isFaceActive = new boolean[FACES];

        Arrays.fill(this.isFaceActive, true);
    }

    public boolean isFaceActive(Direction direction) {
        return this.isFaceActive[direction.getIndex()];
    }

    public void setFaceStatus(Direction direction, boolean status) {
        this.isFaceActive[direction.getIndex()] = status;
    }

    public Vector3i getPosition() {
        return this.position;
    }
}
