package com.duckyshine.app;

import org.joml.Vector3f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import com.duckyshine.app.camera.Camera;
import com.duckyshine.app.debug.Debug;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {
    private long window;

    private Camera camera;

    private void initialise() {
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialise GLFW");
        }

        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

        this.window = GLFW.glfwCreateWindow(720, 1080, "Greedy Meshing", NULL, NULL);

        if (this.window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        this.centreWindow();

        GLFW.glfwMakeContextCurrent(this.window);
    }

    private void centreWindow() {
        try (MemoryStack stack = stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);

            GLFW.glfwGetWindowSize(this.window, width, height);

            GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            int x = (vidmode.width() - width.get(0)) >> 1;
            int y = (vidmode.height() - height.get(0)) >> 1;

            GLFW.glfwSetWindowPos(this.window, x, y);
        }
    }

    private void initialiseScene() {
        this.camera = new Camera();

        this.camera.initialise();
    }

    private void run() {
        GL.createCapabilities();

        this.initialiseScene();

        while (!GLFW.glfwWindowShouldClose(this.window)) {
            this.update();
            this.render();

            GLFW.glfwSwapBuffers(this.window);
            GLFW.glfwPollEvents();
        }

        GLFW.glfwDestroyWindow(this.window);
        GLFW.glfwTerminate();
    }

    private void update() {
        float time = (float) GLFW.glfwGetTime();

        this.camera.update(this.window, time);
    }

    private void render() {
        GL11.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public static void main(String[] args) {
        Main main = new Main();

        main.initialise();
        main.run();
    }
}
