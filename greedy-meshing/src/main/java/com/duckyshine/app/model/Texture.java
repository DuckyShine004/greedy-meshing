package com.duckyshine.app.model;

import static org.lwjgl.opengl.GL30.*;

import com.duckyshine.app.math.Direction;

public class Texture {
    private final int WIDTH = 16;
    private final int HEIGHT = 16;

    private final int NUMBER_OF_BLOCK_TYPES = 1;

    private final float[][] coordinates = { { 1.0f } };

    private int textureId;

    private Direction direction;

    private boolean[] isAtlasCreated;

    public Texture(Direction direction) {
        this.direction = direction;
    }
}
