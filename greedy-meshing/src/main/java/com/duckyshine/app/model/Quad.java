package com.duckyshine.app.model;

import java.util.Arrays;

import org.joml.Vector3i;

import com.duckyshine.app.debug.Debug;
import com.duckyshine.app.math.Direction;

public class Quad {
    private final int[][] INDICES = {
            { 0, 1, 2, 2, 3, 0 },
            { 0, 1, 2, 2, 3, 0 },
            { 0, 1, 2, 2, 3, 0 },
            { 0, 1, 2, 2, 3, 0 },
            { 0, 1, 2, 2, 3, 0 },
            { 0, 1, 2, 2, 3, 0 }
    };

    private final float[][] VERTICES = {
            {
                    0.0f, 1.0f, 0.0f,
                    1.0f, 1.0f, 0.0f,
                    1.0f, 1.0f, 1.0f,
                    0.0f, 1.0f, 1.0f
            },
            {
                    0.0f, 0.0f, 0.0f,
                    1.0f, 0.0f, 0.0f,
                    1.0f, 0.0f, 1.0f,
                    0.0f, 0.0f, 1.0f
            },
            {
                    0.0f, 0.0f, 0.0f,
                    0.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 1.0f,
                    0.0f, 1.0f, 0.0f
            },
            {
                    1.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f,
                    1.0f, 1.0f, 0.0f,
                    1.0f, 1.0f, 1.0f
            },
            {
                    0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    0.0f, 1.0f, 1.0f
            },
            {
                    1.0f, 0.0f, 0.0f,
                    0.0f, 0.0f, 0.0f,
                    0.0f, 1.0f, 0.0f,
                    1.0f, 1.0f, 0.0f
            }
    };

    private int[] indices;

    private float[] vertices;

    private Texture texture;

    private Direction direction;

    public Quad(Vector3i position, Direction direction, Texture texture) {
        this.direction = direction;

        this.indices = this.INDICES[direction.getIndex()];

        this.vertices = this.copyVertices();

        this.texture = texture;

        this.convertLocalToGlobalPosition(position);
    }

    public void convertLocalToGlobalPosition(Vector3i position) {
        for (int i = 0; i < this.vertices.length; i += 3) {
            this.vertices[i] += (float) position.x;
            this.vertices[i + 1] += (float) position.y;
            this.vertices[i + 2] += (float) position.z;
        }
    }

    public float[] copyVertices() {
        float[] vertices = this.VERTICES[this.direction.getIndex()];

        return Arrays.copyOf(vertices, vertices.length);
    }

    public int[] getIndices() {
        return this.indices;
    }

    public float[] getVertices() {
        return this.vertices;
    }

    public Texture getTexture() {
        return this.texture;
    }
}
