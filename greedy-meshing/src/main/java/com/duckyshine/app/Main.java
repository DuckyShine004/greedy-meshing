package com.duckyshine.app;

import java.nio.*;

import org.joml.Matrix4f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import com.duckyshine.app.camera.Camera;
import com.duckyshine.app.debug.Debug;
import com.duckyshine.app.display.Display;
import com.duckyshine.app.display.DisplayType;
import com.duckyshine.app.scene.Scene;
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
    private long window;

    private Scene scene;

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

        this.initialiseWindow();

        this.initialiseCallbacks();

        glfwSetInputMode(this.window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    private void initialiseWindow() {
        Display display = Display.get();

        this.window = glfwCreateWindow(display.getWidth(), display.getHeight(), "Greedy Meshing", NULL, NULL);

        if (this.window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        this.centreWindow();

        glfwMakeContextCurrent(this.window);
    }

    private void initialiseCallbacks() {
        glfwSetKeyCallback(this.window, this::keyCallback);

        glfwSetCursorPosCallback(this.window, this::cursorPosCallback);

        glfwSetFramebufferSizeCallback(this.window, this::frameBufferSizeCallback);
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

    private void initialiseSceneObjects() {
        this.scene = new Scene();

        this.scene.generate();

        this.camera = new Camera();

        this.soundPlayer = new SoundPlayer();
    }

    private void initialiseSceneRenderingParameters() {
        AssetLoader.loadShaders();

        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
    }

    private void run() {
        this.initialiseSceneObjects();

        createCapabilities();

        this.initialiseSceneRenderingParameters();

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

        this.shader = AssetPool.getShader(ShaderType.WORLD.get());

        this.shader.use();

        this.shader.setMatrix4f("projectionViewMatrix", this.camera.getProjectionView());

        this.scene.render();
    }

    private void frameBufferSizeCallback(long window, int width, int height) {
        glViewport(0, 0, width, height);

        this.camera.updateAspectRatio(width, height);
    }

    private void keyCallback(long window, int key, int scanmode, int action, int mods) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
            this.exit();
        }
    }

    private void cursorPosCallback(long window, double mouseX, double mouseY) {
        this.camera.rotate(mouseX, mouseY);
    }

    private void exit() {
        glfwSetWindowShouldClose(this.window, true);

        this.soundPlayer.cleanup();
    }

    public static void main(String[] args) {
        Main main = new Main();

        main.initialise();
        main.run();
    }
}
