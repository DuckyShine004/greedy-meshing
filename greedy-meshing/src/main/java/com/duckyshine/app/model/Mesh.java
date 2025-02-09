package com.duckyshine.app.model;

import java.util.List;
import java.util.ArrayList;

import org.joml.Vector3i;

import com.duckyshine.app.math.Direction;
import com.duckyshine.app.math.noise.Noise;

import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    private Buffer buffer;

    private List<Quad> quads;

    private List<Float> vertices;
    private List<Float> coordinates;

    private List<Integer> indices;

    private int[][] heightMap;

    public Mesh() {
        this.buffer = new Buffer();

        this.quads = new ArrayList<>();

        this.vertices = new ArrayList<>();
        this.coordinates = new ArrayList<>();

        this.indices = new ArrayList<>();
    }

    public void generateDepthMap(Chunk chunk) {
        int width = chunk.getWidth();
        int depth = chunk.getDepth();

        this.heightMap = new int[width][depth];

        for (int x = 0; x < width; x++) {
            for (int z = 0; z < depth; z++) {
                double nx = (double) x / width - 0.5d;
                double nz = (double) z / depth - 0.5d;
                this.heightMap[x][z] = Noise.getNoise2d(nx, nz);
            }
        }
    }

    public void generate(Chunk chunk) {
        if (this.heightMap == null) {
            this.generateDepthMap(chunk);
        }

        for (int x = 0; x < chunk.getWidth(); x++) {
            for (int z = 0; z < chunk.getDepth(); z++) {
                int y = this.heightMap[x][z];

                chunk.addBlock(x, y, z, BlockType.GRASS);
            }
        }
        // for (int x = 0; x < chunk.getWidth(); x++) {
        // for (int y = 0; y < chunk.getHeight(); y++) {
        // for (int z = 0; z < chunk.getDepth(); z++) {
        // chunk.addBlock(x, y, z);
        // }
        // }
        // }
    }

    public void addBlock(Block block) {
        Vector3i position = block.getPosition();

        BlockType blockType = block.getBlockType();

        for (Direction direction : Direction.values()) {
            if (block.isFaceActive(direction)) {
                this.addQuad(position, direction, blockType);
            }
        }
    }

    public void addQuad(Vector3i position, Direction direction, BlockType blockType) {
        int textureId = blockType.getIndex();

        Texture texture = new Texture(direction, textureId);

        Quad quad = new Quad(position, direction, texture);

        this.quads.add(quad);
    }

    public void build() {
        int[] indices = this.getMergedIndices();

        float[] vertices = this.getMergedVertices();

        float[] coordinates = this.getMergedCoordinates();

        this.buffer.setup(vertices, indices, coordinates);
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

    private float[] getMergedCoordinates() {
        float[] coordinates;

        this.coordinates.clear();

        for (Quad quad : this.quads) {
            Texture texture = quad.getTexture();

            for (float coordinate : texture.getCoordinates()) {
                this.coordinates.add(coordinate);
            }
        }

        coordinates = new float[this.coordinates.size()];

        for (int i = 0; i < this.coordinates.size(); i++) {
            coordinates[i] = this.coordinates.get(i);
        }

        return coordinates;
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
