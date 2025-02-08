package com.duckyshine.app.model;

import java.util.List;
import java.util.ArrayList;

import org.joml.Vector3i;

import com.duckyshine.app.model.Buffer;

import com.duckyshine.app.math.Direction;

import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    private Buffer buffer;

    private List<Quad> quads;

    private List<Float> vertices;

    private List<Integer> indices;

    public Mesh() {
        this.buffer = new Buffer();

        this.quads = new ArrayList<>();

        this.vertices = new ArrayList<>();

        this.indices = new ArrayList<>();
    }

    public void generate(Chunk chunk) {
        for (int x = 0; x < chunk.getWidth(); x++) {
            for (int y = 0; y < chunk.getHeight(); y++) {
                for (int z = 0; z < chunk.getDepth(); z++) {
                    chunk.addBlock(x, y, z);
                }
            }
        }
    }

    public void addBlock(Block block) {
        Vector3i position = block.getPosition();

        for (Direction direction : Direction.values()) {
            if (block.isFaceActive(direction)) {
                this.addQuad(position, direction);
            }
        }
    }

    public void addQuad(Vector3i position, Direction direction) {
        this.quads.add(new Quad(position, direction));
    }

    public void build() {
        int[] indices = this.getMergedIndices();

        float[] vertices = this.getMergedVertices();

        this.buffer.setup(vertices, indices);
    }

    private int[] getMergedIndices() {
        int offset = 0;

        int[] indices;

        this.indices.clear();

        for (Quad quad : this.quads) {
            for (int index : quad.getIndices()) {
                this.indices.add(index + offset);
            }

            offset += 4;
        }

        indices = new int[this.indices.size()];

        for (int i = 0; i < this.indices.size(); i++) {
            indices[i] = this.indices.get(i);
        }

        return indices;
    }

    private float[] getMergedVertices() {
        float[] vertices;

        this.vertices.clear();

        for (Quad quad : this.quads) {
            for (float vertex : quad.getVertices()) {
                this.vertices.add(vertex);
            }
        }

        vertices = new float[this.vertices.size()];

        for (int i = 0; i < this.vertices.size(); i++) {
            vertices[i] = this.vertices.get(i);
        }

        return vertices;
    }

    public void cleanup() {
        this.buffer.cleanup();
    }

    public void render() {
        this.buffer.bindVertexArray();

        glDrawElements(GL_TRIANGLES, this.indices.size(), GL_UNSIGNED_INT, 0);

        this.buffer.detachVertexArray();
    }
}
