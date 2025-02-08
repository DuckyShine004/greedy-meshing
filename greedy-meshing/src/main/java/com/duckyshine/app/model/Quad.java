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
        // this.indices = this.INDICES[direction.getIndex()];

        // this.vertices = this.VERTICES[direction.getIndex()];
        int dirIndex = direction.getIndex();
        this.indices = this.INDICES[dirIndex]; // If these arrays never change, referencing them is acceptable.

        // Create a new copy of the vertex array so that modifications are local to this
        // quad.
        float[] baseVertices = this.VERTICES[dirIndex];
        this.vertices = new float[baseVertices.length];
        System.arraycopy(baseVertices, 0, this.vertices, 0, baseVertices.length);

        this.convertLocalToGlobalPosition(position);

        // this.convertLocalToGlobalPosition(position);
    }

    public void convertLocalToGlobalPosition(Vector3i position) {
        for (int i = 0; i < this.vertices.length; i += 3) {
            this.vertices[i] += (float) position.x;
            this.vertices[i + 1] += (float) position.y;
            this.vertices[i + 2] += (float) position.z;
        }
    }

    public int[] getIndices() {
        return this.indices;
    }

    public float[] getVertices() {
        return this.vertices;
    }
}
