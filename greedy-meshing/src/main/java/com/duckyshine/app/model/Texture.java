package com.duckyshine.app.model;

import java.util.Arrays;

import com.duckyshine.app.math.Direction;

public class Texture {
    private final float[][] COORDINATES = {
            {
                    0.0f, 1.0f,
                    0.0f, 2.0f / 3.0f,
                    0.5f, 2.0f / 3.0f,
                    0.5f, 1.0f
            },
            {
                    0.5f, 1.0f,
                    0.5f, 2.0f / 3.0f,
                    1.0f, 2.0f / 3.0f,
                    1.0f, 1.0f

            },
            {
                    0.0f, 2.0f / 3.0f,
                    0.0f, 1.0f / 3.0f,
                    0.5f, 1.0f / 3.0f,
                    0.5f, 2.0f / 3.0f
            },
            {
                    0.5f, 2.0f / 3.0f,
                    0.5f, 1.0f / 3.0f,
                    1.0f, 1.0f / 3.0f,
                    1.0f, 2.0f / 3.0f
            },
            {
                    0.0f, 1.0f / 3.0f,
                    0.0f, 0.0f,
                    0.5f, 0.0f,
                    0.5f, 1.0f / 3.0f

            },
            {
                    0.5f, 1.0f / 3.0f,
                    0.5f, 0.0f,
                    1.0f, 0.0f,
                    1.0f, 1.0f / 3.0f
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

        coordinates = Arrays.copyOf(coordinates, coordinates.length);

        for (int i = 1; i < coordinates.length; i += 2) {
            coordinates[i] = 1.0f - coordinates[i];
        }

        return coordinates;
    }

    public int getId() {
        return this.id;
    }

    public float[] getCoordinates() {
        return this.coordinates;
    }
}
