package com.duckyshine.app.scene;

import java.util.ArrayList;
import java.util.List;

import com.duckyshine.app.model.Chunk;
import com.duckyshine.app.model.Mesh;
import com.duckyshine.app.shader.Shader;

public class Scene {
    private Shader shader;

    private List<Chunk> chunks;

    public Scene() {
        this.chunks = new ArrayList<>();
    }

    public Scene(Shader shader) {
        this.chunks = new ArrayList<>();

        this.shader = shader;
    }

    public void generate() {
        Chunk chunk = new Chunk(0, 0, 0);

        chunk.generate();

        this.chunks.add(chunk);
    }

    public void render() {
        for (Chunk chunk : chunks) {
            Mesh mesh = chunk.getMesh();

            mesh.render();
        }
    }
}
