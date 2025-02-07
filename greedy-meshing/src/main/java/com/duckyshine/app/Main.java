package com.duckyshine.app;

import java.nio.*;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import com.duckyshine.app.camera.Camera;

import com.duckyshine.app.shader.Shader;

import com.duckyshine.app.shader.ShaderType;

import com.duckyshine.app.sound.SoundPlayer;

import com.duckyshine.app.asset.AssetLoader;
import com.duckyshine.app.asset.AssetPool;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.Callbacks.*;

import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {
    private int vao;

    private long window;

    private Shader shader;

    private Camera camera;

    private SoundPlayer soundPlayer;

    private void initialise() {
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialise GLFW");
        }

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        this.window = glfwCreateWindow(1080, 720, "Greedy Meshing", NULL, NULL);

        if (this.window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        this.centreWindow();

        glfwMakeContextCurrent(this.window);
    }

    private void centreWindow() {
        try (MemoryStack stack = stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);

            glfwGetWindowSize(this.window, width, height);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            int x = (vidmode.width() - width.get(0)) >> 1;
            int y = (vidmode.height() - height.get(0)) >> 1;

            glfwSetWindowPos(this.window, x, y);
        }
    }

    private void initialiseScene() {
        this.camera = new Camera();

        this.soundPlayer = new SoundPlayer();
    }

    private void run() {
        this.initialiseScene();

        createCapabilities();

        AssetLoader.loadShaders();

        float vertices[] = {
                0.5f, 0.5f, 0.0f, // top right
                0.5f, -0.5f, 0.0f, // bottom right
                -0.5f, -0.5f, 0.0f, // bottom left
                -0.5f, 0.5f, 0.0f // top left
        };

        int indices[] = { // note that we start from 0!
                0, 1, 3, // first Triangle
                1, 2, 3 // second Triangle
        };

        this.vao = glGenVertexArrays();
        int vbo = glGenBuffers();
        int ebo = glGenBuffers();

        glBindVertexArray(this.vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices).flip(); // flip() prepares the buffer for reading by OpenGL
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Bind the EBO and upload index data
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        // Create an IntBuffer and fill it with index data
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.length);
        indexBuffer.put(indices).flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

        // Configure the vertex attribute pointer
        // In this example, we assume:
        // - The vertex shader expects the vertex position at location 0.
        // - Each vertex consists of 3 floats.
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // Unbind the VBO (the EBO remains bound to the VAO)
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        // Unbind the VAO so that subsequent VAO calls don’t modify this one.
        glBindVertexArray(0);

        while (!glfwWindowShouldClose(this.window)) {
            this.update();
            this.render();

            glfwSwapBuffers(this.window);
            glfwPollEvents();
        }

        glfwDestroyWindow(this.window);
        glfwTerminate();
    }

    private void update() {
        float time = (float) glfwGetTime();

        this.camera.update(this.window, time);
        this.soundPlayer.playMusic();
    }

    private void render() {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        this.shader = AssetPool.getShader(ShaderType.WORLD.getType());

        this.shader.use();

        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

    }

    public static void main(String[] args) {
        Main main = new Main();

        main.initialise();
        main.run();
    }
}
