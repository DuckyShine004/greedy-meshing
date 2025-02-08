package com.duckyshine.app.model;

import java.util.List;
import java.util.ArrayList;

import org.joml.Vector3i;

import com.duckyshine.app.math.Direction;

public class Mesh {
    private List<Quad> quads;

    public Mesh() {
        this.quads = new ArrayList<>();
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

    public void render() {
        // NOTE: Setup openGL params
        for (Quad quad : this.quads) {
            return;
        }
    }
}
