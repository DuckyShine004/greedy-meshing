package com.duckyshine.app;

import java.nio.*;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import com.duckyshine.app.camera.Camera;
import com.duckyshine.app.sound.SoundPlayer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.Callbacks.*;

import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL11.*;

import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {
    private long window;

    private Camera camera;

    private SoundPlayer soundPlayer;

    private void initialise() {
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialise GLFW");
        }

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        this.window = glfwCreateWindow(720, 1080, "Greedy Meshing", NULL, NULL);

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
        this.soundPlayer.playMusic();
    }

    private void run() {
        this.initialiseScene();

        createCapabilities();

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

    }

    private void render() {
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public static void main(String[] args) {
        Main main = new Main();

        main.initialise();
        main.run();
    }
}
