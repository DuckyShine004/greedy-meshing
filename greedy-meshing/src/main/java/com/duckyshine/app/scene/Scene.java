package com.duckyshine.app.scene;

import java.util.ArrayList;
import java.util.List;

import com.duckyshine.app.model.Chunk;
import com.duckyshine.app.model.Mesh;

public class Scene {
    private List<Chunk> chunks;

    public Scene() {
        this.chunks = new ArrayList<>();
    }

    public void generate() {
        Chunk chunk = new Chunk(0, 0, 0);

        chunk.generate();

        chunk.getMesh().render();
    }

    public void render() {
        for (Chunk chunk : chunks) {
            Mesh mesh = chunk.getMesh();

            mesh.render();
        }
    }
}
