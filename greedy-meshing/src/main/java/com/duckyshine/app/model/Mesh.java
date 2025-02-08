package com.duckyshine.app.model;

import java.util.List;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.joml.Vector3i;
import org.lwjgl.BufferUtils;

import com.duckyshine.app.math.Direction;

import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    private int vertexArrayId;

    private int indexBufferId;
    private int vertexBufferId;

    private List<Quad> quads;

    private List<Float> vertices;

    private List<Integer> indices;

    public Mesh() {
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

        this.setupBuffers(vertices, indices);
    }

    private void setupBuffers(float[] vertices, int[] indices) {
        this.vertexArrayId = glGenVertexArrays();

        this.vertexBufferId = glGenBuffers();
        this.indexBufferId = glGenBuffers();

        glBindVertexArray(this.vertexArrayId);

        glBindBuffer(GL_ARRAY_BUFFER, this.vertexBufferId);
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.indexBufferId);
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.length);
        indexBuffer.put(indices).flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindVertexArray(0);
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

    public void render() {
        this.build();

        glBindVertexArray(this.vertexArrayId);

        glDrawElements(GL_TRIANGLES, this.indices.size(), GL_UNSIGNED_INT, 0);

        glBindVertexArray(0);
    }
}
