package com.duckyshine.app.model;

import java.util.Arrays;

import com.duckyshine.app.math.Direction;

public class Texture {
    private final float[][] COORDINATES = {
            {
                    0.0f, 1.0f,
                    0.0f, 0.66f,
                    0.5f, 0.66f,
                    0.5f, 1.0f
            },
            {
                    0.5f, 1.0f,
                    0.5f, 0.66f,
                    1.0f, 0.66f,
                    1.0f, 1.0f

            },
            {
                    0.0f, 0.66f,
                    0.0f, 0.33f,
                    0.5f, 0.33f,
                    0.5f, 0.66f
            },
            {
                    0.5f, 0.66f,
                    0.5f, 0.33f,
                    1.0f, 0.33f,
                    1.0f, 0.66f
            },
            {
                    0.0f, 0.33f,
                    0.0f, 0.0f,
                    0.5f, 0.0f,
                    0.5f, 0.33f

            },
            {
                    0.5f, 0.33f,
                    0.5f, 0.0f,
                    1.0f, 0.0f,
                    1.0f, 0.33f
            }
    };

    private int id;

    private float[] coordinates;

    public Texture(Direction direction, int id) {
        this.id = id;

        this.coordinates = copyCoordinates(direction);
    }

    public float[] copyCoordinates(Direction direction) {
        coordinates = this.COORDINATES[direction.getIndex()];

        return Arrays.copyOf(coordinates, coordinates.length);
    }

    public int getId() {
        return this.id;
    }

    public float[] getCoordinates() {
        return this.coordinates;
    }
}
