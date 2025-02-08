package com.duckyshine.app.model;

import org.joml.Vector3i;

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
            } };

    private int[] indices;

    private float[] vertices;

    public Quad(Vector3i position, Direction direction) {
        this.indices = this.INDICES[direction.getIndex()];

        this.vertices = this.VERTICES[direction.getIndex()];

        this.convertLocalToGlobalPosition(position);
    }

    public void convertLocalToGlobalPosition(Vector3i position) {
        for (int i = 0; i < this.vertices.length; i++) {
            this.vertices[0] += (float) position.x;
            this.vertices[1] += (float) position.y;
            this.vertices[2] += (float) position.z;
        }
    }

    public int[] getIndices() {
        return this.indices;
    }

    public float[] getVertices() {
        return this.vertices;
    }
}
