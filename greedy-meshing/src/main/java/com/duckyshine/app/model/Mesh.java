package com.duckyshine.app.model;

import java.util.List;

import javax.transaction.xa.XAException;

import java.util.ArrayList;
import java.util.Arrays;

import org.joml.Vector3i;

import com.duckyshine.app.debug.Debug;
import com.duckyshine.app.math.Direction;
import com.duckyshine.app.math.Math;
import com.duckyshine.app.math.Vector3;
import com.duckyshine.app.math.noise.Noise;

import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    private Buffer buffer;

    private List<Quad> quads;

    private List<Float> vertices;
    private List<Float> coordinates;

    private List<Integer> indices;
    private List<Integer> textures;

    private int[][] heightMap;

    public Mesh() {
        this.buffer = new Buffer();

        this.quads = new ArrayList<>();

        this.vertices = new ArrayList<>();
        this.coordinates = new ArrayList<>();

        this.indices = new ArrayList<>();
        this.textures = new ArrayList<>();
    }

    public void generateHeightMap(Chunk chunk) {
        int width = chunk.getWidth();
        int depth = chunk.getDepth();

        this.heightMap = new int[width][depth];

        for (int x = 0; x < width; x++) {
            for (int z = 0; z < depth; z++) {
                double offsetX = (double) x / width - 0.5d;
                double offsetZ = (double) z / depth - 0.5d;

                this.heightMap[x][z] = Noise.getNoise2d(offsetX, offsetZ);
            }
        }
    }

    public void generate(Chunk chunk) {
        if (this.heightMap == null) {
            this.generateHeightMap(chunk);
        }

        for (int x = 0; x < chunk.getWidth(); x++) {
            for (int z = 0; z < chunk.getDepth(); z++) {
                int y = this.heightMap[x][z];

                chunk.addBlock(x, y, z, BlockType.GRASS);
            }
        }

        this.cull(chunk);

        this.merge(chunk);
    }

    private void cull(Chunk chunk) {
        int width = chunk.getWidth();
        int depth = chunk.getDepth();
        int height = chunk.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int z = 0; z < depth; z++) {
                    if (!chunk.isBlockActive(x, y, z)) {
                        continue;
                    }

                    Block block = chunk.getBlock(x, y, z);

                    this.cullFacesForBlock(chunk, block);
                }
            }
        }
    }

    private void cullFacesForBlock(Chunk chunk, Block block) {
        Vector3i position = block.getPosition();

        for (Direction direction : Direction.values()) {
            int dx = position.x + direction.getX();
            int dy = position.y + direction.getY();
            int dz = position.z + direction.getZ();

            if (chunk.isBlockActive(dx, dy, dz)) {
                block.setFaceStatus(direction, false);
            }
        }
    }

    private int findMaximumHeight(BlockType[][] grid, int x, int y, int height) {
        BlockType blockType = grid[y][x];

        int maximumHeight = 1;

        while (true) {
            int deltaY = y + maximumHeight;

            if (deltaY >= height || grid[deltaY][x] != blockType) {
                break;
            }

            ++maximumHeight;
        }

        return maximumHeight;
    }

    private int findMaximumWidth(BlockType[][] grid, int x, int y, int width, int maximumHeight) {
        BlockType blockType = grid[y][x];

        int maximumWidth = 1;

        while (true) {
            int deltaX = x + maximumWidth;

            if (deltaX >= width) {
                break;
            }

            boolean isValid = true;

            for (int height = 0; height < maximumHeight; height++) {
                if (grid[deltaX][y + height] != blockType) {
                    isValid = false;
                    break;
                }
            }

            if (isValid) {
                ++maximumWidth;
            } else {
                break;
            }
        }

        return maximumWidth;
    }

    private void resetGrid(BlockType[][] grid, int x, int y, int maximumWidth, int maximumHeight) {
        for (int height = 0; height < maximumHeight; height++) {
            for (int width = 0; width < maximumWidth; width++) {
                int deltaX = x + width;
                int deltaY = y + height;

                grid[deltaY][deltaX] = null;
            }
        }
    }

    private void mergeTop(Chunk chunk, int width, int height, int depth) {
        Direction direction = Direction.TOP;

        for (int y = 0; y < height; y++) {
            BlockType[][] grid = this.getGridZ(chunk, y, width, depth);

            for (int x = 0; x < width; x++) {
                for (int z = 0; z < depth; z++) {
                    BlockType blockType = grid[x][z];

                    if (blockType == null) {
                        continue;
                    }

                    int maximumHeight = this.findMaximumHeight(grid, z, x, width);
                    int maximumWidth = this.findMaximumWidth(grid, z, x, depth, maximumHeight);

                    Vector3i position = new Vector3i(x, y, z);

                    this.addQuad(position, direction, blockType, maximumWidth, 1, maximumHeight);

                    this.resetGrid(grid, z, x, maximumWidth, maximumHeight);
                }
            }
        }
    }

    private BlockType[][] getGridZ(Chunk chunk, int y, int width, int depth) {
        BlockType grid[][] = new BlockType[width][depth];

        for (int x = 0; x < width; x++) {
            for (int z = 0; z < depth; z++) {
                if (!chunk.isBlockActive(x, y, z)) {
                    continue;
                }

                Block block = chunk.getBlock(x, y, z);

                if (!block.isFaceActive(Direction.TOP)) {
                    continue;
                }

                grid[x][z] = block.getBlockType();
            }
        }

        return grid;
    }

    private void merge(Chunk chunk) {
        int width = chunk.getWidth();
        int depth = chunk.getDepth();
        int height = chunk.getHeight();

        // this.mergeTop(chunk, width, height, depth);

        for (int y = 0; y < height; y++) {
            BlockType[][] grid = new BlockType[width][depth];

            for (int x = 0; x < width; x++) {
                for (int z = 0; z < depth; z++) {
                    if (grid[x][z] == null) {
                        continue;
                    }

                    int dx = x + 1;
                    // Find largest height in row
                    while (dx < width && grid[dx][z] == grid[x][z]) {
                        ++dx;
                    }

                    int dz = z + 1;

                    while (dz < depth) {
                        boolean f = true;

                        for (int i = x; i < dx; i++) {
                            if (grid[i][dz] != grid[x][z]) {
                                f = false;
                                break;
                            }
                        }

                        if (f) {
                            ++dz;
                        } else {
                            break;
                        }
                    }

                    this.addQuad(new Vector3i(x, y, z), Direction.TOP, grid[x][z], dx - x, 1, dz
                            - z);

                    for (int i = x; i < dx; i++) {
                        for (int j = z; j < dz; j++) {
                            grid[i][j] = null;
                        }
                    }
                }
            }
        }

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

    public void addQuad(Vector3i position, Direction direction, BlockType blockType, int width, int height, int depth) {
        Quad quad = addQuad(position, direction, blockType);

        quad.scale(direction, width, height, depth);

        quad.translate(position);
    }

    public Quad addQuad(Vector3i position, Direction direction, BlockType blockType) {
        int textureId = blockType.getIndex() * 6 + direction.getIndex();

        Texture texture = new Texture(direction, textureId);

        Quad quad = new Quad(position, direction, texture);

        this.quads.add(quad);

        return quad;
    }

    public void build() {
        int[] indices = this.getMergedIndices();
        int[] textures = this.getMergedTextures();

        float[] vertices = this.getMergedVertices();
        float[] coordinates = this.getMergedCoordinates();

        this.buffer.setup(vertices, indices, coordinates, textures);
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

    private int[] getMergedTextures() {
        int[] textures;

        this.textures.clear();

        for (Quad quad : this.quads) {
            Texture texture = quad.getTexture();

            for (int i = 0; i < 4; i++) {
                this.textures.add(texture.getId());
            }
        }

        textures = new int[this.textures.size()];

        for (int i = 0; i < this.textures.size(); i++) {
            textures[i] = this.textures.get(i);
            // Debug.debug(textures[i]);
        }

        return textures;
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
