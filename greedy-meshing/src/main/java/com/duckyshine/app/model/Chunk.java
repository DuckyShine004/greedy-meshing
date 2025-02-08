package com.duckyshine.app.model;

import org.joml.Vector3i;

public class Chunk {
    private final int WIDTH = 16;
    private final int DEPTH = 16;
    private final int HEIGHT = 16;

    private final Vector3i position;

    private Block[][][] blocks;

    private Mesh mesh;

    public Chunk(Vector3i position) {
        this.position = position;

        this.initialise();
    }

    public Chunk(int x, int y, int z) {
        this.position = new Vector3i(x, y, z);

        this.initialise();
    }

    public void initialise() {
        this.blocks = new Block[WIDTH][HEIGHT][DEPTH];

        this.mesh = new Mesh();
    }

    public void generate() {
        this.mesh.generate(this);

        for (int x = 0; x < this.WIDTH; x++) {
            for (int y = 0; y < this.HEIGHT; y++) {
                for (int z = 0; z < this.DEPTH; z++) {
                    if (this.isBlockActive(x, y, z)) {
                        this.mesh.addBlock(this.blocks[x][y][z]);
                    }
                }
            }
        }
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Mesh getMesh() {
        return this.mesh;
    }

    public boolean isBlockActive(int x, int y, int z) {
        return this.blocks[x][y][z] != null;
    }

    public boolean isBlockActive(Vector3i position) {
        return this.isBlockActive(position.x, position.y, position.z);
    }

    public void addBlock(Vector3i position) {
        this.addBlock(position.x, position.y, position.z);
    }

    public void addBlock(int x, int y, int z) {
        this.blocks[x][y][z] = new Block(x, y, z);
    }

    public Block getBlock(int x, int y, int z) {
        return this.blocks[x][y][z];
    }

    public Block getBlock(Vector3i position) {
        return this.getBlock(position.x, position.y, position.z);
    }

    public Vector3i getPosition() {
        return this.position;
    }

    public int getWidth() {
        return this.WIDTH;
    }

    public int getHeight() {
        return this.HEIGHT;
    }

    public int getDepth() {
        return this.DEPTH;
    }
}
